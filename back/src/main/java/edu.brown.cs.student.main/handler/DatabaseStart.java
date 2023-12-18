package edu.brown.cs.student.main.handler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.ridesorters.DateAdapter;
import spark.Request;
import spark.Response;
import spark.Route;


public class DatabaseStart implements Route {
  private final Database base;

  public DatabaseStart(Database base) {
    this.base = base;
  }


  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().add(new DateAdapter()).build();
    JsonAdapter<Database> jsonAdapter = moshi.adapter(Database.class);

    return jsonAdapter.toJson(this.base);
  }
}
