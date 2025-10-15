package com.lldubercab.strategies.ranking;

import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public interface IRankingPolicy {
    public List<Cab> rank(Booking booking, List<Cab> cabs);
    public IRankingType getCategory();
}
