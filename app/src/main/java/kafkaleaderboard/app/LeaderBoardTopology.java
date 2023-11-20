package kafkaleaderboard.app;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.state.KeyValueStore;

import kafkaleaderboard.app.model.Player;
import kafkaleaderboard.app.model.Product;
import kafkaleaderboard.app.model.ScoreEvent;
import kafkaleaderboard.app.model.join.Enriched;
import kafkaleaderboard.app.model.join.ScoreWithPlayer;
import kafkaleaderboard.app.serialization.json.JsonSerdes;

class LeaderBoardTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KTable<String, Player> players = builder.table("players", Consumed.with(Serdes.String(), JsonSerdes.Player()));

        KStream<String, ScoreEvent> scoreEvents = builder
                .stream("score-events", Consumed.with(Serdes.ByteArray(), JsonSerdes.ScoreEvent()))
                .selectKey((k, v) -> v.getPlayerId().toString());

        GlobalKTable<String, Product> products = builder.globalTable("products",
                Consumed.with(Serdes.String(), JsonSerdes.Product()));

        Joined<String, ScoreEvent, Player> playerJoinParams = Joined.with(Serdes.String(), JsonSerdes.ScoreEvent(),
                JsonSerdes.Player());

        ValueJoiner<ScoreEvent, Player, ScoreWithPlayer> scorePlayerJoiner = (score,
                player) -> new ScoreWithPlayer(score, player);
        KStream<String, ScoreWithPlayer> withPlayers = scoreEvents.join(players, scorePlayerJoiner, playerJoinParams);

        KeyValueMapper<String, ScoreWithPlayer, String> keyMapper = (leftKey, scoreWithPlayer) -> {
            return String.valueOf(scoreWithPlayer.getScoreEvent().getProductId());
        };

        ValueJoiner<ScoreWithPlayer, Product, Enriched> productJoiner = (scoreWithPlayer,
                product) -> new Enriched(scoreWithPlayer, product);
        KStream<String, Enriched> withProducts = withPlayers.join(products, keyMapper, productJoiner);
        withProducts.print(Printed.<String, Enriched>toSysOut().withLabel("with-products"));

        KGroupedStream<String, Enriched> grouped = withProducts.groupBy(
                (key, value) -> value.getProductId().toString(),
                Grouped.with(Serdes.String(), JsonSerdes.Enriched()));

        Initializer<HighScores> highScoresInitializer = HighScores::new;

        Aggregator<String, Enriched, HighScores> highScoresAdder = (key, value, aggregate) -> aggregate.add(value);

        KTable<String, HighScores> highScores = grouped.aggregate(
                highScoresInitializer,
                highScoresAdder,
                Materialized.<String, HighScores, KeyValueStore<Bytes, byte[]>>as("leader-boards")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(JsonSerdes.HighScores()));

        highScores.toStream().to("high-scores");
        return builder.build();
    }
}
