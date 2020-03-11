package cn.xpbootcamp.legacy_code.entity;

import java.util.Objects;

public class User {
    private Long id;
    private Double balance;

    public User(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public boolean unReasonableId(){
        return Objects.isNull(id);
    }
}
