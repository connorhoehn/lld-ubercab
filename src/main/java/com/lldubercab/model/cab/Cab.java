package com.lldubercab.model.cab;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lldubercab.internal.Balance;
import com.lldubercab.internal.Location;
import com.lldubercab.model.CabCategory;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@Builder
public class Cab implements ICabObserverer {

    private final static AtomicInteger idCounter = new AtomicInteger(1);

    private Integer id;
    private Boolean booked;
    private Location location;
    private CabCategory category;

    private Balance balance; 

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

    @SneakyThrows
    public void respondToRideRequest(Booking booking) {
        // 1) Driver will get a precomputed fare price on default pricing and distance
        // Price farePrice = booking.getQuote(this);

        Thread.sleep(100);

        // 2) Create a default response 
        CabNotificationResponse response;

        // 3 ) Simulate decision lag or probability
        Random random = new Random();
        if (random.nextDouble() < .6) {
            //review the booking and decide if you want to take it..
            response = CabNotificationResponse.builder()
                .cabId(this.getId())
                .response(true)
                .build();
        } else {
            response = CabNotificationResponse.builder()
                .cabId(this.getId())
                .response(false)
                .build();
        }

        // 4) Add the response to the booking 
        synchronized(booking) {
            if (booking.getStatus() == BookingStatus.CREATED) {
                booking.addDriverResponse(response);
            }
        }

    }

}
