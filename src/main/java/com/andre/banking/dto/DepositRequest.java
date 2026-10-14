package com.andre.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositRequest(
    @NotNull UUID requestId,

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 17, fraction = 2)
    BigDecimal amount

) {}