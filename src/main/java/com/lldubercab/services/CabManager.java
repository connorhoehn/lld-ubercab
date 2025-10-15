package com.lldubercab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.strategies.ranking.IRankingType;

import lombok.SneakyThrows;

import com.lldubercab.strategies.ranking.BestRatingRankingPolicy;
import com.lldubercab.strategies.ranking.DefaultRankingPolicy;
import com.lldubercab.strategies.ranking.IRankingPolicy;

public class CabManager {
    private NotificationManager notificationManager = new NotificationManager();
    
    final private Map<IRankingType, IRankingPolicy> matchingPolicies = Map.of(
        IRankingType.DEFAULT, new DefaultRankingPolicy(),
        IRankingType.BEST_RATING, new BestRatingRankingPolicy()
        );

    private Map<Integer, Cab> cabs = new ConcurrentHashMap<>();

    final private FilterService filterService = new FilterService();

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
        boolean status = cab.atomicBook();

        if (status) {
            notificationManager.unsubscribeAvailableCab(cab);            
        }

        return status;
    }
    
    public boolean release(Cab cab) {
        final Boolean releaseStatus = cab.atomicRelease();

        if (releaseStatus) {
            notificationManager.subscribeAvailableCab(cab);
        }

        return releaseStatus;
    }

    public List<Cab> findAvailableCabs() {

        List<Cab> allCabs = new ArrayList<>(cabs.values());
        List<Cab> availableList = new ArrayList<>();

        for (Cab currentCab: allCabs) {
            if(!currentCab.getBooked()) {
                availableList.add(currentCab);
            }
        }
        return availableList;
    }

    public Cab matchAndRank(Booking booking) {
        
        // 1) Get Riders Matching Policy
        IRankingType driverRequestedPolicy = booking.getRideRequest().getRankingStrategy();
        IRankingPolicy policy = matchingPolicies.get(driverRequestedPolicy);
        
        // 2) Find available cabs
        List<Cab> cabs = this.getAvailableCabs(booking);

        // 3) Rank the filtered cabs 
        cabs = policy.rank(booking, cabs);

        // 4) Return the first one for now
        Cab cab = cabs.get(0);
        return (cab == null) ? null : cab;
    }

    @SneakyThrows
    public List<Cab> getAvailableCabs(Booking request) {
        synchronized(cabs) {
            while (findAvailableCabs().isEmpty()) {
                cabs.wait();
            }

            // Get all active cabs
            List<Cab> availableCabs = findAvailableCabs();

            // Filters based on ride request paramers
            List<Cab> matchedCabs = filterService.match(request, availableCabs);

            return matchedCabs;
        }
    }

}
