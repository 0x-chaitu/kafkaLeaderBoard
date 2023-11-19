package kafkaleaderboard.app.model;

public class Product {
    private Long productId;
    private String name;

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId( Long id) {
        this.productId = id;
    }

    public String getProductName() {
        return this.name;
    }

    public void setProductName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{ id" + getProductId() + " name " +  getProductName() + " }" ;
    }
}
