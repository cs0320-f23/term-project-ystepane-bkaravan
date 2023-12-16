package edu.brown.cs.student.main.rideshare;

import edu.brown.cs.student.main.ridesorters.ScoreCompare;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import spark.Request;

public class Database {

  private final List<Ride> rides;
  private Ride pending;
  private int lastID = 0;

  private Guest currentUser;
  private int currentUserScore;

  public Database() {
    this.rides = new ArrayList<>();
    this.pending = null;
    this.currentUser = null;
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
        checkRide.addGuest(guest, 0);
      }
    }
  }

//  public void joinByID(Guest guest, int id) {
//    for (Ride checkRide : this.rides) {
//      if (checkRide.idMatch(id)) {
//        checkRide.addGuest(guest, 0);
//        return;
//      }
//    }
//  }

  // add time
  public void createRide(City orig, City dest, RideType type, int spotsLeft, Guest host, String time) {
    Ride newRide = new Ride(orig, dest, type, spotsLeft, host, time);
    if (!this.rides.contains(newRide)) {
      newRide.setRideID(this.lastID);
      this.lastID++;
      this.rides.add(newRide);
    }
  }

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
          //System.out.println("Current item " + stringRep);
          toRet.add(stringRep);
        }
      }
    } catch (FileUploadException e) {
      System.out.println("oh oh");
    }
    return toRet;
  }

  public RideType parseRideType(String input) {
    return switch (input.toLowerCase()) {
      case "driver" -> RideType.DRIVER;
      case "taxi" -> RideType.TAXI;
      default -> null;
    };
  }

  public Ride getRideByID(int id) {
    for (Ride checkRide : this.rides) {
      if (checkRide.idMatch(id) && checkRide.hasSpace()) {
        return checkRide;
      }
    }
    return null;
  }

  public void filterByScore() {
    ScoreCompare compare = new ScoreCompare();
    Collections.sort(this.rides, compare);
  }

  public void filterByScoreUser() {

  }

  public void filterByDate() {

  }
  public void filterByDateUser() {

  }

  public void filterByDistance() {

  }

  public void filterByDistanceUser() {

  }



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
      default -> false;
    };
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

  public void setCurrentUser(Guest guest) {
    this.currentUser = guest;
  }
  public Guest getCurrentUser() {return this.currentUser;}
  public int getCurrentUserScore() {return this.currentUserScore;}
  public void setCurrentUserScore(int score) {this.currentUserScore = score;};

  public boolean hasPending() {
    return (this.pending != null);
  }
  public boolean hasCurrentUser() {return (this.currentUser != null);}

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
