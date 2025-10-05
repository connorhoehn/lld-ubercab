package com.lldubercab.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.passenger.Passenger;

import lombok.SneakyThrows;


public class RideService {
    
    final private List<Booking> rideRequestQueue = new ArrayList<>();
    final private List<Booking> activeRideQueue = new ArrayList<>();

    final private List<Booking> archivedTrips = new ArrayList<>();

    final static int WORKERS = Runtime.getRuntime().availableProcessors();

    final private CabManager cabManager;

    final private MatcherService matcherService;

    public RideService(CabManager cManager) {
        cabManager = cManager;
        matcherService = new MatcherService();
        startRideRequestConsumer();
        startRideCleanUpConsumer();
    }

    @SneakyThrows
    private void startRideCleanUpConsumer() {
        System.out.println("Number of cores on system: " + WORKERS + " and number assigned to Ride Expirer: 2");

        Runnable rideExpirer = () -> {
            while (true) {
                Booking booking = null;

                synchronized(activeRideQueue) {
                    while(activeRideQueue.size() == 0) {
                        System.out.println("There are no active rides in the system.");
                        try {activeRideQueue.wait(); } catch (Exception e) {};
                    }
                    System.out.println("There are " + activeRideQueue.size() + " rides right now.");
                    booking = activeRideQueue.get(0);    
                }

                Random randomWaitTime = new Random();
                int durationOfTrip = 2000 + randomWaitTime.nextInt(3000);
                try {Thread.sleep(durationOfTrip); } catch (Exception e) {};

               if (booking.getStatus() == BookingStatus.ACTIVE) {
                   synchronized(booking) {
                        cabManager.release(booking.getCab());
                        booking.setStatus(BookingStatus.COMPLETE);
                        archivedTrips.add(booking);
                        System.out.println(Thread.currentThread().getName() + " Booking: " + booking.getId() + " is complete. Released cab: " + booking.getCab().getId());
                   }
                   synchronized(activeRideQueue) {
                       activeRideQueue.remove(booking);
                   }
    
                   synchronized(archivedTrips) {
                       archivedTrips.add(booking);
                   }
               }
            }
        };
        
        for (int i = 0; i < 2; i++) {
            new Thread(rideExpirer, "thread-" + i).start();
        }
    }

    @SneakyThrows
    private void startRideRequestConsumer() {

        System.out.println("Number of cores on system: " + WORKERS + " and number assigned to Ride Dispatcher: 10");

        Runnable rideDispatcher = () -> {
            while (true) {
                Booking rideRequest = null;

                synchronized(rideRequestQueue) {
                    while (rideRequestQueue.isEmpty()) {
                        System.out.println("Waiting for ride requests.... " + Thread.currentThread().getName());
                        try { rideRequestQueue.wait(); } catch (Exception e) {}; //};
                    }
                    
                    rideRequest = rideRequestQueue.remove(0);
                }

                //find cabs, if no cab, put it back in the queue, 
                List<Cab> availableCabs = null;

                while(cabManager.findAvailableCabs().isEmpty()) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Waiting for cabs to become available...");
                    } catch (Exception e) {
                        //
                    };
                }

                availableCabs = cabManager.findAvailableCabs();

                Cab matchedCab = matcherService.match(rideRequest, availableCabs);

                if (matchedCab != null) {
                    synchronized (matchedCab) {
                        if (!matchedCab.getBooked()) {
                            cabManager.assign(matchedCab);
                            rideRequest.setCab(matchedCab);
                            rideRequest.setStatus(BookingStatus.ACTIVE);
                            System.out.println(Thread.currentThread().getName() + " - Assigning Passenger: " + rideRequest.getPassenger().getName() + " with cab:"  + matchedCab.getId());
                        } else {
                            System.out.println("Cab was just booked.. Adding ride request back into the queue.. ");
                            rideRequestQueue.add(rideRequest);
                            continue;
                        }
                    }

                    synchronized(activeRideQueue) {
                        activeRideQueue.add(rideRequest);
                        activeRideQueue.notifyAll();
                    }
                }
                
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(rideDispatcher, "thread-" + i).start();
        }
    }

    public Booking requestRide(Passenger user, Long time, Location pickup, Location dropoff) {

        final Booking booking = new Booking();
        booking.setId(new Random().nextInt(100));
        booking.setPassenger(user);
        booking.setPickupLocation(pickup);
        booking.setDeliveryLocation(dropoff);

        synchronized(rideRequestQueue) {
            rideRequestQueue.add(booking);
            rideRequestQueue.notifyAll();
        }

        return booking;
    }

    public BookingStatus checkStatus(int bookingId) {

        for (Booking activeBooking: activeRideQueue) {
            if (activeBooking.getId() == bookingId) {
                return activeBooking.getStatus();
            }
        }   

        for (Booking archivedBooking: archivedTrips) {
            if (archivedBooking.getId() == bookingId) {
                return archivedBooking.getStatus();
            }
        }   

        return null;
    }
}
