package com.fintransact.core.payload.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PixTransferRequest {
    @NotBlank
    private String targetKey;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
