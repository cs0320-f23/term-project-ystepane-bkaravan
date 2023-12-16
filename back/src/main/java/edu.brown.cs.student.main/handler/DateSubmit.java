package edu.brown.cs.student.main.handler;

import edu.brown.cs.student.main.rideshare.City;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;
import edu.brown.cs.student.main.rideshare.Ride;
import edu.brown.cs.student.main.rideshare.RideType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import spark.Request;
import spark.Response;
import spark.Route;

public class DateSubmit implements Route {

  private final Database base;

  public DateSubmit(Database base) {
    this.base = base;
  }

  @Override
  public Object handle(Request request, Response response) {
//    String time = parseFormData(request.body());
    String test = request.contentType();
    ArrayList<String> test1 = new ArrayList<>(this.base.parseFormData(request));
    String timeS = test1.get(0);
    String dateAndTime = timeS.substring(0, 10) + " " + timeS.substring(11);
    //System.out.println(dateAndTime);
    City origin = new City(test1.get(1), Double.parseDouble(test1.get(2)), Double.parseDouble(test1.get(3)), true);
    City dest = new City(test1.get(4), Double.parseDouble(test1.get(5)), Double.parseDouble(test1.get(6)), false);
//    Guest guest1 = new Guest ("Bogdasha", "1234556", "bogdasha@email.com");
//    Ride testRide = new Ride(origin, dest, RideType.TAXI, 4, guest1);
    Ride pending = new Ride(origin, dest, dateAndTime);
    this.base.setPending(pending);
//    System.out.println(test1.get(5).isEmpty());
//    System.out.println(test1);
    return null;
  }

}
