package com.fintransact.core.service;

import com.fintransact.core.model.Account;
import com.fintransact.core.model.PixKey;
import com.fintransact.core.model.Transaction;
import com.fintransact.core.model.User;
import com.fintransact.core.model.enums.PixKeyType;
import com.fintransact.core.model.TransactionType;
import com.fintransact.core.repository.AccountRepository;
import com.fintransact.core.repository.PixKeyRepository;
import com.fintransact.core.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PixService {

    @Autowired
    private PixKeyRepository pixKeyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public PixKey createKey(User user, String key, PixKeyType type) {
        if (pixKeyRepository.existsByKey(key)) {
            throw new RuntimeException("Pix Key already exists");
        }

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        PixKey pixKey = PixKey.builder()
                .key(key)
                .type(type)
                .account(account)
                .build();

        return pixKeyRepository.save(pixKey);
    }

    public List<PixKey> getKeysByUser(User user) {
        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return pixKeyRepository.findByAccountId(account.getId());
    }

    public Account getAccountByKey(String key) {
        PixKey pixKey = pixKeyRepository.findByKey(key)
                .orElseThrow(() -> new RuntimeException("Pix Key not found"));
        return pixKey.getAccount();
    }

    @Transactional
    public Transaction transfer(User sender, String targetKey, BigDecimal amount) {
        Account sourceAccount = accountRepository.findByUserId(sender.getId())
                .orElseThrow(() -> new RuntimeException("Source Account not found"));

        Account targetAccount = getAccountByKey(targetKey);

        if (sourceAccount.getId().equals(targetAccount.getId())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Debit source
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        accountRepository.save(sourceAccount);

        // Credit target
        targetAccount.setBalance(targetAccount.getBalance().add(amount));
        accountRepository.save(targetAccount);

        // Create Transaction Record
        Transaction transaction = Transaction.builder()
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .amount(amount)
                .type(TransactionType.TRANSFER) // Reusing TRANSFER type, could add PIX type later
                .timestamp(LocalDateTime.now())
                .referenceId(UUID.randomUUID().toString())
                .build();

        return transactionRepository.save(transaction);
    }
}
