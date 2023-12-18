package edu.brown.cs.student.main.ridesorters;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  @ToJson
  String dateToJson(Date date) {
    return dateFormat.format(date);
  }

  @FromJson
  Date dateFromJson(String dateString) throws ParseException {
    return dateFormat.parse(dateString);
  }
}