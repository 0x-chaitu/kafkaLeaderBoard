package kafkaleaderboard.app;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;

import kafkaleaderboard.app.model.Player;
import kafkaleaderboard.app.serialization.json.JsonSerdes;

class LeaderBoardTopology {
    

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KTable<String, Player> players = builder.table("players", Consumed.with(Serdes.String(), JsonSerdes.Player()));
    
        return builder.build();
    }
}
