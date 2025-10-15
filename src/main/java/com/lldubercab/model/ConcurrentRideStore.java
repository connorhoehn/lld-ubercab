package com.lldubercab.model;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.lldubercab.model.booking.Booking;

import lombok.SneakyThrows;

public class ConcurrentRideStore {

    private PriorityQueue<Booking> rideQueue = new PriorityQueue<>(new Comparator<Booking>() {
        @Override
        public int compare(Booking a, Booking b) {
            return Long.compare(a.getRideRequest().getRequestTime(), b.getRideRequest().getRequestTime());
        }
    });

    public void insert(Booking booking) {
        synchronized(rideQueue) {
            rideQueue.add(booking);
            rideQueue.notifyAll();
        }
    }

    @SneakyThrows
    public Booking removeOrWait() {
        synchronized(rideQueue) {
            Booking booking;

            while(rideQueue.isEmpty()) {
                rideQueue.wait();
                booking = rideQueue.poll();
                if (booking != null) {
                    return booking;
                }
            }
        }
        return removeOrWait();
    }
}
