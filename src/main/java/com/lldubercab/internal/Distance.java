package com.lldubercab.internal;

import lombok.Data;

@Data
public class Distance {
    private String unit;
    private Double value;
    
    public Distance(Double value) {
        this.unit = "KM";
        this.value = value;
    }
}
