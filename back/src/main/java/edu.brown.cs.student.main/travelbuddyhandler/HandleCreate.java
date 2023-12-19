package edu.brown.cs.student.main.travelbuddyhandler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;
import edu.brown.cs.student.main.rideshare.Ride;
import edu.brown.cs.student.main.rideshare.RideType;
import edu.brown.cs.student.main.ridesorters.DateAdapter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * A class that handles the create request, if there is a pending ride and enough information from
 * the user, creates the ride and adds it to the database
 */

public class HandleCreate implements Route {

  private final Database base;

  public HandleCreate(Database base) {
    this.base = base;
  }

  /**
   * Handle method that performs error checking and creates a new ride from a pending ride, if
   * possible
   * @param request
   * @param response
   * @return a database with an added ride or an error message, both as a json
   */

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().add(new DateAdapter()).build();
    Type mapStringObj = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObj);

    Map<String, Object> responseMap = new HashMap<>();

    if (!this.base.hasPending()) {
      responseMap.put("error", "Please create a pending ride first");
      return adapter.toJson(responseMap);
    }

    if (!this.base.hasCurrentUser()) {
      responseMap.put("error", "Please save your information before creating a ride");
      return adapter.toJson(responseMap);
    }

    String spotsLeft = request.queryParams("spots");
    String type = request.queryParams("type");
    int newSpots;

    try {
      newSpots = Integer.parseInt(spotsLeft);
      RideType newType = this.base.parseRideType(type);
      if (newType == null) {
        responseMap.put("error", "Expected driver or taxi for ride type");
        return adapter.toJson(responseMap);
      }
      Guest newHost = this.base.getCurrentUser();
      Ride pendingRide = this.base.getPending();
      pendingRide.adjustRide(newHost, newSpots, newType, this.base.getCurrentUserScore()); // we can adjust the points of the ride here

      this.base.delPending();
      this.base.setCurrentUserScore(0); // double check these
      this.base.setCurrentUser(null);
      this.base.addRide(pendingRide);

      responseMap.put("database", this.base);
      return adapter.toJson(responseMap);

    } catch (NumberFormatException e ) {
      responseMap.put("error", "Please provide the number of spots in the ride");
      return adapter.toJson(responseMap);
    }

  }

}
