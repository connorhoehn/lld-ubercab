package com.lldubercab.executors;

import java.util.Random;

import com.lldubercab.model.ConcurrentRideStore;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.services.CabManager;

public class RideCleanupExecutor implements Runnable {

    private final static Random random = new Random();

    ConcurrentRideStore activeRideStore;
    CabManager cabManager;

    public RideCleanupExecutor(ConcurrentRideStore activeQ, CabManager cManager) {
        activeRideStore = activeQ;
        cabManager = cManager;
    }

    public void run() {
        while (true) {
            try {
                Booking booking = activeRideStore.removeOrWait();
                booking.setStatus(BookingStatus.COMPLETING);
                
                simulateTrip(booking);
            } catch (Exception e) {

                System.out.println("Could not complete booking. " + e.toString());
            }
        }            
    }

    private void archiveBooking(Booking booking) {

        final boolean releaseStatus = cabManager.release(booking.getCab());
        if (!releaseStatus) {
            System.out.println("Unable to release cab and booking");
        }

        booking.setStatus(BookingStatus.COMPLETE);
        System.out.println(Thread.currentThread().getName() + " Booking: " + booking.getId() + " is complete. Released cab: " + booking.getCab().getId());
    }

    public void simulateTrip(Booking booking) {
        while (true) {

            int durationOfTrip = 2000 + random.nextInt(3000);
            try {Thread.sleep(durationOfTrip); } catch (Exception e) {};

            if (booking.getStatus() == BookingStatus.COMPLETING) {
                archiveBooking(booking);
                return;
            } else {
                System.out.println("Booking " + booking.getId() + " status changed unexpectedly to: " + booking.getStatus());
                return;
            }
        }
        
    }
}
