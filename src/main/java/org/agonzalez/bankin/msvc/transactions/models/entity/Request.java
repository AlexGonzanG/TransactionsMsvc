package org.agonzalez.bankin.msvc.transactions.models.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Request {


    @NotEmpty
    private String cardId;
    private String currencyCode;
    private float price;

    @NotEmpty
    private String transactionId;
}
