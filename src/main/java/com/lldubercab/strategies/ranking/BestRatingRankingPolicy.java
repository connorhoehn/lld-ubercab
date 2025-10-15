package com.lldubercab.strategies.ranking;

import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class BestRatingRankingPolicy implements IRankingPolicy {

    public List<Cab> rank(Booking booking, List<Cab> cabs) {
        return cabs;
    }

    public IRankingType getCategory() {
        return IRankingType.BEST_RATING;
    }
    
}
