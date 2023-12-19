package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

/**
 * Simple class to set the sorting algorithm for collections
 */
public class ScoreCompare implements Comparator<Ride> {

  /**
   * Compare the ridescores and * -1 to be in descending order
   * @param ride1 the first object to be compared.
   * @param ride2 the second object to be compared.
   * @return
   */
  @Override
  public int compare(Ride ride1, Ride ride2) {
    return -1 * Integer.compare(ride1.getRideScore(), ride2.getRideScore());
  }

}
