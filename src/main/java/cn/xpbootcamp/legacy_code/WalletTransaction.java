package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.entity.Order;
import cn.xpbootcamp.legacy_code.entity.Wallet;
import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;

import javax.transaction.InvalidTransactionException;

public class WalletTransaction {

    private RedisDistributedLock distributedLock;
    private WalletService walletService;


    public WalletTransaction(RedisDistributedLock distributedLock, WalletService walletService) {
        this.distributedLock = distributedLock;
        this.walletService = walletService;
    }


    public boolean execute(Wallet wallet) throws InvalidTransactionException {
        if (wallet.isExecuted()) {
            return true;
        }

        Order order = wallet.getOrder();
        order.verifyParameter();

        String id = wallet.getPreAssignedId();

        boolean isLocked = false;
        try {
            isLocked = distributedLock.lock(id);

            // 锁定未成功，返回false
            if (!isLocked) {
                return false;
            }

            // double check
            if (wallet.isExecuted()) {
                return true;
            }

            // 交易超过20天
            if (order.hasDaysPastDue(20)) {
                wallet.expired();
                return false;
            }

            String walletTransactionId = walletService.moveMoney(id, order);
            wallet.executed(walletTransactionId);
            return true;
        } catch (IllegalArgumentException exception) {
            wallet.failed();
            throw exception;
        } finally {
            if (isLocked) {
                distributedLock.unlock(id);
            }
        }
    }
}