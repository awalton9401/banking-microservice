 package com.andre.banking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    

    public Account() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Adds a valid deposit amount to the balance of an active account. */
    public void deposit(BigDecimal amount) {

        // Require a positive amount with no more than two decimal places.
        if (amount == null ||
            amount.signum() <= 0 ||
            amount.scale() > 2) {
            throw new IllegalArgumentException(
                "Invalid deposit amount"
            );
        }

        // Only active accounts can receive deposits.
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                "Account is not active"
            );
        }

        // BigDecimal is immutable, so assign the updated balance back to the account.
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {

        if (amount == null ||
            amount.signum() <= 0 ||
            amount.scale() > 2) {

            throw new IllegalArgumentException(
                "Withdrawal amount must be positive with at most 2 decimal places"
            );
        }

        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                "Account is not active"
            );
        }

        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException(
                "Insufficient funds"
            );
        }

        this.balance = this.balance.subtract(amount);
    }
}
