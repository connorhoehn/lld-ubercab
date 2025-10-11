package com.lldubercab.model.booking;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.CabCategory;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.passenger.Passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@ToString
@Builder
public class Booking {
    private int id;
    private Passenger passenger;
    private Cab cab;
    private BookingStatus status;
    private Location pickupLocation;
    private Location deliveryLocation;
    private Integer maxDistance;
    private CabCategory preferredCabCategory;
    private Double maxPrice;

    public synchronized void setStatus(BookingStatus status) {
        this.status = status;
    } 

    public synchronized BookingStatus getStatus() {
        return status;
    } 

}
