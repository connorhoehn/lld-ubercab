package com.lldubercab.services;

import java.util.Arrays;
import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.strategies.IMatchingStrategy;
import com.lldubercab.strategies.ProximityMatching;

public class MatcherService {
    final private List<IMatchingStrategy> matchingStrategies = Arrays.asList(new ProximityMatching());

    public Cab match(Booking booking, List<Cab> cabs) {
        for (Cab cab: cabs) {
            boolean suitableMatch = true;

            for (IMatchingStrategy strategy: matchingStrategies) {
                if(!strategy.matches(booking, cab)) {
                    suitableMatch = false;
                    break;
                }
            }

            if (suitableMatch) {
                return cab;
            }
        }

        return null;
    }
}
