package org.agonzalez.bankin.msvc.transactions.Service;

import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Request;
import org.agonzalez.bankin.msvc.transactions.models.entity.Transaction;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<Transaction> annulmentTransaction(Request request) throws LocalNotFoundException;

    ResponseEntity<Transaction> inquiryTransaction(int transactionId);

    ResponseEntity<Transaction> purchaseTransaction(Request transaction);


}
