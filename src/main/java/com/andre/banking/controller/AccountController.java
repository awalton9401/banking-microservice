package com.andre.banking.controller;

import com.andre.banking.dto.CreateAccountRequest;
import com.andre.banking.entity.Account;
import com.andre.banking.service.AccountService;
import com.andre.banking.dto.DepositRequest;
import com.andre.banking.dto.WithdrawalRequest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        Account account =
                accountService.createAccount(request.accountType());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(
            @PathVariable Long id) {

        Account account = accountService.getAccount(id);

        return ResponseEntity.ok(account);
    }

   @PostMapping("/{id}/deposits")
    public ResponseEntity<Account> deposit(
            @PathVariable Long id,
            @Valid @RequestBody DepositRequest request) {

        Account account = accountService.deposit(
            id,
            request.amount(),
            request.requestId()
        );

        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/withdrawals")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long id,
            @Valid @RequestBody WithdrawalRequest request) {

        Account account = accountService.withdraw(
            id,
            request.amount(),
            request.requestId()
        );

        return ResponseEntity.ok(account);
    }
}