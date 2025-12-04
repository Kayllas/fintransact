package com.fintransact.core.payload.response;

import com.fintransact.core.model.enums.PixKeyType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PixKeyResponse {
    private Long id;
    private String key;
    private PixKeyType type;
}
