package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.entity.Order;
import cn.xpbootcamp.legacy_code.entity.Wallet;
import cn.xpbootcamp.legacy_code.enums.WalletStatus;
import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.service.WalletServiceImpl;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;

import javax.transaction.InvalidTransactionException;

import static cn.xpbootcamp.legacy_code.enums.WalletStatus.EXECUTED;

public class WalletTransaction {
    private String id;
    private Wallet wallet;
    private String walletTransactionId;


    public WalletTransaction(Wallet wallet) {
        this.id = wallet.getPreAssignedId();
        this.wallet = wallet;
        this.stitchingId();
    }

    private void stitchingId() {
        if (!this.id.startsWith("t_")) {
            this.id = "t_" + this.id;
        }
    }

    public boolean execute() throws InvalidTransactionException {
        if (hasExecuted()) return true;

        Order order = this.wallet.getOrder();
        order.verifyParameter();

        boolean isLocked = false;
        try {
            isLocked = RedisDistributedLock.getSingletonInstance().lock(id);

            // 锁定未成功，返回false
            if (!isLocked) {
                return false;
            }
            if (hasExecuted()) return true; // double check
            long executionInvokedTimestamp = System.currentTimeMillis();
            // 交易超过20天
            Long createdTimestamp = order.getCreatedTimestamp();
            if (executionInvokedTimestamp - createdTimestamp > 1728000000) {
                this.wallet.expired();
                return false;
            }
            WalletService walletService = new WalletServiceImpl();
            String walletTransactionId = walletService.moveMoney(id, order);
            if (walletTransactionId != null) {
                this.walletTransactionId = walletTransactionId;
                this.wallet.executed();
                return true;
            } else {
                this.wallet.failed();
                return false;
            }
        } finally {
            if (isLocked) {
                RedisDistributedLock.getSingletonInstance().unlock(id);
            }
        }
    }



    private boolean hasExecuted() {
        if (this.wallet.isExecuted()) {
            return true;
        }
        return false;
    }


}