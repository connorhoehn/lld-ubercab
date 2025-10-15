package com.lldubercab.model.cab;

import com.lldubercab.model.booking.Booking;

public interface ICabObserverer {
    void respondToRideRequest(Booking booking);
}
