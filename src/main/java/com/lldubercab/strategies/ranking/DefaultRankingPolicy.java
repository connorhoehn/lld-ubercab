package com.lldubercab.strategies.ranking;

import java.util.LinkedList;
import java.util.List;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.cab.CabNotificationResponse;
import com.lldubercab.services.NotificationManager;

import lombok.SneakyThrows;

public class DefaultRankingPolicy implements IRankingPolicy {

    NotificationManager notificationManager = NotificationManager.getInstance();

    public List<Cab> rank(Booking booking, List<Cab> cabs) {

        // 3.1) Notify cabs about the booking
        notificationManager.notifyMatchedCabs(booking, cabs);

        // 3.2) Wait until all cabs respond to the notification with a limit of 5 seconds
        List<CabNotificationResponse> driverAcceptedResponses = this.waitForResponses(booking, Long.getLong("100"));

        // 3.4) Find cabs that responded
        List<Cab> acceptedCabs = new LinkedList<>();
        for (CabNotificationResponse response: driverAcceptedResponses) {
            acceptedCabs.add(cabs.get(response.getCabId()));
        }

        // 3.5) Return null if no matches 
        if (acceptedCabs.isEmpty()) {
            return null;
        }            

        return acceptedCabs;
    }

    @SneakyThrows
    public List<CabNotificationResponse> waitForResponses(Booking booking, long duration) {
        
        long startTime = System.currentTimeMillis();

        // Wait 2 seconds for all cab drivers to respond
        while(System.currentTimeMillis() - startTime < 2000) {
            Thread.sleep(2000);
        }

        return booking.getDriverResponses();
    }


    public IRankingType getCategory() {
        return IRankingType.DEFAULT;
    }
    
}
