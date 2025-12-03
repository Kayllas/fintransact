package com.fintransact.core.service;

import com.fintransact.core.model.Account;
import com.fintransact.core.model.Transaction;
import com.fintransact.core.model.TransactionType;
import com.fintransact.core.repository.AccountRepository;
import com.fintransact.core.repository.TransactionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public Transaction transfer(Long sourceUserId, String targetAccountNumber, BigDecimal amount) {
        Account sourceAccount = accountRepository.findByUserId(sourceUserId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = Transaction.builder()
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Send notification
        String message = "Transfer of " + amount + " from " + sourceAccount.getUser().getName() + " to "
                + targetAccount.getUser().getName();
        rabbitTemplate.convertAndSend("transaction-notifications", message);

        return savedTransaction;
    }

    public List<Transaction> getHistory(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findBySourceAccountIdOrTargetAccountIdOrderByTimestampDesc(account.getId(),
                account.getId());
    }

    public Account getAccount(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
