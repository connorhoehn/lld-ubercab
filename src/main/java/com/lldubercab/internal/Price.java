package com.lldubercab.internal;

import lombok.Data;

@Data
public class Price {
    private String currency;
    private Double amount;
    public Price(Double amount) {
        this.amount = amount;
    }

    public int compare(Price p2) {
        return this.getAmount() > p2.getAmount() ? 1 : 0;
    }
}
