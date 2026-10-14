package com.andre.banking.service;

import com.andre.banking.entity.Account;
import com.andre.banking.entity.Transaction;
import com.andre.banking.entity.TransactionType;
import com.andre.banking.repository.TransactionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferService {

    private final EntityManager entityManager;
    private final TransactionRepository transactionRepository;

    public TransferService(
            EntityManager entityManager,
            TransactionRepository transactionRepository) {
        this.entityManager = entityManager;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(
            Long fromId,
            Long toId,
            BigDecimal amount,
            UUID requestId) {

        if (requestId == null || fromId == null || toId == null) {
            throw new IllegalArgumentException("Missing required fields");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException(
                "Cannot transfer to the same account"
            );
        }

        if (amount == null || amount.signum() <= 0 ||
                amount.scale() > 2) {
            throw new IllegalArgumentException("Invalid amount");
        }

        // Lock accounts in a consistent order.
        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);

        Account first = entityManager.find(
            Account.class, firstId, LockModeType.PESSIMISTIC_WRITE
        );

        Account second = entityManager.find(
            Account.class, secondId, LockModeType.PESSIMISTIC_WRITE
        );

        if (first == null || second == null) {
            throw new IllegalArgumentException("Account not found");
        }

        Account from = fromId.equals(firstId) ? first : second;
        Account to = toId.equals(firstId) ? first : second;

        // We'll add a proper transfer idempotency record next.
        if (transactionRepository.findByRequestId(requestId).isPresent()) {
            throw new IllegalStateException("Request ID already used");
        }

        from.withdraw(amount);
        to.deposit(amount);

        // Temporary distinct IDs for the two ledger entries.
        transactionRepository.save(new Transaction(
            UUID.randomUUID(), from,
            TransactionType.TRANSFER_OUT, amount
        ));

        transactionRepository.save(new Transaction(
            UUID.randomUUID(), to,
            TransactionType.TRANSFER_IN, amount
        ));
    }
}