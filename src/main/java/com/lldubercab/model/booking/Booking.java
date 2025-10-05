package com.lldubercab.model.booking;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.passenger.Passenger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class Booking {
    private int id;
    private Passenger passenger;
    private Cab cab;
    private BookingStatus status;
    private Location pickupLocation;
    private Location deliveryLocation;
}
