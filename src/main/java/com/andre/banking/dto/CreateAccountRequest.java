package com.andre.banking.dto;

import com.andre.banking.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
    @NotNull AccountType accountType
) {}