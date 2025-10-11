package com.lldubercab.services;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lldubercab.executors.RideCleanupExecutor;
import com.lldubercab.executors.RideSchedulerExecutor;
import com.lldubercab.model.ConcurrentRideStore;
import com.lldubercab.model.RideRequest;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;

import lombok.SneakyThrows;

public class RideService {

    final private ConcurrentRideStore pendingRideQueue = new ConcurrentRideStore();
    final private ConcurrentRideStore activeRideQueue = new ConcurrentRideStore();
    
    final private List<Booking> allRidesStore = new ArrayList<>();

    final static int WORKERS = Runtime.getRuntime().availableProcessors();

    final private CabManager cabManager;

    public RideService(CabManager cManager) {
        cabManager = cManager;
        startRideRequestConsumer();
        startRideCleanUpConsumer();
    }

    @SneakyThrows
    private void startRideCleanUpConsumer() {
        System.out.println("Number of cores on system: " + WORKERS + " and number assigned to Ride Expirer: 2");
        
        for (int i = 0; i < 2; i++) {
            new Thread(new RideCleanupExecutor(activeRideQueue, cabManager), "thread-" + i).start();
        }
    }

    @SneakyThrows
    private void startRideRequestConsumer() {

        System.out.println("Number of cores on system: " + WORKERS + " and number assigned to Ride Dispatcher: 10");

        for (int i = 0; i < 10; i++) {
            new Thread(new RideSchedulerExecutor(pendingRideQueue, activeRideQueue, cabManager), "thread-" + i).start();
        }
    }

    private static final AtomicInteger nextBookingId = new AtomicInteger(1);

    public Booking requestRide(RideRequest rideRequest) {

        final Booking booking = Booking.builder()
            .id(nextBookingId.incrementAndGet())
            .passenger(rideRequest.getPassenger())
            .status(BookingStatus.CREATED)
            .pickupLocation(rideRequest.getPickup())
            .deliveryLocation(rideRequest.getDropOff())
            .maxDistance(rideRequest.getMaxDistanceAway())
            .maxPrice(rideRequest.getMaxPrice())
            .preferredCabCategory(rideRequest.getCategory())
            .build();

        allRidesStore.add(booking);

        addToQueue(booking);

        System.out.println("Added new booking request to the queue: " + booking.getId());

        return booking;
    }

    private void addToQueue(Booking booking) {
        synchronized(pendingRideQueue) {
            pendingRideQueue.insert(booking);
            pendingRideQueue.notifyAll();
        }
    }

    public BookingStatus checkStatus(int bookingId) {

        for (Booking booking: allRidesStore) {
            if (booking.getId() == bookingId) {
                return booking.getStatus();
            }
        }

        return BookingStatus.UNKNOWN;
    }
}
