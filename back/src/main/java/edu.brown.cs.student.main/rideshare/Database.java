package edu.brown.cs.student.main.rideshare;

import edu.brown.cs.student.main.ridesorters.DateCompare;
import edu.brown.cs.student.main.ridesorters.DistanceCompare;
import edu.brown.cs.student.main.ridesorters.IDCompare;
import edu.brown.cs.student.main.ridesorters.ScoreCompare;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import spark.Request;

/**
 * This is our representation of the backend database, that keeps track of the rides
 */

public class Database {

  private final List<Ride> rides;
  private Ride pending;
  private int lastID = 0;

  private Guest currentUser;
  private int currentUserScore;

  /**
   * Constructor with some initializations
   */
  public Database() {
    this.rides = new ArrayList<>();
    this.pending = null;
    this.currentUser = null;
  }

  /**
   * Adding the ride and setting its id
   * @param toAdd ride to add to the database
   */
  public void addRide(Ride toAdd) {
    if (!this.rides.contains(toAdd)) {
      toAdd.setRideID(this.lastID);
      this.lastID++;
      this.rides.add(toAdd);
    }
  }

  /**
   * Helper method only used for mock data addition
   * @param guest to add to the ride
   * @param ride ride to modify
   */
  public void joinRide(Guest guest, Ride ride) {
    for (Ride checkRide : this.rides) {
      if (checkRide.equals(ride)) {
        checkRide.addGuest(guest, 0);
      }
    }
  }

  /**
   * Helper method used only for backend mock data, parameters are needed to init a Ride
   * @param orig
   * @param dest
   * @param type
   * @param spotsLeft
   * @param host
   * @param time
   */
  public void createRide(City orig, City dest, RideType type, int spotsLeft, Guest host, Date time) {
    Ride newRide = new Ride(orig, dest, type, spotsLeft, host, time);
    if (!this.rides.contains(newRide)) {
      newRide.setRideID(this.lastID);
      this.lastID++;
      this.rides.add(newRide);
    }
  }

  /**
   * This is a helper method for POST spark requests, that can parse out their data and get fields
   * as a list of Strings
   * @param request
   * @return a list of strings that have the POST form contents
   */

  public List<String> parseFormData(Request request) {

    DiskFileItemFactory factory = new DiskFileItemFactory();

    // Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);
    List<String> toRet = new ArrayList<>();
    // Parse the request
    try {
      List<FileItem> items = upload.parseRequest(request.raw());

      for (FileItem item : items) {
        if (item.isFormField()) {
          String stringRep = item.getString();
          toRet.add(stringRep);
        }
      }
    } catch (FileUploadException e) {
      System.out.println("Form parsing error");
    }
    return toRet;
  }

  /**
   *
   * @param input string for the input
   * @return an enum for the according ride, if it exists
   */
  public RideType parseRideType(String input) {
    return switch (input.toLowerCase()) {
      case "driver" -> RideType.DRIVER;
      case "taxi" -> RideType.TAXI;
      default -> null;
    };
  }

  /**
   * Gets the ride provided its id
   * @param id id of the ride
   * @return ride of the corresponding id
   */
  public Ride getRideByID(int id) {
    for (Ride checkRide : this.rides) {
      if (checkRide.idMatch(id) && checkRide.hasSpace()) {
        return checkRide;
      }
    }
    return null;
  }

  /**
   * Filtering the whole database from the highest to lowest scores
   */
  public void filterByScore() {
    ScoreCompare compare = new ScoreCompare();
    Collections.sort(this.rides, compare);
  }

  /**
   * Filtering the whole database against the remembered score of the current user
   */

  public void filterByScoreUser() {
    Collections.sort(this.rides, Comparator.comparingInt(ride ->
        Math.abs(ride.getRideScore() - this.currentUserScore)));
  }

  /**
   * Filtering the whole database based on date, from past to future
   */
  public void filterByDate() {
    DateCompare compare = new DateCompare();
    Collections.sort(this.rides, compare);

  }

  /**
   * Filtering the entire database, based on the proximity to the date of the pending ride
   */

  public void filterByDateUser() {
    Collections.sort(this.rides, Comparator.comparingLong(ride ->
        Math.abs(ride.getTime().getTime() - this.pending.getTime().getTime())));
  }

  /**
   * Filtering the whole database based on distance
   */
  public void filterByDistance() {
    DistanceCompare compare = new DistanceCompare();
    Collections.sort(this.rides, compare);
  }

  /**
   * Filter by distance based on the proximity to the pending ride
   */
  public void filterByDistanceUser() {
    Collections.sort(this.rides, Comparator.comparingDouble(ride -> Math.abs(
        this.pending.getOrigin().compareDistance(ride.getOrigin()) +
            this.pending.getDestination().compareDistance(ride.getDestination()))));
  }

  /**
   * Filter by rideID, lowest to highest
   */
  public void filterByID() {
    IDCompare compare = new IDCompare();
    Collections.sort(this.rides, compare);
  }

  /**
   * Handler method that decided which filter to call
   * @param mode string as a key to the filter
   * @return true if the key was recognized, false otherwise
   */

  public boolean handleFilter(String mode) {
    return switch (mode) {
      case "score" -> {
        if (this.hasCurrentUser()) {
          filterByScoreUser();
        } else {
          filterByScore();
        }
        yield true;
      }
      case "time" -> {
        if (this.hasPending()) {
          filterByDateUser();
        } else {
          filterByDate();
        }
        yield true;
      }
      case "distance" -> {
        if (this.hasPending()) {
          filterByDistanceUser();
        } else {
          filterByDistance();
        }
        yield true;
      }
      case "id" -> {
        filterByID();
        yield true;
      }

      default -> false;
    };
  }

  /**
   * @return true if there are no rides in the database
   */
  public boolean isEmpty() {
    return this.rides.isEmpty();
  }

  /**
   * @param ride to set to be the pending ride
   */
  public void setPending(Ride ride) {
    this.pending = ride;
  }

  /**
   * @param guest to set the current user to
   */
  public void setCurrentUser(Guest guest) {
    this.currentUser = guest;
  }

  /**
   * @return current user
   */
  public Guest getCurrentUser() {return this.currentUser;}

  /**
   * @return the score of the current user
   */

  public int getCurrentUserScore() {return this.currentUserScore;}

  /**
   * @param score target score to set the current field to
   */

  public void setCurrentUserScore(int score) {this.currentUserScore = score;};

  /**
   *
   * @return true if the database currently has a pending ride
   */

  public boolean hasPending() {
    return (this.pending != null);
  }

  /**
   * @return true if the database currently has a user
   */
  public boolean hasCurrentUser() {return (this.currentUser != null);}

  /**
   * @return current pending ride
   */
  public Ride getPending() {
    return this.pending;
  }

  /**
   *
   * @return string representation of the database
   */

  @Override
  public String toString() {
    String toRet = "";
    for (Ride ride : this.rides) {
      toRet = toRet + ride.toString() + " \n";
    }
    return toRet;
  }

}
