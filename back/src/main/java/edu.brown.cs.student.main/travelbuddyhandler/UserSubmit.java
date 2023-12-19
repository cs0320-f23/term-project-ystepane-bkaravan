package edu.brown.cs.student.main.travelbuddyhandler;


import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;

import java.util.ArrayList;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that handles a POST request that has the required information to create an instance of the
 * Guest class
 */

public class UserSubmit implements Route {

  private final Database base;

  public UserSubmit(Database base) {
    this.base = base;
  }

  /**
   * Handle method that gets the information out of the post form and creates a current user if we
   * have enough information
   * @param request
   * @param response
   * @return null
   */
  @Override
  public Object handle(Request request, Response response) {

    ArrayList<String> formData = new ArrayList<>(this.base.parseFormData(request));
    System.out.println(formData);

    Guest currentUser = new Guest(formData.get(0), formData.get(1), formData.get(2));
    Integer currentUserScore = Integer.parseInt(formData.get(3)) + Integer.parseInt(formData.get(4)) + Integer.parseInt(formData.get(5));
    System.out.println(formData);
    this.base.setCurrentUser(currentUser);
    this.base.setCurrentUserScore(currentUserScore);

    return null;
  }

}
