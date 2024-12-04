package org.agonzalez.bankin.msvc.transactions.controller;

import org.agonzalez.bankin.msvc.transactions.Service.CardService;
import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.agonzalez.bankin.msvc.transactions.repositories.CardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
class CardControllerTest {

    @InjectMocks
    private CardController cardController;

    @Autowired
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() throws Exception {
        this.cardController = new CardController(this.cardService);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.cardController = null;
    }

    @Test
    void createCardSuccessTest(){
        assertEquals(HttpStatus.OK, this.cardController.createCard(123456).getStatusCode());
    }

    @Test
    void activeCardSuccessTest() throws LocalNotFoundException {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId("1234561333084264");
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        Mockito.when(this.cardRepository.findByCardId("1234561333084264")).thenReturn(card);
        assertEquals(HttpStatus.OK, this.cardController.activeCard(card.get()).getStatusCode());
    }

    @Test
    void activeCardErrorTest() {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId(null);
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        LocalNotFoundException expected = new LocalNotFoundException("CardId null not available: CardId not available ");
        LocalNotFoundException exception = assertThrows(LocalNotFoundException.class, () -> {
            this.cardController.activeCard(card.get()).getStatusCode();
        });
        assertEquals(exception.getClass(), expected.getClass());
    }



    @Test
    void blockCardSuccessTest() throws LocalNotFoundException {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId("1234561333084264");
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        Mockito.when(this.cardRepository.findByCardId("1234561333084264")).thenReturn(card);
        assertEquals(HttpStatus.OK, this.cardController.blockCard(card.get().getCardId()).getStatusCode());
    }

    @Test
    void blockCardErrorTest() {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId(null);
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        LocalNotFoundException expected = new LocalNotFoundException("CardId null not available: CardId not available ");
        LocalNotFoundException exception = assertThrows(LocalNotFoundException.class, () -> {
            this.cardController.blockCard(card.get().getCardId()).getStatusCode();
        });
        assertEquals(exception.getClass(), expected.getClass());
    }

    @Test
    void rechargeBalanceSuccessTest() throws LocalNotFoundException {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId("1234561333084264");
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        card.get().setBalance(1000);
        Card card2 = card.get();
        Mockito.when(this.cardRepository.findByCardId("1234561333084264")).thenReturn(card);
        Mockito.when(this.cardRepository.save(card.get())).thenReturn(card2);
        assertEquals(HttpStatus.OK, this.cardController.rechargeBalance(card.get()).getStatusCode());
    }

    @Test
    void rechargeBalanceErrorTest() {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId(null);
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        LocalNotFoundException expected = new LocalNotFoundException("CardId null not available: CardId not available ");
        LocalNotFoundException exception = assertThrows(LocalNotFoundException.class, () -> {
            this.cardController.rechargeBalance(card.get()).getStatusCode();
        });
        assertEquals(exception.getClass(), expected.getClass());
    }


    @Test
    void balanceInquirySuccessTest() throws LocalNotFoundException {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId("1234561333084264");
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        Mockito.when(this.cardRepository.findByCardId("1234561333084264")).thenReturn(card);
        assertEquals(HttpStatus.OK, this.cardController.balanceInquiry(card.get().getCardId()).getStatusCode());
    }

    @Test
    void balanceInquiryErrorTest() {
        Optional<Card> card = Optional.of(new Card());
        card.get().setCardId(null);
        card.get().setActive(false);
        card.get().setCurrencyCode("840");
        LocalNotFoundException expected = new LocalNotFoundException("CardId null not available: CardId not available ");
        LocalNotFoundException exception = assertThrows(LocalNotFoundException.class, () -> {
            this.cardController.balanceInquiry(card.get().getCardId()).getStatusCode();
        });
        assertEquals(exception.getClass(), expected.getClass());
    }


}
