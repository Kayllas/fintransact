package com.fintransact.core.controller;

import com.fintransact.core.model.Account;
import com.fintransact.core.model.PixKey;
import com.fintransact.core.model.Transaction;
import com.fintransact.core.model.User;
import com.fintransact.core.payload.request.PixKeyRequest;
import com.fintransact.core.payload.request.PixTransferRequest;
import com.fintransact.core.payload.response.MessageResponse;
import com.fintransact.core.payload.response.PixKeyResponse;
import com.fintransact.core.repository.UserRepository;
import com.fintransact.core.security.services.UserDetailsImpl;
import com.fintransact.core.service.PixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Pix", description = "Pix instant payment system endpoints for key management and transfers")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pix")
public class PixController {

    @Autowired
    PixService pixService;

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Register Pix Key", description = "Register a new Pix key (EMAIL, CPF, or RANDOM) linked to your account")
    @PostMapping("/keys")
    public ResponseEntity<?> createKey(@Valid @RequestBody PixKeyRequest request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        try {
            PixKey pixKey = pixService.createKey(user, request.getKey(), request.getType());
            return ResponseEntity.ok(new PixKeyResponse(pixKey.getId(), pixKey.getKey(), pixKey.getType()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "List my Pix Keys", description = "Get all Pix keys registered to the authenticated user")
    @GetMapping("/keys")
    public ResponseEntity<List<PixKeyResponse>> getMyKeys(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        List<PixKey> keys = pixService.getKeysByUser(user);
        List<PixKeyResponse> response = keys.stream()
                .map(k -> new PixKeyResponse(k.getId(), k.getKey(), k.getType()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lookup Pix Key", description = "Find the owner of a Pix key (name is masked for privacy)")
    @GetMapping("/keys/{key}")
    public ResponseEntity<?> getKeyOwner(@PathVariable String key) {
        try {
            Account account = pixService.getAccountByKey(key);
            // Masking name for privacy simulation: "Kayque Castro" -> "K***** C*****"
            String maskedName = account.getUser().getName().replaceAll("(?<=.{1}).", "*");
            return ResponseEntity.ok(new MessageResponse("Key belongs to: " + maskedName));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Transfer via Pix", description = "Make an instant transfer using a Pix key")
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody PixTransferRequest request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        try {
            Transaction transaction = pixService.transfer(user, request.getTargetKey(), request.getAmount());
            return ResponseEntity
                    .ok(new MessageResponse("Transfer successful! Transaction ID: " + transaction.getReferenceId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
