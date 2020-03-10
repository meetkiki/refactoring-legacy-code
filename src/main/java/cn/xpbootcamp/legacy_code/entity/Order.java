package cn.xpbootcamp.legacy_code.entity;

import cn.xpbootcamp.legacy_code.utils.IdGenerator;

import javax.transaction.InvalidTransactionException;

public class Order {

    private Long id;

    private User buyer;
    private User seller;
    private Product product;

    private Double amount;

    private Long createdTimestamp;

    public Order(User buyer, User seller, Product product) {
        this.id = IdGenerator.generateOrderId();
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        this.amount = product.getAmount();
        this.createdTimestamp = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void verifyParameter() throws InvalidTransactionException {
        if (buyer.unReasonableId() || (seller.unReasonableId()) || product.unReasonableAmount()) {
            throw new InvalidTransactionException("This is an invalid order");
        }
    }



}
