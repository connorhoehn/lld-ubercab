package com.lldubercab.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.strategies.filter.CategoryFilter;
import com.lldubercab.strategies.filter.IFilterStrategy;
import com.lldubercab.strategies.filter.PriceFilter;

public class FilterService {
    final private List<IFilterStrategy> matchingStrategies = Arrays.asList(new PriceFilter(), new CategoryFilter());

    public List<Cab> rank(List<Cab> bookings) {
        return bookings;
    }

    public List<Cab> filter(Booking booking, List<Cab> cabs) {
        List<Cab> matchedCabs = new ArrayList<Cab>();

        for (Cab cab: cabs) {
            boolean suitableMatch = true;

            for (IFilterStrategy strategy: matchingStrategies) {
                if(!strategy.matches(booking, cab)) {
                    suitableMatch = false;
                    break;
                }
            }

            if (suitableMatch) {
                matchedCabs.add(cab);
            }
        }

        return matchedCabs;
    }

    public List<Cab> match(Booking booking, List<Cab> cabs) {

        // Inventory of items - get one item from that list.

        // 2 steps - matching
        // filtering
        // ranking.
        
        cabs = this.filter(booking, cabs);

        
        cabs = this.rank(cabs);

        // sort by proximity to user

        return cabs;
    }

}
