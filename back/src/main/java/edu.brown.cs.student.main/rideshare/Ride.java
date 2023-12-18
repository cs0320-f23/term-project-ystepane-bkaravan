package edu.brown.cs.student.main.rideshare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride {

  private final Date time;
  private int rideID;
  private int rideScore;
  private final City origin;
  private final City destination;
  private RideType type;
  private int spotsLeft;
  //private final String departureTime;
  private Guest host;
  private final List<Guest> guests;

//  private Date departureTime;
  // add time
  public Ride(City orig, City dest, RideType type, int spots, Guest host, Date time) {
    //this.departureTime = time;
    this.time = time;
    this.rideScore = 0;
    this.rideID = 0;
    this.origin = orig;
    this.destination = dest;
    this.type = type;
    this.spotsLeft = spots;
    this.host = host;
    this.guests = new ArrayList<>(spots);
  }

  public Ride(City orig, City dest, Date newTime) {
    this.time = newTime;
    //this.departureTime = time;
    this.rideID = 0;
    this.origin = orig;
    this.destination = dest;
    this.type = RideType.PENDING;
    this.spotsLeft = 2;
    this.host = new Guest("Consider" ,"Creating", "Ride");
    this.guests = new ArrayList<>();
  }

  public boolean addGuest(Guest guest, int score) {
    if (this.spotsLeft > 0 && (!this.guests.contains(guest))) {
      this.guests.add(guest);
      this.rideScore += score;
      this.spotsLeft--;
      return true;
    }
    return false;
  }

  public boolean idMatch(int id) {
    return this.rideID == id;
  }

  public void adjustRide(Guest host, int spotsLeft, RideType type, int score) {
    this.host = host;
    this.spotsLeft = spotsLeft;
    this.type = type;
    this.rideScore = score;
  }

  public double getDistance() {
    double d1 = Math.pow((this.destination.getLat() - this.origin.getLat()), 2);
    double d2 = Math.pow((this.destination.getLon() - this.origin.getLon()), 2);
    return Math.sqrt(d1 + d2);
  }

  public City getOrigin() {return this.origin;}
  public City getDestination() {return this.destination;}
  public int getRideScore() {
    return this.rideScore;
  }

  //public String getTime() {return this.departureTime;}

  public Date getTime() {return this.time;}

  public boolean hasSpace() {return this.spotsLeft > 0;}

  public void setRideID(int ID) {
    this.rideID = ID;
  }
  public int getRideID() {return this.rideID; }

  @Override
  public String toString() {
    String toRet = "";
    toRet = toRet + "ID: " + this.rideID + " " + origin.getName() + " " + destination.getName() + " " + this.type + " "
        + this.host.getName() + " " + this.spotsLeft + " " + this.guests;
    return toRet;
  }

}
