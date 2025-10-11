package com.lldubercab.services;

import java.util.Arrays;
import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.strategies.CategoryMatching;
import com.lldubercab.strategies.IMatchingStrategy;
import com.lldubercab.strategies.PriceMatching;

public class MatcherService {
    final private List<IMatchingStrategy> matchingStrategies = Arrays.asList(new PriceMatching(), new CategoryMatching());

    public Cab match(Booking booking, List<Cab> cabs) {

        // Inventory of items - get one item from that list.

        // 2 steps - matching
        // filtering
        // ranking.


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

        


        // sort by proximity to user

        return null;
    }

}
