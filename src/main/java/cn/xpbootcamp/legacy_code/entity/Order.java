package cn.xpbootcamp.legacy_code.entity;

import cn.xpbootcamp.legacy_code.utils.IdGenerator;

import javax.transaction.InvalidTransactionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Order {

    private Long id;

    private User buyer;
    private User seller;
    private Product product;

    private Double amount;

    private LocalDateTime createdTimestamp;

    public Order(User buyer, User seller, Product product) {
        this.id = IdGenerator.generateOrderId();
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;

        this.amount = product.getAmount();
        this.createdTimestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }


    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void verifyParameter() throws InvalidTransactionException {
        if (buyer.unReasonableId() || (seller.unReasonableId()) || product.unReasonableAmount()) {
            throw new InvalidTransactionException("This is an invalid order");
        }
    }


    public boolean hasDaysPastDue(int day){
        long daysDiff = ChronoUnit.DAYS.between(createdTimestamp, LocalDateTime.now());
        return daysDiff > 20;
    }



}
