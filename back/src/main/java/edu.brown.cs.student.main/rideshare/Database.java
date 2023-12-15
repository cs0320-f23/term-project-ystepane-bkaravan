package edu.brown.cs.student.main.rideshare;

import java.util.ArrayList;
import java.util.List;

public class Database {

  private final List<Ride> rides;
  private Ride pending;
  private int lastID = 0;

  public Database() {
    this.rides = new ArrayList<>();
    this.pending = null;
  }

  public void addRide(Ride toAdd) {
    if (!this.rides.contains(toAdd)) {
      toAdd.setRideID(this.lastID);
      this.lastID++;
      this.rides.add(toAdd);
    }
  }
  // maybe remove the rides that already happened or add a "cannot join a ride in the past" warning
  public void joinRide(Guest guest, Ride ride) {
//    Ride currentRide = this.rides.get();
    for (Ride checkRide : this.rides) {
      if (checkRide.equals(ride)) {
        checkRide.addGuest(guest);
      }
    }
  }

  public void joinByID(Guest guest, int id) {
    for (Ride checkRide : this.rides) {
      if (checkRide.idMatch(id)) {
        checkRide.addGuest(guest);
        return;
      }
    }
  }

  // add time
  public void createRide(City orig, City dest, RideType type, int spotsLeft, Guest host) {
    Ride newRide = new Ride(orig, dest, type, spotsLeft, host);
    if (!this.rides.contains(newRide)) {
      newRide.setRideID(this.lastID);
      this.lastID++;
      this.rides.add(newRide);
    }
  }

  public boolean isEmpty() {
    return this.rides.isEmpty();
  }

  public void setPending(Ride ride) {
    this.pending = ride;
  }

  public void delPending() {
    this.pending = null;
  }

  public boolean hasPending() {
    return (this.pending != null);
  }

  public Ride getPending() {
    return this.pending;
  }

  @Override
  public String toString() {
    String toRet = "";
    for (Ride ride : this.rides) {
      toRet = toRet + ride.toString() + " \n";
    }
    return toRet;
  }

}
