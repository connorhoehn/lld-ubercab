package com.lldubercab.model.booking;

import java.util.List;

import com.lldubercab.internal.Price;
import com.lldubercab.model.RideRequest;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.cab.CabNotificationResponse;
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
    private BookingStatus status;
    // Passenger
    private Passenger passenger;
    // Cab
    private Cab cab;
    // Rider Request
    private RideRequest rideRequest;
    // Driver Responses
    private List<CabNotificationResponse> driverResponses;

    private Price fareQuote;
    
    public synchronized void setStatus(BookingStatus status) {
        this.status = status;
    }

    public synchronized BookingStatus getStatus() {
        return status;
    } 

    public void addDriverResponse(CabNotificationResponse response) {
        driverResponses.add(response);
    }

    public synchronized Price getQuote(Cab cab) {
        // review the cab distance and adjust the price
        return this.fareQuote;
    }
    
    // Allow cabs to book directly on this
    public synchronized void book(Cab cab) {
        this.status = BookingStatus.ALLOCATING_CAB;
        this.cab = cab;
    }

}
