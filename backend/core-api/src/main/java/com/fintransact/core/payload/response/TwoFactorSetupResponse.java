package com.fintransact.core.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwoFactorSetupResponse {
    private String secret;
    private String qrCodeUrl;
}
