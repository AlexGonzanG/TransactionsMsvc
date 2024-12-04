package org.agonzalez.bankin.msvc.transactions.controller;

import org.agonzalez.bankin.msvc.transactions.Service.TransactionService;
import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.agonzalez.bankin.msvc.transactions.models.entity.Request;
import org.agonzalez.bankin.msvc.transactions.models.entity.Transaction;
import org.agonzalez.bankin.msvc.transactions.repositories.CardRepository;
import org.agonzalez.bankin.msvc.transactions.repositories.TransactionRepository;
import org.agonzalez.bankin.msvc.transactions.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Autowired
    private TransactionService transactionService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() throws Exception {
        this.transactionController = new TransactionController(this.transactionService);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.transactionController = null;
    }

    @Test
    void purchaseTransactionSuccessTest(){
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId("1234561333084264");
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        card.get().setBalance(1000);
        Card card2 = card.get();
        Request request = new Request();
        request.setTransactionId("123456");
        request.setCardId("1234561333084264");
        request.setCurrencyCode("840");
        request.setPrice(1000);
        Transaction transaction = new Transaction();
        transaction.setResponseData("SUCCESSFUL TRANSACTION");
        transaction.setTransactionType("Compra");
        transaction.setOriginalTransactionId("123456");
        transaction.setAmount(1000);
        transaction.setState("1");
        transaction.setTransactionId("123456");
        transaction.setCurrencyCode("840");
        transaction.setDateTime(Utils.dateFormater());

        Mockito.when(this.cardRepository.findByCardId("1234561333084264")).thenReturn(card);
        Mockito.when(this.transactionRepository.save(transaction)).thenReturn(transaction);
        Mockito.when(this.cardRepository.save(card.get())).thenReturn(card2);
        assertEquals(HttpStatus.OK, this.transactionController.purchaseTransaction(request).getStatusCode());
    }

    @Test
    void purchaseTransactionErrorTest(){
        Request request = new Request();
        request.setTransactionId("1234567890");
        request.setCardId("1234567890");
        request.setCurrencyCode("840");
        request.setPrice(1000);
        assertEquals(HttpStatus.NOT_FOUND, this.transactionController.purchaseTransaction(request).getStatusCode());
    }

}
