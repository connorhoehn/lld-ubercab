package com.lldubercab.model.passenger;

import com.lldubercab.model.RideRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class Passenger {
    private String name;
    private List<RideRequest> rideRequests;

    public Passenger(String name) {
        this.name = name;
    }
}
