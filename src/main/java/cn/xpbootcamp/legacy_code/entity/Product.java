package cn.xpbootcamp.legacy_code.entity;

public class Product {
    private Long id;
    private Double amount;

    public Product(Long id, Double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount(){
        return amount;
    }

    public boolean unReasonableAmount(){
        return amount < 0.0;
    }
}
