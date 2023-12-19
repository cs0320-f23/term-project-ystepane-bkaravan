package edu.brown.cs.student.main.travelbuddyhandler;

import edu.brown.cs.student.main.rideshare.City;
import edu.brown.cs.student.main.rideshare.Database;

import edu.brown.cs.student.main.rideshare.Ride;

import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is a class than handles a POST request that covers the map part of the project - we are
 * getting the coordinate and names of origin and destination and the time of the ride
 */
public class DateSubmit implements Route {

  private final Database base;

  /**
   * Constructor and our database
   * @param base
   */
  public DateSubmit(Database base) {
    this.base = base;
  }

  /**
   * handle method that parses the data from the post request and creates a "pending ride" for the
   * database
   * @param request
   * @param response
   * @return null
   */
  @Override
  public Object handle(Request request, Response response) {

    ArrayList<String> test1 = new ArrayList<>(this.base.parseFormData(request));
    String timeS = test1.get(0);
    String dateAndTime = timeS.substring(0, 10) + " " + timeS.substring(11);
    System.out.println(dateAndTime);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date newDate = null;
    try {
      newDate = dateFormat.parse(dateAndTime);
    } catch (ParseException e) {
      System.out.println(e);
    }

    City origin = new City(test1.get(1), Double.parseDouble(test1.get(2)), Double.parseDouble(test1.get(3)), true);
    City dest = new City(test1.get(4), Double.parseDouble(test1.get(5)), Double.parseDouble(test1.get(6)), false);
    Ride pending = new Ride(origin, dest, newDate);
    this.base.setPending(pending);

    return null;
  }

}
