package com.lldubercab;

import com.lldubercab.internal.Location.Location;
import com.lldubercab.model.CabCategory;
import com.lldubercab.model.RideRequest;
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

        final Cab cab1 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.LIMO)
            .build();
        final Cab cab2 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.STANDARD)
            .build();
        final Cab cab3 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.STANDARD)
            .build();

        final CabManager cabManager = new CabManager();
        cabManager.insertIntoFleet(cab1);
        cabManager.insertIntoFleet(cab2);
        cabManager.insertIntoFleet(cab3);
        
        final RideService rideService = new RideService(cabManager);
        
        final Booking booking = rideService.requestRide(RideRequest.builder()
            .passenger(user)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(2000.0)
            .build());
        
        final BookingStatus status = rideService.checkStatus(booking.getId());

        System.out.println("Current booking status: " + status);

        try { Thread.sleep(2000);} catch (Exception e) {};

        final Passenger user1 = new Passenger("connor1");

        rideService.requestRide(
            RideRequest.builder()
            .passenger(user1)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(2000.0)
            .build()
        );
        
        try { Thread.sleep(3000);} catch (Exception e) {};

        final Cab cab4 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.STANDARD)
            .build();

        final Cab cab5 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.STANDARD)
            .build();

        cabManager.insertIntoFleet(cab4);
        cabManager.insertIntoFleet(cab5);

        final Passenger user2 = new Passenger("connor2");

        rideService.requestRide(
            RideRequest.builder()
            .passenger(user2)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(2000.0)
            .build()
        );

        final Passenger user3 = new Passenger("connor3");

        rideService.requestRide(
            RideRequest.builder()
            .passenger(user3)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(2000.0)
            .build()
        );
        final Passenger user4 = new Passenger("connor4");

        final Booking booking4 = rideService.requestRide(
            RideRequest.builder()
            .passenger(user4)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(200.0)
            .build()
        );

        final Passenger user5 = new Passenger("connor5");

        final Booking booking5 = rideService.requestRide(
            RideRequest.builder()
            .passenger(user5)
            .pickup(new Location(25.0, 25.0))
            .dropOff(new Location(25.0, 25.0))
            .maxDistanceAway(25)
            .category(CabCategory.STANDARD)
            .maxPrice(2000.0)
            .build()
        );

        try { Thread.sleep(2000);} catch (Exception e) {};

        final Cab cab6 = Cab.builder()
            .location(new Location(25.0, 25.0))
            .category(CabCategory.STANDARD)
            .build();

        cabManager.insertIntoFleet(cab6);

        try { Thread.sleep(2000);} catch (Exception e) {};

        System.out.println("Status of booking " + booking.getId() + ": " + rideService.checkStatus(booking.getId()));
        System.out.println("Status of booking " + booking4.getId() + ": " + rideService.checkStatus(booking4.getId()));
        System.out.println("Status of booking " + booking5.getId() + ": " + rideService.checkStatus(booking5.getId()));
        
        try { Thread.sleep(10000);} catch (Exception e) {};

        System.out.println("Status of booking " + booking.getId() + ": " + rideService.checkStatus(booking.getId()));
        System.out.println("Status of booking " + booking4.getId() + ": " + rideService.checkStatus(booking4.getId()));
        System.out.println("Status of booking " + booking5.getId() + ": " + rideService.checkStatus(booking5.getId()));

    }
}