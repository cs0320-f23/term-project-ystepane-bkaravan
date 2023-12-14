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

public class HandleCreate implements Route {

  private final Database base;

  public HandleCreate(Database base) {
    this.base = base;
  }

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObj = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObj);

    Map<String, Object> responseMap = new HashMap<>();

    if (!this.base.hasPending()) {
      responseMap.put("error", "No pending rides");
      return adapter.toJson(responseMap);
    }

    String name = request.queryParams("name");
    String phone = request.queryParams("phone");
    String email = request.queryParams("email");
    String spotsLeft = request.queryParams("spots");
    String type = request.queryParams("type");

    if (name == null || phone == null || email == null) {
      responseMap.put("error", "insufficient parameters");
      return adapter.toJson(responseMap);
    }

    Guest newHost = new Guest(name, phone, email);

    Ride pendingRide = this.base.getPending();
    pendingRide.adjustRide(newHost, 4, RideType.DRIVER);

    this.base.setPending(null);
    this.base.addRide(pendingRide);
    responseMap.put("database", this.base);
    return adapter.toJson(responseMap);
  }

}
