package com.lldubercab.model.cab;

import java.util.concurrent.atomic.AtomicInteger;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.CabCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cab {

    private final static AtomicInteger idCounter = new AtomicInteger(1);

    private Integer id;
    private Boolean booked;
    private Location location;
    private CabCategory category;

    public static class CabBuilder {
        public Cab build() {
            if (id == null) {
                id = idCounter.getAndIncrement();
            }
            return new Cab(id, location, category);
        }
    }

    private Cab(Integer id, Location location, CabCategory category) {
        this.id = id;
        this.booked = false;
        this.location = location;
        this.category = category;
    }

    public synchronized boolean atomicBook() {
        
        if (booked) {
            return false;
        }
        
        booked = true;
        return true;
    }

    public synchronized boolean atomicRelease() {
        
        if (!booked) {
            return false;
        }
        
        booked = false;
        return true;
    }

}
