package com.fintransact.core.payload.request;

import com.fintransact.core.model.enums.PixKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PixKeyRequest {
    @NotBlank
    private String key;

    @NotNull
    private PixKeyType type;
}
