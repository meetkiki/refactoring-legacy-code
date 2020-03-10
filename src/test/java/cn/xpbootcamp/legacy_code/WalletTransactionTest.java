package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.entity.Order;
import cn.xpbootcamp.legacy_code.entity.Product;
import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.entity.Wallet;
import org.junit.jupiter.api.Test;

import javax.transaction.InvalidTransactionException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletTransactionTest {

    @Test
    void shouldThrowInvalidTransactionExceptionWhenIllegalArgument() throws InvalidTransactionException {
        User buyer = new User(1L, 100.0);
        User seller = new User(2L, 0.0);
        Product product = new Product(1L,-1.0);
        Order order = new Order(buyer, seller, product);
        Wallet wallet = new Wallet(order);
        WalletTransaction transaction = new WalletTransaction(wallet);

        assertThrows(InvalidTransactionException.class, () -> transaction.execute());
    }

}
