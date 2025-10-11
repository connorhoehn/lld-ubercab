package com.lldubercab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;

public class CabManager {
    private Map<Integer, Cab> cabs = new ConcurrentHashMap<>();

    final private MatcherService matcherService = new MatcherService();

    public boolean cabsAvailable() {
        return cabs.size() > 0 ? true : false;
    }

    public Cab findCab(Integer id) {
        return cabs.get(id);
    }

    public void insertIntoFleet(Cab cab) {
        cabs.put(cab.getId(), cab);
    }
    
    public boolean assign(Cab cab) {
        return cab.atomicBook();
    }
    
    public boolean release(Cab cab) {
        Cab foundCab = findCab(cab.getId());

        final Boolean releaseStatus = foundCab.atomicRelease();

        if (releaseStatus) {
            synchronized(cabs) {
                cabs.notifyAll();
            }
        }

        return releaseStatus;
    }

    public synchronized List<Cab> findAvailableCabs() {

        List<Cab> allCabs = new ArrayList<>(cabs.values());
        List<Cab> availableList = new ArrayList<>();

        for (Cab currentCab: allCabs) {
            if(!currentCab.getBooked()) {
                availableList.add(currentCab);
            }
        }
        return availableList;
    }

    public Cab findMatchingcab(Booking request) {
        synchronized(cabs) {
            while (findAvailableCabs().isEmpty()) {
                try {
                    cabs.wait();
                } catch (Exception e) {
                    //
                }
            }
            List<Cab> availableCabs = findAvailableCabs();

            return matcherService.match(request, availableCabs);
        }
    }

}
