package org.agonzalez.bankin.msvc.transactions.controller;

import lombok.AllArgsConstructor;
import org.agonzalez.bankin.msvc.transactions.Service.TransactionService;
import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Request;
import org.agonzalez.bankin.msvc.transactions.models.entity.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(path = "/purchase")
    public ResponseEntity<Transaction> purchaseTransaction(@RequestBody Request request) {
        ResponseEntity<Transaction> response = null;
        try {
            response = this.transactionService.purchaseTransaction(request);
        } catch (Exception e) {
            return (new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return response;
    }

    @GetMapping(path = "/{transactionId}")
    public ResponseEntity<Transaction> queryTransaction(@PathVariable(name = "transactionId") int transaction) throws LocalNotFoundException {
        try {
            return (this.transactionService.inquiryTransaction(transaction));
        } catch (Exception e) {
            throw new LocalNotFoundException("TransactionId not available " + e.getMessage());
        }
    }

    @PostMapping(path = "/anulation")
    public ResponseEntity<Transaction> annulmentTransaction(@RequestBody Request request) throws LocalNotFoundException {
        try {
            return this.transactionService.annulmentTransaction(request);
        } catch (Exception e) {
            throw new LocalNotFoundException("Request Invalid: " + e.getMessage());
        }
    }
}
