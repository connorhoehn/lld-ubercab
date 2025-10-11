package com.lldubercab.model;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.passenger.Passenger;

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
    private Integer maxDistanceAway;
    private CabCategory category;
    private Double maxPrice;
}
