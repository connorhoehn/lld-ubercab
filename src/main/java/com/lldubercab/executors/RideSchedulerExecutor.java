package com.lldubercab.executors;


import com.lldubercab.model.ConcurrentRideStore;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.services.CabManager;

public class RideSchedulerExecutor implements Runnable {

    CabManager cabManager;
    ConcurrentRideStore activeRideQueue;
    ConcurrentRideStore pendingRideQueue;

    public RideSchedulerExecutor(ConcurrentRideStore pq, ConcurrentRideStore ac, CabManager cManager) {
        this.pendingRideQueue = pq;
        this.activeRideQueue = ac;
        this.cabManager = cManager;
    }

    public void scheduleBooking(Booking booking) {
        Cab matchedCab = cabManager.findMatchingcab(booking);

        if (matchedCab != null) {
            final boolean bookingResult = cabManager.assign(matchedCab);
            
            if (bookingResult) {
                booking.setCab(matchedCab);
                booking.setStatus(BookingStatus.ACTIVE);
                activeRideQueue.insert(booking);
    
                System.out.println(Thread.currentThread().getName() + " - Assigning Passenger: " + booking.getPassenger().getName() + " with cab:"  + matchedCab.getId());
                return;
            } else {
                System.out.println("There was an issue booking the cab.");
                synchronized (pendingRideQueue) {
                    booking.setStatus(BookingStatus.CREATED);
                    pendingRideQueue.insert(booking);
                }

            }
        } else {
            synchronized (pendingRideQueue) {
                booking.setStatus(BookingStatus.CREATED);
                pendingRideQueue.insert(booking);
            }
        }
    }

    public void run() {
        while (true) {
            try {
                Booking rideRequest = pendingRideQueue.removeOrWait();
                rideRequest.setStatus(BookingStatus.CREATED);
                
                scheduleBooking(rideRequest);
            } catch (Exception e) {

                System.out.println("Could not schedule booking. " + e.toString());
            }
        }            
    }
}
