package com.fintransact.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TwoFactorServiceTest {

    @InjectMocks
    private TwoFactorService twoFactorService;

    @Test
    void generateNewSecret_Success() {
        String secret = twoFactorService.generateNewSecret();
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
    }

    @Test
    void generateQrCodeImageUri_Success() {
        String secret = "JBSWY3DPEHPK3PXP";
        String email = "test@test.com";
        String uri = twoFactorService.generateQrCodeImageUri(secret, email);

        assertNotNull(uri);
        assertTrue(uri.contains(secret));
        assertTrue(uri.contains(email));
    }
}
