package com.lldubercab.strategies;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public interface IMatchingStrategy {
    public boolean matches(Booking booking, Cab cab);
}
