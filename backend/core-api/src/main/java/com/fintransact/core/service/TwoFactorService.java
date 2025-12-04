package com.fintransact.core.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorService {

    private final GoogleAuthenticator gAuth;

    public TwoFactorService() {
        this.gAuth = new GoogleAuthenticator();
    }

    public String generateNewSecret() {
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public String generateQrCodeImageUri(String secret, String accountName) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder(secret).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("FinTransact", accountName, key);
    }

    public boolean isOtpValid(String secret, String code) {
        try {
            int verificationCode = Integer.parseInt(code);
            return gAuth.authorize(secret, verificationCode);
        } catch (Exception e) {
            return false;
        }
    }
}
