package org.agonzalez.bankin.msvc.transactions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataResponse {

    EXPIRED_CARD("EXPIRED DATE CARD"),
    INACTIVE_CARD("INACTIVE CARD"),
    INSUFFICIENT_FUNDS("INSUFFICIENT FUNDS"),
    INVALID_CURRENCY_TYPE("INVALID CURRENCY TYPE"),
    SUCCESSFUL_TRANSACTION("SUCCESSFUL TRANSACTION"),
    TRANSACTION_NOT_FOUND("ORIGINAL TRANSACTION NOT FOUND"),
    EXPIRED_TIME("COULD NOT BE CANCELED FOR MORE THAN 24 HRS");

    private String value;

}
