package org.agonzalez.bankin.msvc.transactions.repositories;

import org.agonzalez.bankin.msvc.transactions.models.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
}
