package org.agonzalez.bankin.msvc.transactions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TranType {

    PURCHASE("Compra"), ANULATION("Anulación");

    private String value;
}
