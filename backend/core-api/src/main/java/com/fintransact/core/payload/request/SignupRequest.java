package com.fintransact.core.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 11, max = 14, message = "CPF must be 11-14 characters (with or without formatting)")
    private String cpf;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
