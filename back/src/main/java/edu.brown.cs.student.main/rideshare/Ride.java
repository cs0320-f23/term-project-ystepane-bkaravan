package edu.brown.cs.student.main.rideshare;

import java.util.ArrayList;
import java.util.List;

public class Ride {

  private City origin;
  private City destination;
  private RideType type;
  private int spotsLeft;
  private int departureTime;
  private Guest host;
  private List<Guest> guests;

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

  public void addGuest(Guest guest) {
    if (this.spotsLeft > 0 && (!this.guests.contains(guest))) {
      this.guests.add(guest);
      this.spotsLeft--;
    }
  }

  @Override
  public String toString() {
    String toRet = "";
    toRet = toRet + origin.getName() + " " + destination.getName() + " "  + this.host.getName() + " " + this.spotsLeft + " " + this.guests;
    return toRet;
  }


}
