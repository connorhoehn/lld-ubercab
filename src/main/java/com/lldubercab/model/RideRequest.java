package com.lldubercab.model;


import com.lldubercab.internal.Distance;
import com.lldubercab.internal.Location;
import com.lldubercab.internal.Price;
import com.lldubercab.model.passenger.Passenger;
import com.lldubercab.strategies.ranking.IRankingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RideRequest {
    private Passenger passenger;
    private Location pickup;
    private Location dropOff;
    private Long requestTime;
    private CabCategory category;
    private Price maxPrice;
    private Distance maxDistanceAway;
    
    private IRankingType rankingStrategy;
    private int requestAtempts; // only 3 attempts

    public void incrementAttempts() {
        this.requestAtempts++;
    }
}
