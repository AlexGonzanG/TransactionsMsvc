package org.agonzalez.bankin.msvc.transactions.models.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cards")
@Entity
@Data
public class Card {

    @Id
    @Column(length = 16)
    private String cardId;

    private String cardholderName;

    @Column(nullable = false)
    private Date expireDate;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private float balance;
    private boolean isActive;

    @OrderColumn
    @OneToMany
    private Transaction[] transactions;

}
