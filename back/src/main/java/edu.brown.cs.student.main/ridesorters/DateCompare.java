package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

public class DateCompare implements Comparator<Ride> {

  @Override
  public int compare(Ride ride1, Ride ride2) {

    return Long.compare(ride1.getTime().getTime(), ride2.getTime().getTime()); //potentially -1 *
  }

}