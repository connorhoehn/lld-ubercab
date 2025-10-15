package com.lldubercab.strategies.filter;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class CategoryFilter implements IFilterStrategy {
    public boolean matches(Booking booking, Cab cab) {
        return booking.getRideRequest().getCategory() == cab.getCategory();
    }
}
