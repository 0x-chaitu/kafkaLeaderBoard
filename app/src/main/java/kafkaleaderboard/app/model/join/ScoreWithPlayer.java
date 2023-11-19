package kafkaleaderboard.app.model.join;

import kafkaleaderboard.app.model.Player;
import kafkaleaderboard.app.model.ScoreEvent;

public class ScoreWithPlayer {
    private ScoreEvent scoreEvent;
    private Player player;

    private ScoreWithPlayer(ScoreEvent scoreEvent, Player player) {
        this.scoreEvent = scoreEvent;
        this.player = player;
    }

    public ScoreEvent getScoreEvent() {
        return this.scoreEvent;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "{" + " scoreEvent='" + getScoreEvent() + "'" + ", player='" + getPlayer() + "'" + "}";
    }

}
