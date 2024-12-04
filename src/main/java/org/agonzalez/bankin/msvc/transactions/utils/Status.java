package org.agonzalez.bankin.msvc.transactions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    ANNULMENT("ANNULMENT"), APPROVED("SUCCESSFUL"), INVALID_TRANSACTION("INVALID_TRANSACTION");

    private String value;

}
