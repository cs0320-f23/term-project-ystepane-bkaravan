package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

public class DistanceCompare implements Comparator<Ride> {

  @Override
  public int compare(Ride ride1, Ride ride2) {

    return Double.compare(ride1.getDistance(), ride2.getDistance()); //potentially -1 *
  }

}
