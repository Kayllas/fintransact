package com.fintransact.core.controller;

import com.fintransact.core.model.Transaction;
import com.fintransact.core.payload.response.MessageResponse;
import com.fintransact.core.security.services.UserDetailsImpl;
import com.fintransact.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> payload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String targetAccountNumber = (String) payload.get("targetAccountNumber");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        try {
            transactionService.transfer(userDetails.getId(), targetAccountNumber, amount);
            return ResponseEntity.ok(new MessageResponse("Transfer successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(transactionService.getHistory(userDetails.getId()));
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(transactionService.getAccount(userDetails.getId()));
    }
}
