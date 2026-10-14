package com.andre.banking.controller;

import com.andre.banking.dto.TransferRequest;
import com.andre.banking.service.TransferService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> transfer(
            @Valid @RequestBody TransferRequest request) {

        transferService.transfer(
            request.fromAccountId(),
            request.toAccountId(),
            request.amount(),
            request.requestId()
        );

        return ResponseEntity.ok(
            Map.of("message", "Transfer completed")
        );
    }
}