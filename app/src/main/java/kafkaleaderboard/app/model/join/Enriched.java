package kafkaleaderboard.app.model.join;

import kafkaleaderboard.app.model.Product;

public class Enriched implements Comparable<Enriched> {

    private Long playerId;
    private Long productId;
    private String playerName;
    private String gameName;
    private Double score;

    public Enriched(ScoreWithPlayer scoreEventWithPlayer, Product product) {
        this.playerId = scoreEventWithPlayer.getPlayer().getPlayerId();
        ;
        this.productId = product.getProductId();
        this.playerName = scoreEventWithPlayer.getPlayer().getPlayerName();
        this.gameName = product.getProductName();
        this.score = scoreEventWithPlayer.getScoreEvent().getScore();

    }

    @Override
    public int compareTo(Enriched o) {
        return Double.compare(o.score, score);
    }

    public Long getPlayerId() {
        return this.playerId;
      }
    
      public Long getProductId() {
        return this.productId;
      }
    
      public String getPlayerName() {
        return this.playerName;
      }
    
      public String getGameName() {
        return this.gameName;
      }
    
      public Double getScore() {
        return this.score;
      }
    
      @Override
      public String toString() {
        return "{"
            + " playerId='"
            + getPlayerId()
            + "'"
            + ", playerName='"
            + getPlayerName()
            + "'"
            + ", gameName='"
            + getGameName()
            + "'"
            + ", score='"
            + getScore()
            + "'"
            + "}";
      }

}
