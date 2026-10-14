package com.andre.banking.service;

import com.andre.banking.entity.Account;
import com.andre.banking.entity.AccountType;
import com.andre.banking.repository.AccountRepository;

import com.andre.banking.entity.Transaction;
import com.andre.banking.entity.TransactionType;
import com.andre.banking.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EntityManager entityManager;
    private final TransactionRepository transactionRepository;

    public AccountService(
            AccountRepository accountRepository,
            EntityManager entityManager,
            TransactionRepository transactionRepository){
        
        this.accountRepository = accountRepository;
        this.entityManager = entityManager;
        this.transactionRepository = transactionRepository;        
        }

    @Transactional
    public Account createAccount(AccountType accountType) {

        if (accountType == null) {
            throw new IllegalArgumentException(
                "Account type is required"
            );
        }

        Account account = new Account();

        account.setAccountNumber(UUID.randomUUID().toString());
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {

        return accountRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Account not found: " + id
                )
            );
    }

    @Transactional
    public Account deposit(
            Long accountId,
            BigDecimal amount,
            UUID requestId) {

        if (requestId == null) {
            throw new IllegalArgumentException(
                "Request ID is required"
            );
        }

        Account account = entityManager.find(
            Account.class,
            accountId,
            LockModeType.PESSIMISTIC_WRITE
        );

        if (account == null) {
            throw new IllegalArgumentException(
                "Account not found: " + accountId
            );
        }

        var existing = transactionRepository.findByRequestId(requestId);

        if (existing.isPresent()) {
            Transaction previous = existing.get();

            if (!previous.getAccount().getId().equals(accountId)
                    || previous.getAmount().compareTo(amount) != 0
                    || previous.getType() != TransactionType.DEPOSIT) {
                throw new IllegalStateException(
                    "Request ID already used for a different operation"
                );
            }

            return account;
        }

        account.deposit(amount);

        Transaction transaction = new Transaction(
            requestId,
            account,
            TransactionType.DEPOSIT,
            amount
        );

        transactionRepository.save(transaction);

        return account;
    }

    @Transactional
    public Account withdraw(
            Long accountId,
            BigDecimal amount,
            UUID requestId) {

        if (requestId == null) {
            throw new IllegalArgumentException(
                "Request ID is required"
            );
        }

        if (amount == null ||
            amount.signum() <= 0 ||
            amount.scale() > 2) {
            throw new IllegalArgumentException(
                "Invalid withdrawal amount"
            );
        }

        Account account = entityManager.find(
            Account.class,
            accountId,
            LockModeType.PESSIMISTIC_WRITE
        );

        if (account == null) {
            throw new IllegalArgumentException(
                "Account not found: " + accountId
            );
        }

        var existing =
            transactionRepository.findByRequestId(requestId);

        if (existing.isPresent()) {

            Transaction previous = existing.get();

            if (!previous.getAccount().getId().equals(accountId)
                    || previous.getAmount().compareTo(amount) != 0
                    || previous.getType() != TransactionType.WITHDRAWAL) {

                throw new IllegalStateException(
                    "Request ID already used for another operation"
                );
            }

            return account;
        }

        account.withdraw(amount);

        Transaction transaction = new Transaction(
            requestId,
            account,
            TransactionType.WITHDRAWAL,
            amount
        );

        transactionRepository.save(transaction);

        return account;
    }

    
}