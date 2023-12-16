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

public class UserSubmit implements Route {

  private final Database base;

  public UserSubmit(Database base) {
    this.base = base;
  }

  @Override
  public Object handle(Request request, Response response) {
//    String time = parseFormData(request.body());
    ArrayList<String> formData = new ArrayList<>(this.base.parseFormData(request));
    //System.out.println(formData);
    Guest currentUser = new Guest(formData.get(0), formData.get(1), formData.get(2));
    Integer currentUserScore = Integer.parseInt(formData.get(3)) + Integer.parseInt(formData.get(4)) + Integer.parseInt(formData.get(5));
//    System.out.println(currentUserScore);
//    System.out.println(currentUser);
    this.base.setCurrentUser(currentUser);

//    System.out.println(test1.get(5).isEmpty());
//    System.out.println(test1);
    return null;
  }

}
