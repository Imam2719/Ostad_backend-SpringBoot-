package com.example.Ostad_SpringBoot.controller;

import com.example.Ostad_SpringBoot.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    // In-memory data store
    private List<Transaction> transactions = new ArrayList<>();

    // 1. GET /api/transactions - Get All Transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestParam(required = false) String type) {

        // Advanced: Filter by type if provided
        if (type != null && !type.isEmpty()) {
            List<Transaction> filtered = transactions.stream()
                    .filter(t -> t.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filtered);
        }

        return ResponseEntity.ok(transactions);
    }

    // 2. GET /api/transactions/{id} - Get a specific Transaction
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                return ResponseEntity.ok(transaction);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 3. PUT /api/transactions/{id} - Update a specific Transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable String id,
            @RequestBody Transaction updatedTransaction) {

        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                // Update the transaction properties
                transaction.setType(updatedTransaction.getType());
                transaction.setAmount(updatedTransaction.getAmount());
                transaction.setDescription(updatedTransaction.getDescription());
                transaction.setDate(updatedTransaction.getDate());

                return ResponseEntity.ok(transaction);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 4. DELETE /api/transactions/{id} - Delete a specific Transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        boolean removed = transactions.removeIf(t -> t.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 5. POST /api/transactions - Add a new Transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        // Generate ID if not provided
        if (transaction.getId() == null || transaction.getId().isEmpty()) {
            transaction.setId(String.valueOf(System.currentTimeMillis()));
        }

        transactions.add(transaction);
        return ResponseEntity.status(201).body(transaction);
    }
}