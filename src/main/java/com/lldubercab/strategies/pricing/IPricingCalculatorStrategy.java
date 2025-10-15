package com.lldubercab.strategies.pricing;

import com.lldubercab.model.booking.Booking;

public interface IPricingCalculatorStrategy {
    public void update(Booking booking);
}
