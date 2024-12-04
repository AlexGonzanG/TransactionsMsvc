package org.agonzalez.bankin.msvc.transactions.Service.serviceImpl;

import lombok.AllArgsConstructor;
import org.agonzalez.bankin.msvc.transactions.Service.CardService;
import org.agonzalez.bankin.msvc.transactions.constants.Constants;
import org.agonzalez.bankin.msvc.transactions.exception.LocalNotFoundException;
import org.agonzalez.bankin.msvc.transactions.models.entity.Card;
import org.agonzalez.bankin.msvc.transactions.repositories.CardRepository;
import org.agonzalez.bankin.msvc.transactions.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public ResponseEntity<Card> createCard(int bin) {
        if (String.valueOf(bin).length() != 6) {
            return (new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return (new ResponseEntity<>(this.saveCard(this.createNewCard(String.valueOf(bin))), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Card> activateCard(String cardId) throws LocalNotFoundException {
        return (new ResponseEntity<>(this.updateStatusCard(cardId, true), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Card> balanceInquiry(String cardId) throws LocalNotFoundException {
        return (new ResponseEntity<>(this.findCard(cardId), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Card> blockCard(String cardId) throws LocalNotFoundException {
        return (new ResponseEntity<>(this.updateStatusCard(cardId, false), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Card> rechargeBalance(String cardId, float balance) throws LocalNotFoundException {
        Card card = this.findCard(cardId);
        card.setBalance(card.getBalance() + balance);
        return (new ResponseEntity<>(this.saveCard(card), HttpStatus.OK));
    }

    private Card saveCard(Card card) {
        Card cardSave = this.cardRepository.save(card);
        return (cardSave);
    }

    private Card createNewCard(String issuerId) {
        Card card = new Card();
        card.setCardId(issuerId + Utils.generateRandomNum(10));
        card.setCurrencyCode(Constants.CURRENCY_CODE);
        card.setCardholderName(Constants.CUSTOMER_NAME);
        card.setExpireDate(Utils.dateFormater(3, 0));
        return (card);
    }

    private Card updateStatusCard(String cardId, boolean isActive) throws LocalNotFoundException {
        Card foundCard = this.findCard(cardId);
        foundCard.setActive(isActive);
        return (this.saveCard(foundCard));
    }

    private Card findCard(String cardId) throws LocalNotFoundException {
        Optional<Card> foundCard = this.cardRepository.findByCardId(cardId);
        if (!foundCard.isPresent()) {
            throw new LocalNotFoundException("CardId not available ");
        }
        return (foundCard.get());
    }

}
