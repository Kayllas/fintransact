package com.fintransact.core.payload.request;

import lombok.Data;

@Data
public class TwoFactorVerifyRequest {
    private String code;
}
