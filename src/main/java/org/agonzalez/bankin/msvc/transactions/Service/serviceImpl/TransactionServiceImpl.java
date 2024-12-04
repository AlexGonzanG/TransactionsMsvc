package org.agonzalez.bankin.msvc.transactions.Service.serviceImpl;

import lombok.AllArgsConstructor;
import org.agonzalez.bankin.msvc.transactions.Service.TransactionService;
import org.agonzalez.bankin.msvc.transactions.exception.ResourceNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.agonzalez.bankin.msvc.transactions.models.entity.Request;
import org.agonzalez.bankin.msvc.transactions.models.entity.Transaction;
import org.agonzalez.bankin.msvc.transactions.repositories.CardRepository;
import org.agonzalez.bankin.msvc.transactions.repositories.TransactionRepository;
import org.agonzalez.bankin.msvc.transactions.utils.DataResponse;
import org.agonzalez.bankin.msvc.transactions.utils.Status;
import org.agonzalez.bankin.msvc.transactions.utils.TranType;
import org.agonzalez.bankin.msvc.transactions.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Override
    public ResponseEntity<Transaction> annulmentTransaction(Request request) {
        Card card = this.cardRepository.findByCardId(request.getCardId()).orElseThrow(() -> {
            throw (new ResourceNotFoundException("Card", "CardId", request.getCardId()));
        });

        Transaction oriTransaction = this.transactionRepository.findByTransactionId(request.getTransactionId()).orElseThrow(() -> {
            throw (new ResourceNotFoundException("Transaction", "transactionId", request.getTransactionId()));
        });

        Transaction transaction = this.getTransactionFromRequest(request);

        if (!this.isTransactionFromCard(card, request.getTransactionId())) {
            transaction.setState(Status.INVALID_TRANSACTION.getValue());
            transaction.setResponseData(DataResponse.TRANSACTION_NOT_FOUND.getValue());
            return (new ResponseEntity<>(transaction, HttpStatus.BAD_REQUEST));
        }
        transaction.setTransactionType(TranType.ANULATION.getValue());
        transaction.setCurrencyCode(oriTransaction.getCurrencyCode());
        transaction.setAmount(oriTransaction.getAmount());

        if (!oriTransaction.getDateTime().after(Utils.dateFormater(0, -24))) {
            transaction.setState(Status.INVALID_TRANSACTION.getValue());
            transaction.setResponseData(DataResponse.EXPIRED_TIME.getValue());
            this.createNewTransaction(card, transaction);
            return (new ResponseEntity<>(this.saveTransactionInBd(transaction, card), HttpStatus.OK));
        }

        oriTransaction.setState(Status.ANNULMENT.getValue());
        oriTransaction.setAnnulledBy(transaction.getTransactionId());
        this.transactionRepository.save(oriTransaction);
        transaction.setState(Status.APPROVED.getValue());
        transaction.setResponseData(DataResponse.SUCCESSFUL_TRANSACTION.getValue());
        card.setBalance(card.getBalance() + oriTransaction.getAmount());
        this.createNewTransaction(card, transaction);
        return (new ResponseEntity<>(this.saveTransactionInBd(transaction, card), HttpStatus.OK));

    }

    @Override
    public ResponseEntity<Transaction> inquiryTransaction(int transactionId) {
        return (new ResponseEntity<>(this.transactionRepository.findByTransactionId(String.valueOf(transactionId)).orElseThrow(() -> {
            throw (new ResourceNotFoundException("Transaction", "TransactionId", String.valueOf(transactionId)));
        }), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Transaction> purchaseTransaction(Request request) {
        Card card = this.cardRepository.findByCardId(request.getCardId()).orElseThrow(() -> {
            throw (new ResourceNotFoundException("Card", "CardId", request.getCardId()));
        });
        Transaction transaction = this.getTransactionFromRequest(request);
        transaction.setTransactionType(TranType.PURCHASE.getValue());
        if (!transaction.getCurrencyCode().equals(card.getCurrencyCode())) {
            return (new ResponseEntity<>(this.validateCurrencyCode(card, transaction), HttpStatus.OK));
        }
        if (card.getBalance() < transaction.getAmount()) {
            return (new ResponseEntity<>(this.validateFounds(card, transaction), HttpStatus.OK));
        }
        if (transaction.getDateTime().after(card.getExpireDate())) {
            return (new ResponseEntity<>(this.validateExpireDate(card, transaction), HttpStatus.OK));
        }
        if (!card.isActive()) {
            return (new ResponseEntity<>(this.validateIsActiveCard(card, transaction), HttpStatus.OK));
        }

        return (new ResponseEntity<>(this.createNewTransaction(card, transaction), HttpStatus.OK));

    }


    private Transaction getTransactionFromRequest(Request request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getPrice());
        transaction.setCurrencyCode(request.getCurrencyCode());
        transaction.setDateTime(Utils.dateFormater());
        transaction.setTransactionId(Utils.generateRandomNum(6));
        transaction.setOriginalTransactionId(request.getTransactionId());
        return (transaction);
    }

    private Transaction saveTransactionInBd(Transaction transaction, Card card) {
        Transaction transactionSaved = this.transactionRepository.save(transaction);
        this.cardRepository.save(card);
        return (transactionSaved);
    }

    private Transaction validateCurrencyCode(Card card, Transaction transaction) {
        transaction.setState(Status.INVALID_TRANSACTION.getValue());
        transaction.setResponseData(DataResponse.INVALID_CURRENCY_TYPE.getValue());
        this.transactionOnCardRegistry(card, transaction);
        return this.saveTransactionInBd(transaction, card);
    }

    private Transaction validateFounds(Card card, Transaction transaction) {
        transaction.setState(Status.INVALID_TRANSACTION.getValue());
        transaction.setResponseData(DataResponse.INSUFFICIENT_FUNDS.getValue());
        this.transactionOnCardRegistry(card, transaction);
        return this.saveTransactionInBd(transaction, card);
    }

    private Transaction validateExpireDate(Card card, Transaction transaction) {
        transaction.setState(Status.INVALID_TRANSACTION.getValue());
        transaction.setResponseData(DataResponse.EXPIRED_CARD.getValue());
        this.transactionOnCardRegistry(card, transaction);
        return this.saveTransactionInBd(transaction, card);
    }

    private Transaction validateIsActiveCard(Card card, Transaction transaction) {
        transaction.setState(Status.INVALID_TRANSACTION.getValue());
        transaction.setResponseData(DataResponse.INACTIVE_CARD.getValue());
        this.transactionOnCardRegistry(card, transaction);
        return this.saveTransactionInBd(transaction, card);
    }

    private Transaction createNewTransaction(Card card, Transaction transaction) {
        card.setBalance(card.getBalance() - transaction.getAmount());
        transaction.setState(Status.APPROVED.getValue());
        transaction.setResponseData(DataResponse.SUCCESSFUL_TRANSACTION.getValue());
        this.transactionOnCardRegistry(card, transaction);
        return this.saveTransactionInBd(transaction, card);
    }

    private void transactionOnCardRegistry(Card card, Transaction transaction) {
        int transactionNumber = card.getTransactions() == null ? 0 : card.getTransactions().length;
        Transaction[] registryTransactions = new Transaction[transactionNumber + 1];
        System.arraycopy(card.getTransactions(), 0, registryTransactions, 0, transactionNumber);
        registryTransactions[registryTransactions.length - 1] = transaction;
        card.setTransactions(registryTransactions);
    }

    private boolean isTransactionFromCard(Card card, String transactionId) {
        for (Transaction transaction : card.getTransactions()) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return (true);
            }
        }
        return (false);
    }

}
