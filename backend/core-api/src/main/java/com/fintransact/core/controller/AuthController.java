package com.fintransact.core.controller;

import com.fintransact.core.model.Account;
import com.fintransact.core.model.User;
import com.fintransact.core.payload.request.LoginRequest;
import com.fintransact.core.payload.request.SignupRequest;
import com.fintransact.core.payload.request.TwoFactorVerifyRequest;
import com.fintransact.core.payload.response.JwtResponse;
import com.fintransact.core.payload.response.MessageResponse;
import com.fintransact.core.payload.response.TwoFactorSetupResponse;
import com.fintransact.core.repository.AccountRepository;

import com.fintransact.core.exception.UserAlreadyExistsException;
import com.fintransact.core.repository.UserRepository;
import com.fintransact.core.security.jwt.JwtUtils;
import com.fintransact.core.security.services.UserDetailsImpl;
import com.fintransact.core.service.TwoFactorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "Authentication and authorization endpoints including login, signup, and 2FA management")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        UserRepository userRepository;

        @Autowired
        AccountRepository accountRepository;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        TwoFactorService twoFactorService;

        @Operation(summary = "User login", description = "Authenticate user with email and password. If 2FA is enabled, a code must be provided.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "202", description = "2FA code required"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials or 2FA code")
        })
        @PostMapping("/signin")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                User user = userRepository.findById(userDetails.getId()).orElseThrow();

                if (user.isTwoFactorEnabled()) {
                        if (loginRequest.getCode() == null || loginRequest.getCode().isEmpty()) {
                                return ResponseEntity.accepted().body(new MessageResponse("2FA Required"));
                        }

                        if (!twoFactorService.isOtpValid(user.getTwoFactorSecret(), loginRequest.getCode())) {
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                                .body(new MessageResponse("Invalid 2FA Code"));
                        }
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());

                return ResponseEntity.ok(new JwtResponse(jwt,
                                userDetails.getId(),
                                userDetails.getEmail(),
                                user.getName(),
                                roles));
        }

        @PostMapping("/setup-2fa")
        public ResponseEntity<?> setupTwoFactorAuth(Authentication authentication) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                User user = userRepository.findById(userDetails.getId()).orElseThrow();

                String secret = twoFactorService.generateNewSecret();
                user.setTwoFactorSecret(secret);
                userRepository.save(user);

                String qrCodeUrl = twoFactorService.generateQrCodeImageUri(secret, user.getEmail());

                return ResponseEntity.ok(new TwoFactorSetupResponse(secret, qrCodeUrl));
        }

        @PostMapping("/verify-2fa")
        public ResponseEntity<?> verifyTwoFactorAuth(@RequestBody TwoFactorVerifyRequest request,
                        Authentication authentication) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                User user = userRepository.findById(userDetails.getId()).orElseThrow();

                if (twoFactorService.isOtpValid(user.getTwoFactorSecret(), request.getCode())) {
                        user.setTwoFactorEnabled(true);
                        userRepository.save(user);
                        return ResponseEntity.ok(new MessageResponse("2FA Enabled Successfully"));
                } else {
                        return ResponseEntity.badRequest().body(new MessageResponse("Invalid Code"));
                }
        }

        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
                // Check for duplicate email
                if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                        throw new UserAlreadyExistsException("Email is already in use");
                }

                // Check for duplicate CPF
                if (userRepository.existsByCpf(signUpRequest.getCpf())) {
                        throw new UserAlreadyExistsException("CPF is already registered");
                }

                // Create new user's account
                User user = User.builder()
                                .name(signUpRequest.getName())
                                .email(signUpRequest.getEmail())
                                .cpf(signUpRequest.getCpf())
                                .password(encoder.encode(signUpRequest.getPassword()))
                                .build();

                User savedUser = userRepository.save(user);

                // Create initial account with 0 balance
                Account account = Account.builder()
                                .user(savedUser)
                                .accountNumber(UUID.randomUUID().toString())
                                .balance(BigDecimal.ZERO)
                                .build();

                accountRepository.save(account);

                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
}
