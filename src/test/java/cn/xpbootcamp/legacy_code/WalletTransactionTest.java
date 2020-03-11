package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.entity.Order;
import cn.xpbootcamp.legacy_code.entity.Product;
import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.entity.Wallet;
import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.InvalidTransactionException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WalletTransactionTest {

    private static RedisDistributedLock distributedLock;
    private static WalletService walletService;

    @BeforeAll
    public static void before(){
        distributedLock = Mockito.mock(RedisDistributedLock.class);
        walletService = Mockito.mock(WalletService.class);
    }


    @Test
    void shouldThrowInvalidTransactionExceptionWhenIllegalArgument() {
        User buyer = new User(1L, 100.0);
        User seller = new User(2L, 0.0);
        Product product = new Product(1L, -1.0);
        Order order = new Order(buyer, seller, product);
        Wallet wallet = new Wallet(order);

        WalletTransaction transaction = new WalletTransaction(distributedLock,walletService);
        Mockito.when(distributedLock.lock(wallet.getPreAssignedId())).thenReturn(true);

        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> transaction.execute(wallet));
        assertEquals("This is an invalid order", exception.getMessage());
    }


    @Test
    void shouldReturnTrueWhenExecuted() throws InvalidTransactionException {
        User buyer = new User(1L, 100.0);
        User seller = new User(2L, 0.0);
        Product product = new Product(1L, 1.0);
        Order order = new Order(buyer, seller, product);
        Wallet wallet = new Wallet(order);
        wallet.executed("testWalletTransactionId");

        WalletTransaction transaction = new WalletTransaction(distributedLock,walletService);
        Mockito.when(distributedLock.lock(wallet.getPreAssignedId())).thenReturn(true);

        assertTrue(transaction.execute(wallet));
    }

    @Test
    void shouldReturnFalseWhenUnLocked() throws InvalidTransactionException {
        User buyer = new User(1L, 100.0);
        User seller = new User(2L, 0.0);
        Product product = new Product(1L, 1.0);
        Order order = new Order(buyer, seller, product);
        Wallet wallet = new Wallet(order);

        WalletTransaction transaction = new WalletTransaction(distributedLock,walletService);
        Mockito.when(distributedLock.lock(wallet.getPreAssignedId())).thenReturn(false);

        assertFalse(transaction.execute(wallet));
    }


    @Test
    void shouldReturnFalseWhenExpired20Days() throws InvalidTransactionException {
        User buyer = new User(1L, 100.0);
        User seller = new User(2L, 0.0);
        Product product = new Product(1L, 1.0);
        Order order = new Order(buyer, seller, product);

        order.setCreatedTimestamp(LocalDateTime.of(2020,1,20,10,00,00));
        Wallet wallet = new Wallet(order);

        WalletTransaction transaction = new WalletTransaction(distributedLock,walletService);
        Mockito.when(distributedLock.lock(wallet.getPreAssignedId())).thenReturn(false);

        assertFalse(transaction.execute(wallet));
    }




}
