package cn.xpbootcamp.legacy_code.service;

import cn.xpbootcamp.legacy_code.entity.Order;
import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.repository.UserRepository;
import cn.xpbootcamp.legacy_code.repository.UserRepositoryImpl;

import java.util.UUID;

public class WalletServiceImpl implements WalletService {

    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public String moveMoney(String id, Order order) {
        User buyer = order.getBuyer();
        Double amount = order.getAmount();
        if (buyer.getBalance() >= amount) {
            User seller = order.getSeller();
            seller.setBalance(seller.getBalance() + amount);
            buyer.setBalance(buyer.getBalance() - amount);
            return UUID.randomUUID().toString() + id;
        }
        throw new IllegalArgumentException("buyer Not enough money to buy the product");
    }
}
