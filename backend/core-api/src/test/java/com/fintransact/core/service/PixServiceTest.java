package com.fintransact.core.service;

import com.fintransact.core.exception.InsufficientBalanceException;
import com.fintransact.core.exception.PixKeyAlreadyExistsException;
import com.fintransact.core.exception.ResourceNotFoundException;
import com.fintransact.core.model.Account;
import com.fintransact.core.model.PixKey;
import com.fintransact.core.model.Transaction;
import com.fintransact.core.model.User;
import com.fintransact.core.model.enums.PixKeyType;
import com.fintransact.core.repository.AccountRepository;
import com.fintransact.core.repository.PixKeyRepository;
import com.fintransact.core.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PixServiceTest {

    @Mock
    private PixKeyRepository pixKeyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PixService pixService;

    private User user;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        account = new Account();
        account.setId(1L);
        account.setUser(user);
        account.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void createKey_Success() {
        when(pixKeyRepository.existsByKey(anyString())).thenReturn(false);
        when(accountRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(account));
        when(pixKeyRepository.save(any(PixKey.class))).thenAnswer(i -> i.getArguments()[0]);

        PixKey createdKey = pixService.createKey(user, "test@test.com", PixKeyType.EMAIL);

        assertNotNull(createdKey);
        assertEquals("test@test.com", createdKey.getKey());
        assertEquals(PixKeyType.EMAIL, createdKey.getType());
    }

    @Test
    void createKey_AlreadyExists() {
        when(pixKeyRepository.existsByKey(anyString())).thenReturn(true);

        assertThrows(PixKeyAlreadyExistsException.class, () -> {
            pixService.createKey(user, "test@test.com", PixKeyType.EMAIL);
        });
    }

    @Test
    void transfer_Success() {
        User receiverUser = new User();
        receiverUser.setId(2L);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setUser(receiverUser);
        receiverAccount.setBalance(BigDecimal.ZERO);

        PixKey targetKey = new PixKey();
        targetKey.setAccount(receiverAccount);

        when(accountRepository.findByUserId(user.getId())).thenReturn(Optional.of(account));
        when(pixKeyRepository.findByKey("receiver@test.com")).thenReturn(Optional.of(targetKey));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction transaction = pixService.transfer(user, "receiver@test.com", new BigDecimal("100.00"));

        assertNotNull(transaction);
        assertEquals(new BigDecimal("900.00"), account.getBalance());
        assertEquals(new BigDecimal("100.00"), receiverAccount.getBalance());
    }

    @Test
    void transfer_InsufficientBalance() {
        User receiverUser = new User();
        receiverUser.setId(2L);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setUser(receiverUser);

        PixKey targetKey = new PixKey();
        targetKey.setAccount(receiverAccount);

        when(accountRepository.findByUserId(user.getId())).thenReturn(Optional.of(account));
        when(pixKeyRepository.findByKey("receiver@test.com")).thenReturn(Optional.of(targetKey));

        assertThrows(InsufficientBalanceException.class, () -> {
            pixService.transfer(user, "receiver@test.com", new BigDecimal("2000.00"));
        });
    }
}
