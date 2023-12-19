package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

/**
 * Simple class that shows how to compare distance
 */
public class DistanceCompare implements Comparator<Ride> {

  /**
   * Get the distance of every ride and compare them
   * @param ride1 the first object to be compared.
   * @param ride2 the second object to be compared.
   * @return
   */
  @Override
  public int compare(Ride ride1, Ride ride2) {

    return Double.compare(ride1.getDistance(), ride2.getDistance()); //potentially -1 *
  }

}
