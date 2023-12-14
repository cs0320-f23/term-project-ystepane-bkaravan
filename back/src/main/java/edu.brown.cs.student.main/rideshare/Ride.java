package edu.brown.cs.student.main.rideshare;

import java.util.ArrayList;
import java.util.List;

public class Ride {

  private final City origin;
  private final City destination;
  private RideType type;
  private int spotsLeft;
  private int departureTime;
  private Guest host;
  private final List<Guest> guests;

//  private Date departureTime;
  // add time
  public Ride(City orig, City dest, RideType type, int spots, Guest host) {
    this.origin = orig;
    this.destination = dest;
    this.type = type;
    this.spotsLeft = spots;
    this.host = host;
    this.guests = new ArrayList<>(spots);
  }

  public Ride(City orig, City dest) {
    this.origin = orig;
    this.destination = dest;
    this.type = RideType.PENDING;
    this.spotsLeft = 2;
    this.host = new Guest("Consider" ,"Creating", "Ride");
    this.guests = new ArrayList<>();
  }

  public void addGuest(Guest guest) {
    if (this.spotsLeft > 0 && (!this.guests.contains(guest))) {
      this.guests.add(guest);
      this.spotsLeft--;
    }
  }

  public void adjustRide(Guest host, int spotsLeft, RideType type) {
    this.host = host;
    this.spotsLeft = spotsLeft;
    this.type = type;
  }

  @Override
  public String toString() {
    String toRet = "";
    toRet = toRet + origin.getName() + " " + destination.getName() + " " + this.type + " "
        + this.host.getName() + " " + this.spotsLeft + " " + this.guests;
    return toRet;
  }

}
