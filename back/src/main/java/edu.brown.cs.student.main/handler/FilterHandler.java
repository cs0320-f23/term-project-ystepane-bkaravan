package edu.brown.cs.student.main.handler;

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

public class
FilterHandler implements Route {
  private final Database base;

  public FilterHandler(Database base) {
    this.base = base;
  }

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
      responseMap.put("error", "Please provide a valid filter: score, date, or distance");
      return adapter.toJson(responseMap);
    }

    responseMap.put("database", this.base);

    return adapter.toJson(responseMap);
  }

}
