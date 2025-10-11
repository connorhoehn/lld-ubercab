package com.lldubercab.strategies;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class PriceMatching implements IMatchingStrategy {
    final private static int PRICE_PER_UNIT = 10;
    public boolean matches(Booking booking, Cab cab) {
        // calculate price
        double distance = booking.getPickupLocation().distance(booking.getDeliveryLocation());
        double price = distance * PRICE_PER_UNIT;

        return price < booking.getMaxPrice();
    }
}
