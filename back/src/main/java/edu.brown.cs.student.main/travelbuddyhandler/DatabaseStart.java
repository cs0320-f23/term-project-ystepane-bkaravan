package edu.brown.cs.student.main.travelbuddyhandler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.ridesorters.DateAdapter;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is a class that handles a simple request of just outputting the Database as a json
 */


public class DatabaseStart implements Route {
  private final Database base;

  /**
   * Constructor
   * @param base the injected base to share between other handlers
   */
  public DatabaseStart(Database base) {
    this.base = base;
  }


  /**
   * Using moshi with our custom DataAdapter to create a Json
   * @param request
   * @param response
   * @return our database as a json
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().add(new DateAdapter()).build();
    JsonAdapter<Database> jsonAdapter = moshi.adapter(Database.class);

    return jsonAdapter.toJson(this.base);
  }
}
