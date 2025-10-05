package com.lldubercab.model.cab;

import com.lldubercab.internal.Location.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Cab {
    private Integer id;
    private Boolean booked;
    private Location location;
}
