package edu.brown.cs.student.main.travelbuddyhandler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.ridesorters.DateAdapter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is a get endpoint that handles the request for filtering the database
 */

public class FilterHandler implements Route {
  private final Database base;

  /**
   * Constructor with our database
   * @param base
   */
  public FilterHandler(Database base) {
    this.base = base;
  }

  /**
   * Handle method that checks proper arguments and sends the updated database to the front
   * @param request
   * @param response
   * @return
   */
  @Override
  public Object handle(Request request, Response response) {

    Moshi moshi = new Moshi.Builder().add(new DateAdapter()).build();
    Type mapStringObj = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObj);

    Map<String, Object> responseMap = new HashMap<>();

    String mode = request.queryParams("param");

    if (mode == null) {
      responseMap.put("error", "Provide filtering parameter");
      return adapter.toJson(responseMap);
    }

    if (!this.base.handleFilter(mode.toLowerCase())) {
      responseMap.put("error", "Please provide a valid filter: score, time, id, or distance");
      return adapter.toJson(responseMap);
    }

    responseMap.put("database", this.base);

    return adapter.toJson(responseMap);
  }

}
