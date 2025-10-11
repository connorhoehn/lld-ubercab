package com.lldubercab.internal.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Location {
    private Double lat;
    private Double lng;

    public Double distance(Location otherLocation) {
       return Math.abs(otherLocation.lat + lat) + Math.abs(otherLocation.lng + lng);
    }
}
