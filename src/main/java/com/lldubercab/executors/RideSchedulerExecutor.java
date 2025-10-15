package com.lldubercab.executors;


import com.lldubercab.model.ConcurrentRideStore;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.services.CabManager;

import lombok.SneakyThrows;


public class RideSchedulerExecutor implements Runnable {

    final static int MAX_RETRIES = 3;

    CabManager cabManager;
    ConcurrentRideStore activeRideQueue;
    ConcurrentRideStore pendingRideQueue;

    public RideSchedulerExecutor(ConcurrentRideStore pq, ConcurrentRideStore ac, CabManager cManager) {
        this.pendingRideQueue = pq;
        this.activeRideQueue = ac;
        this.cabManager = cManager;
    }

    @SneakyThrows
    public void retryBooking(Booking booking) {
        System.out.println("There was an issue booking a cab: " + booking.getId());
        
        booking.getRideRequest().incrementAttempts();

        double attempts = booking.getRideRequest().getRequestAtempts();

        // provide feedback to the user that the ride has failed
        if (attempts >= MAX_RETRIES) {
            cancelBooking(booking);
        }

        long timeToWait = (long) Math.pow(10.0, attempts);
        
        // simulate waiting for the booking to be put back into the queue and consumed
        Thread.sleep(timeToWait);

        synchronized (pendingRideQueue) {
            pendingRideQueue.insert(booking);
        }
    }

    public void cancelBooking(Booking booking) {
        booking.setStatus(BookingStatus.NO_CABS_AVAILABLE);
        System.out.println(Thread.currentThread().getName() + " - Assigning Passenger: " + booking.getPassenger().getName() + " with cab:"  + booking.getCab());
    }


    public void activateBooking(Booking booking) {
        booking.setStatus(BookingStatus.ACTIVE);
        activeRideQueue.insert(booking);
        System.out.println(Thread.currentThread().getName() + " - Assigning Passenger: " + booking.getPassenger().getName() + " with cab:"  + booking.getCab());
    }

    public void attemptToMatchCab(Booking booking) {
        booking.setStatus(BookingStatus.ALLOCATING_CAB);

        Cab matchedCab = cabManager.matchAndRank(booking);

        if (matchedCab == null) {
            retryBooking(booking);
            return;
        }

        System.out.println(Thread.currentThread().getId() + ": was able to match a cab. " + booking.getId());

        final boolean sucessfullyAssignedCab = cabManager.assign(matchedCab);

        if (!sucessfullyAssignedCab) {
            retryBooking(booking);
            return;
        }

        System.out.println(Thread.currentThread().getId() + ": was able to assign a cab. " + booking.getId());

        activateBooking(booking);
    }

    public void run() {
        while (true) {
            try {
                Booking rideRequest = pendingRideQueue.removeOrWait();

                // created, or user could have updated their ride request parameters for the booking
                if (rideRequest.getStatus() == BookingStatus.CREATED || rideRequest.getStatus() == BookingStatus.UPDATED) {
                    attemptToMatchCab(rideRequest);
                }
                
            } catch (Exception e) {

                System.out.println("Could not schedule booking. " + e.toString());
            }
        }            
    }
}
