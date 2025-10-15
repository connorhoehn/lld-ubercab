package com.lldubercab.strategies.pricing;

import java.util.Calendar;

import com.lldubercab.model.booking.Booking;

public class DiscountPricingStrategy implements IPricingCalculatorStrategy {
    public void update(Booking booking) {

        Calendar cal = Calendar.getInstance();
        
        //if thursday, discount
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            booking.getFareQuote().setAmount(booking.getFareQuote().getAmount() * .7);
        }
    }
}
