package com.lldubercab.model.booking;

public enum BookingStatus {
   UNKNOWN,
   CREATED, // user booking recieved
   UPDATED, //user updated their request
   NO_CABS_AVAILABLE, //system provided feedback that it could not book based on parameters
   ALLOCATING_CAB, // matching a cab to the booking
   MATCHED, // found cab
   ACTIVE, // ride in progress
   COMPLETING, // wrapping up the booking
   COMPLETE  // done, archived
}
