package com.lldubercab;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.booking.Booking;
import com.lldubercab.model.booking.BookingStatus;
import com.lldubercab.model.cab.Cab;
import com.lldubercab.model.passenger.Passenger;
import com.lldubercab.services.CabManager;
import com.lldubercab.services.RideService;

// Version 1
// 
// public class Main {
//     public static void main(String[] args) {

//         final Passenger user = new Passenger("connor");
//         final Cab cab1 = new Cab(0, false, new Location(25.00, 25.00));
//         final Cab cab2 = new Cab(1, false, new Location(25.00, 25.00));

//         final CabManager cabManager = new CabManager();
//         cabManager.activate(cab1);
//         cabManager.activate(cab2);

//         cabManager.deactivate(cab1);
        
//         final2 RideService rideService = new RideService(cabManager);
        
//         final Booking booking = rideService.requestRide(user, System.currentTimeMillis());
        
//         final BookingStatus status = rideService.checkStatus(booking);

//         System.out.println("Current booking status: " + status);
//     }
// }



public class Main {
    public static void main(String[] args) {

        final Passenger user = new Passenger("connor");

        final Cab cab1 = new Cab(0, false, new Location(25.00, 25.00));
        final Cab cab2 = new Cab(1, false, new Location(25.00, 25.00));
        final Cab cab3 = new Cab(3, false, new Location(25.00, 25.00));

        final CabManager cabManager = new CabManager();
        cabManager.insertIntoFleet(cab1);
        cabManager.insertIntoFleet(cab2);
        cabManager.insertIntoFleet(cab3);
        
        final RideService rideService = new RideService(cabManager);
        
        final Booking booking = rideService.requestRide(user,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));
        
        final BookingStatus status = rideService.checkStatus(booking.getId());

        System.out.println("Current booking status: " + status);

        try { Thread.sleep(2000);} catch (Exception e) {};

        final Booking booking2 = rideService.requestRide(user,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));
        
        try { Thread.sleep(3000);} catch (Exception e) {};

        final Cab cab4 = new Cab(4, false, new Location(25.00, 25.00));
        final Cab cab5 = new Cab(5, false, new Location(25.00, 25.00));

        cabManager.insertIntoFleet(cab4);
        cabManager.insertIntoFleet(cab5);

        final Passenger user2 = new Passenger("connor2");
        final Booking booking3 = rideService.requestRide(user2,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));

        final Passenger user3 = new Passenger("connor3");
        final Booking booking4 = rideService.requestRide(user3,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));

        final Passenger user4 = new Passenger("connor4");
        final Booking booking5 = rideService.requestRide(user4,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));


        final Passenger user5 = new Passenger("connor5");
        final Booking booking6 = rideService.requestRide(user5,  System.currentTimeMillis(), new Location(25.00, 25.00), new Location(25.00, 25.00));

        try { Thread.sleep(2000);} catch (Exception e) {};

        final Cab cab6 = new Cab(6, false, new Location(25.00, 25.00));
        cabManager.insertIntoFleet(cab6);

        try { Thread.sleep(2000);} catch (Exception e) {};

        System.out.println("Status of booking " + booking.getId() + ": " + rideService.checkStatus(booking.getId()));
        System.out.println("Status of booking " + booking4.getId() + ": " + rideService.checkStatus(booking4.getId()));
        System.out.println("Status of booking " + booking6.getId() + ": " + rideService.checkStatus(booking6.getId()));
        
        try { Thread.sleep(10000);} catch (Exception e) {};

        System.out.println("Status of booking " + booking.getId() + ": " + rideService.checkStatus(booking.getId()));
        System.out.println("Status of booking " + booking4.getId() + ": " + rideService.checkStatus(booking4.getId()));
        System.out.println("Status of booking " + booking6.getId() + ": " + rideService.checkStatus(booking6.getId()));

    }
}