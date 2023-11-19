package kafkaleaderboard.app.model;

public class Player {
    private Long playerId;
    private String name;

    public Long getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId( Long id) {
        this.playerId = id;
    }

    public String getPlayerName() {
        return this.name;
    }

    public void setPlayerName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{ id" + getPlayerId() + " name " +  getPlayerName() + " }" ;
    }
}
