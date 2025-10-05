package com.lldubercab.services;

import java.util.ArrayList;
import java.util.List;

import com.lldubercab.model.cab.Cab;

public class CabManager {
    private List<Cab> cabs = new ArrayList<>();

    public boolean cabsAvailable() {
        return cabs.size() > 0 ? true : false;
    }

    public Cab findCab() {
        return cabs.get(0);
    }
    
    public void insertIntoFleet(Cab cab) {
        cabs.add(cab);
    }
    
    public synchronized List<Cab> findAvailableCabs() {
        List<Cab> availableList = new ArrayList<>();
        for (Cab currentCab: cabs) {
            if(!currentCab.getBooked()) {
                availableList.add(currentCab);
            }
        }
        return availableList;
    }

    private Cab getCab(Cab cab) {
        for (Cab c: cabs) {
            if (c.getId() == cab.getId()) {
                synchronized (c) {
                    return c;
                }
            }
        }
        return null;
    }

    public void assign(Cab cab) {
        synchronized(cab) { // thread safety
            cab.setBooked(true);
        }
    }

    public boolean release(Cab cab) {
        Cab foundCab = getCab(cab); 
        
        synchronized(foundCab) { // thread safety
            if (foundCab.getBooked()) {
                foundCab.setBooked(false);
            }
        }
        return false;
    }
}
