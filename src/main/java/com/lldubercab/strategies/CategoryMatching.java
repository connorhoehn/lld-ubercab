package com.lldubercab.strategies;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class CategoryMatching implements IMatchingStrategy {
    public boolean matches(Booking booking, Cab cab) {
        return booking.getPreferredCabCategory() == cab.getCategory();
    }
}
