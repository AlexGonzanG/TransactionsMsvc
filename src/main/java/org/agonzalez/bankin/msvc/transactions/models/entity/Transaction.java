package org.agonzalez.bankin.msvc.transactions.models.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity
@Data
public class Transaction {
    @Column(nullable = false)
    private float amount;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false)
    private Date dateTime;
    @Id
    @Column(length = 6)
    private String transactionId;
    @Column(nullable = false)
    private String responseData;
    private String state;
    @Column(length = 6)
    private String originalTransactionId;
    private String transactionType;
    @Column(length = 6)
    private String annulledBy;

}
