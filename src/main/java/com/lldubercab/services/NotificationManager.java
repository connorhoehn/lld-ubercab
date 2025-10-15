package com.lldubercab.services;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.cab.ICabObserverer;

public class NotificationManager {
    
    private final List<ICabObserverer> subscribedCabs = Arrays.asList();

    private static NotificationManager instance = null;

    public static synchronized NotificationManager getInstance() {

        if (instance == null) {
            instance = new NotificationManager();
        }

        return instance;
    }

    public void subscribeAvailableCab(ICabObserverer cab) {
        subscribedCabs.add(cab);
    }

    public void subscribeAvailableCabs(List<Cab> cabs) {
        for (ICabObserverer cab: cabs) {
            subscribeAvailableCab(cab);
        }
    }

    public void unsubscribeAvailableCab(ICabObserverer cab) {
        subscribedCabs.remove(cab);
    }

    public void notifyMatchedCabs(Booking booking, List<Cab> matchedCabs) {
        Set<Integer> cabIds = matchedCabs.stream().map(cab -> cab.getId()).collect(Collectors.toSet());

        for (ICabObserverer cab: subscribedCabs) {
            Cab cabInstance = (Cab) cab;
            if(cabIds.contains(cabInstance.getId())) {

                // simulate a fanout across a network to available cabs 
                new Thread(() -> {
                    cab.respondToRideRequest(booking);
                });
            }
        }
    }
}
