package com.lldubercab.strategies.filter;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public interface IFilterStrategy {
    public boolean matches(Booking booking, Cab cab);
}
