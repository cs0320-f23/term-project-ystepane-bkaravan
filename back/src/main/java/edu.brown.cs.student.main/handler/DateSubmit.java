package edu.brown.cs.student.main.handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class DateSubmit implements Route {

  @Override
  public Object handle(Request request, Response response) {
//    String time = parseFormData(request.body());
    String example = request.queryParams("pickuptime");
    String example2 = request.body();
//    System.out.println(example);
    System.out.println(example2);
//    System.out.println(time);
    return null;
  }

  private String parseFormData(String formData) {
    // Extract the value of the pickuptime field from the multipart form data
    String[] parts = formData.split("------WebKitFormBoundary[\\w]+--\r\n");
    for (String part : parts) {
      if (part.contains("pickuptime")) {
        // Extract the value after the last occurrence of "\r\n\r\n"
        int startIndex = part.lastIndexOf("\r\n\r\n") + "\r\n\r\n".length();
        return part.substring(startIndex).trim();
      }
    }
    return null;
  }
}
