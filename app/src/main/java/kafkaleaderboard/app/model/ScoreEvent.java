package kafkaleaderboard.app.model;




public class ScoreEvent{
    private Long playerId;
    private Long productId;
    private Double score;

    public Long getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId( Long id) {
        this.playerId = id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public Double getScore() {
        return this.score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "{ id" + getPlayerId() + " name " +  getProductId() + " }" ;
    }
}