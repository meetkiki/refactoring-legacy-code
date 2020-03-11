package cn.xpbootcamp.legacy_code.entity;

import cn.xpbootcamp.legacy_code.enums.WalletStatus;
import cn.xpbootcamp.legacy_code.utils.IdGenerator;

import java.util.Objects;

import static cn.xpbootcamp.legacy_code.enums.WalletStatus.EXECUTED;
import static cn.xpbootcamp.legacy_code.enums.WalletStatus.EXPIRED;
import static cn.xpbootcamp.legacy_code.enums.WalletStatus.FAILED;
import static cn.xpbootcamp.legacy_code.enums.WalletStatus.TO_BE_EXECUTED;

public class Wallet {

    private String preAssignedId;

    private Order order;
    private WalletStatus status;


    public Wallet(Order order) {
        this.order = order;
    }

    public String getPreAssignedId() {
        if (isEmptyId()) {
            this.preAssignedId = IdGenerator.generateTransactionId();
        }
        return preAssignedId;
    }

    private boolean isEmptyId() {
        return preAssignedId == null || preAssignedId.isEmpty();
    }

    public boolean isExecuted(){
        return status == EXECUTED;
    }

    public Wallet expired(){
        this.status = EXPIRED;
        return this;
    }

    public Wallet failed(){
        this.status = FAILED;
        return this;
    }


    public Wallet executed(){
        this.status = EXECUTED;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public WalletStatus getStatus() {
        if (Objects.isNull(status)){
            status = TO_BE_EXECUTED;
        }
        return status;
    }
}
