package com.lldubercab.strategies.pricing;

import com.lldubercab.internal.Price;
import com.lldubercab.model.booking.Booking;

public class DefaultsPricingModifierStrategy implements IPricingCalculatorStrategy {
    final private static int PRICE_PER_UNIT = 10;

    public void update(Booking booking) {
        // calculate price
        double distance = booking.getRideRequest().getPickup().distance(booking.getRideRequest().getDropOff());
        double price = distance * PRICE_PER_UNIT;

        booking.setFareQuote(new Price(price));
    }
}
