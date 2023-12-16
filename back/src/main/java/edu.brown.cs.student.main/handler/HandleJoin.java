package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;
import edu.brown.cs.student.main.rideshare.Ride;
import edu.brown.cs.student.main.rideshare.RideType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class HandleJoin implements Route {

  private final Database base;

  public HandleJoin(Database base) {
    this.base = base;
  }

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObj = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObj);
    Map<String, Object> responseMap = new HashMap<>();

    if (this.base.isEmpty()) {
      responseMap.put("error", "No rides to join");
      return adapter.toJson(responseMap);
    }
    String rideID = request.queryParams("id");

    if (rideID == null) {
      responseMap.put("error", "No ID provided");
      return adapter.toJson(responseMap);
    }

    int joinID;

    try {
      joinID = Integer.parseInt(rideID);
      if (this.base.hasCurrentUser()) {
        //join the current user with the inputID
        Ride potential = this.base.getRideByID(joinID);
        if (potential != null) {
          potential.addGuest(this.base.getCurrentUser());
          this.base.setCurrentUser(null);
        } else {
          responseMap.put("error", "ID not found or the ride is full");
          return adapter.toJson(responseMap);
        }
      } else {
        responseMap.put("error", "save your information before joining");
        return adapter.toJson(responseMap);
      }
      responseMap.put("database", this.base);
      return adapter.toJson(responseMap);
    } catch (NumberFormatException e) {
      responseMap.put("error", "RideID must be an integer");
      return adapter.toJson(responseMap);
    }
  }

}
