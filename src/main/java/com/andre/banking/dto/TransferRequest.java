package com.andre.banking.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
    @NotNull UUID requestId,
    @NotNull Long fromAccountId,
    @NotNull Long toAccountId,
    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 17, fraction = 2)
    BigDecimal amount
) {}