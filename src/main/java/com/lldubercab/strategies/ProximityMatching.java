package com.lldubercab.strategies;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class ProximityMatching implements IMatchingStrategy {
    public boolean matches(Booking booking, Cab cab) {
        return true;
    }
}
