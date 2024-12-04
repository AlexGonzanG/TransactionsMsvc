package org.agonzalez.bankin.msvc.transactions.controller;

import lombok.AllArgsConstructor;
import org.agonzalez.bankin.msvc.transactions.Service.CardService;
import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;


    @GetMapping(path = "/{productId}/number")
    public ResponseEntity<Card> createCard(@PathVariable(name = "productId") int bin) {
        return (this.cardService.createCard(bin));
    }

    @PostMapping(path = "/enroll")
    public ResponseEntity<Card> activeCard(@RequestBody Card card) throws LocalNotFoundException {
        ResponseEntity<Card> response = null;
        try {
            response = this.cardService.activateCard(card.getCardId());
        } catch (Exception e) {
            throw new LocalNotFoundException("CardId " + card.getCardId() + " not available: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping(path = "/{cardId}")
    public ResponseEntity<Card> blockCard(@PathVariable(name = "cardId") String cardId) throws LocalNotFoundException {
        ResponseEntity<Card> response = null;
        try {
            response = this.cardService.blockCard(cardId);
        } catch (Exception e) {
            throw new LocalNotFoundException("CardId not available " + e.getMessage());
        }
        return response;
    }

    @PostMapping(path = "/balance")
    public ResponseEntity<Card> rechargeBalance(@RequestBody Card card) throws LocalNotFoundException {
        ResponseEntity<Card> response = null;
        try {
            response = this.cardService.rechargeBalance(card.getCardId(), card.getBalance());
        } catch (Exception e) {
            throw new LocalNotFoundException("CardId not available " + e.getMessage());
        }
        return response;
    }

    @GetMapping(path = "/balance/{cardId}")
    public ResponseEntity<Card> balanceInquiry(@PathVariable(name = "cardId") String cardId) throws LocalNotFoundException {
        ResponseEntity<Card> response = null;
        try {
            response = this.cardService.balanceInquiry(cardId);
        } catch (Exception e) {
            throw new LocalNotFoundException("CardId not available " + e.getMessage());
        }
        return response;
    }
}
