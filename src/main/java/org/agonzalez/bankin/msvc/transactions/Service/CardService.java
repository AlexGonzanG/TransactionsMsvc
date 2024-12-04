package org.agonzalez.bankin.msvc.transactions.Service;

import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<Card> createCard(int productId);

    ResponseEntity<Card> activateCard(String cardId) throws LocalNotFoundException;

    ResponseEntity<Card> balanceInquiry(String cardId) throws LocalNotFoundException;

    ResponseEntity<Card> blockCard(String cardId) throws LocalNotFoundException;

    ResponseEntity<Card> rechargeBalance(String cardId, float balance) throws LocalNotFoundException;


}
