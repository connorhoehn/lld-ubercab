package com.lldubercab.model;

import java.util.ArrayList;
import java.util.List;
import com.lldubercab.model.booking.Booking;

import lombok.SneakyThrows;

public class ConcurrentRideStore {

    private List<Booking> rideStore = new ArrayList<>();

    public boolean isEmpty() {
        synchronized(rideStore) {
            if(rideStore.isEmpty()) {
                return true;
            }
            return false;
        }
    }

    public void insert(Booking booking) {
        synchronized(rideStore) {
            rideStore.add(booking);
            rideStore.notifyAll();
        }
    }

    @SneakyThrows
    public Booking removeOrWait() {
        synchronized(rideStore) {
            while(rideStore.isEmpty()) {
                rideStore.wait();
            }
            if (!rideStore.isEmpty()) {
                return rideStore.remove(0);
            }
        }
        return removeOrWait();
    }

    public List<Booking> clone() {
        synchronized(rideStore) {
            return new ArrayList<>(rideStore);
        }
    }

}
