package cn.xpbootcamp.legacy_code.service;

import cn.xpbootcamp.legacy_code.entity.Order;

public interface WalletService {

    String moveMoney(String id, Order order);

}
