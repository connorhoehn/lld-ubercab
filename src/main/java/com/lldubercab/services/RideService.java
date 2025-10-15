package com.lldubercab.services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lldubercab.executors.RideCleanupExecutor;
import com.lldubercab.executors.RideSchedulerExecutor;
import com.lldubercab.model.ConcurrentRideStore;
import com.lldubercab.model.RideRequest;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.strategies.pricing.DefaultsPricingModifierStrategy;
import com.lldubercab.strategies.pricing.IPricingCalculatorStrategy;
import com.lldubercab.strategies.pricing.DemandPricingCalculatorStrategy;
import com.lldubercab.strategies.pricing.DiscountPricingStrategy;

import lombok.SneakyThrows;

public class RideService {

    private List<IPricingCalculatorStrategy> pricingCalculatorStrategies = Arrays.asList(
        new DefaultsPricingModifierStrategy(),
        new DemandPricingCalculatorStrategy(),
        new DiscountPricingStrategy());

    final private ConcurrentRideStore pendingRideQueue = new ConcurrentRideStore();
    final private ConcurrentRideStore activeRideQueue = new ConcurrentRideStore();
    
    final private List<Booking> allRidesStore = new ArrayList<>();

    final private CabManager cabManager;

    public RideService(CabManager cManager) {
        cabManager = cManager;
        startRideRequestConsumer();
        startRideCleanUpConsumer();
    }

    @SneakyThrows
    private void startRideCleanUpConsumer() {
        
        for (int i = 0; i < 2; i++) {
            new Thread(new RideCleanupExecutor(activeRideQueue, cabManager), "thread-" + i).start();
        }
    }

    @SneakyThrows
    private void startRideRequestConsumer() {

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
            .rideRequest(rideRequest)
            .build();

        // 1) Apply the default pricing stragies for a base rate on known parameters
        for (IPricingCalculatorStrategy strategy: pricingCalculatorStrategies) {
            strategy.update(booking);
        }

        // 2) Update Stores
        allRidesStore.add(booking);
        rideRequest.getPassenger().addRideRequest(rideRequest); // utility for user to update ride request parameters

        // 3) Worker Queue
        pendingRideQueue.insert(booking);

        System.out.println("Added new booking request to the queue: " + booking.getId());

        return booking;
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
