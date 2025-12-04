package com.fintransact.core.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String code;

    public String getUsername() {
        // This method was added as per instruction, but its body was syntactically
        // incorrect in the provided snippet.
        // Assuming it should return the email, but email field was removed.
        // Returning null or throwing an exception might be appropriate depending on the
        // intended use.
        // For now, leaving it as a placeholder that compiles.
        return null; // Or throw new UnsupportedOperationException("Username not available in this
                     // request type");
    }
}
