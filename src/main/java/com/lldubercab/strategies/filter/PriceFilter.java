package com.lldubercab.strategies.filter;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class PriceFilter implements IFilterStrategy {
    final private static int PRICE_PER_UNIT = 10;
    public boolean matches(Booking booking, Cab cab) {
        // calculate price
        double distance = booking.getRideRequest().getPickup().distance(booking.getRideRequest().getDropOff());
        double price = distance * PRICE_PER_UNIT;

        return price < booking.getRideRequest().getMaxPrice().getAmount();
    }
}
