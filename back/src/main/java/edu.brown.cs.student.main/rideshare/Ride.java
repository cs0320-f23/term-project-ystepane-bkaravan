package edu.brown.cs.student.main.rideshare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class that handles everything has to do with a ride
 */

public class Ride {

  private final Date time;
  private int rideID;
  private int rideScore;
  private final City origin;
  private final City destination;
  private RideType type;
  private int spotsLeft;
  private Guest host;
  private final List<Guest> guests;

//  private Date departureTime;
  // add time

  /**
   * Constructor for the backend example where all information is known
   * @param orig
   * @param dest
   * @param type
   * @param spots
   * @param host
   * @param time
   */
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

  /**
   * Constructor for the frontend use, creates a pending ride that later gets updated
   * @param orig
   * @param dest
   * @param newTime
   */
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

  /**
   * Adds a guest to a list of guests of the current ride with some error checking
   * @param guest a guest to add to the list
   * @param score a score to adjust for the ride
   * @return true if guest was added, false otherwise
   */

  public boolean addGuest(Guest guest, int score) {
    if (this.spotsLeft > 0 && (!this.guests.contains(guest))) {
      this.guests.add(guest);
      this.rideScore += score;
      this.spotsLeft--;
      return true;
    }
    return false;
  }

  /**
   * Helper method that helps with defensive programming when adding guests through the Database
   * class
   * @param id to match
   * @return true if id of the ride matched the input ID
   */
  public boolean idMatch(int id) {
    return this.rideID == id;
  }

  /**
   * Helper method for adjusting the ride, after a pending ride has been created
   * @param host the new host
   * @param spotsLeft the number of spots for the ride
   * @param type enumed type
   * @param score the score of the host
   */
  public void adjustRide(Guest host, int spotsLeft, RideType type, int score) {
    this.host = host;
    this.spotsLeft = spotsLeft;
    this.type = type;
    this.rideScore = score;
  }

  /**
   * Helper method to get the distance of the ride by using the distance formula from coordinates
   * @return distance as a double
   */
  public double getDistance() {
    double d1 = Math.pow((this.destination.getLat() - this.origin.getLat()), 2);
    double d2 = Math.pow((this.destination.getLon() - this.origin.getLon()), 2);
    return Math.sqrt(d1 + d2);
  }

  /**
   * getter for the origin
   * @return
   */
  public City getOrigin() {return this.origin;}

  /**
   * getter for destination
   * @return
   */
  public City getDestination() {return this.destination;}

  /**
   * getter for the ridescore
   * @return
   */
  public int getRideScore() {
    return this.rideScore;
  }

  /**
   * Getter for current ride
   * @return
   */
  public Date getTime() {return this.time;}

  /**
   * Boolean if the current ride has space for more guests
   * @return true if we have more than 0 spots
   */
  public boolean hasSpace() {return this.spotsLeft > 0;}

  /**
   * setter for the current rideID, used when a new ride is created
   * @param ID
   */
  public void setRideID(int ID) {
    this.rideID = ID;
  }

  /**
   * Getter for the current rideID
   * @return
   */
  public int getRideID() {return this.rideID; }

  /**
   * To string method
   * @return a string representation of the ride
   */

  @Override
  public String toString() {
    String toRet = "";
    toRet = toRet + "ID: " + this.rideID + " " + origin.getName() + " " + destination.getName() + " " + this.type + " "
        + this.host.getName() + " " + this.spotsLeft + " " + this.guests;
    return toRet;
  }

}
