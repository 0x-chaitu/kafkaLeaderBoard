
package kafkaleaderboard.app;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kafkaleaderboard.app.model.join.Enriched;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * LeaderboardService
 */
public class LeaderboardService {
    private final HostInfo hostInfo;
    private final KafkaStreams streams;

    private static final Logger log = LoggerFactory.getLogger(LeaderboardService.class);

    LeaderboardService(HostInfo hostInfo, KafkaStreams streams) {
        this.hostInfo = hostInfo;
        this.streams = streams;
    }

    ReadOnlyKeyValueStore<String, HighScores> getStore() {
        return streams.store(
                StoreQueryParameters.fromNameAndType(
                        "leader-boards",
                        QueryableStoreTypes.keyValueStore()));
    }

    void getAll(Context ctx) {
        String from = ctx.pathParam("from");
        String to = ctx.pathParam("to");

        Map<String, List<Enriched>> leaderBoard = new HashMap<>();

        KeyValueIterator<String, HighScores> range = getStore().all();
        while (range.hasNext()) {
            KeyValue<String, HighScores> next = range.next();
            String game = next.key;
            HighScores highScores = next.value;
            leaderBoard.put(game, highScores.toList());
        }
        range.close();
        ctx.json(leaderBoard);
    }

    public void start() {
        Javalin app = Javalin.create().start(hostInfo.port());

        app.get("/leaderboard", this::getAll);

    }

}