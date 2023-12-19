package edu.brown.cs.student.main.ridesorters;
import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

/**
 * Simple class to show how compare ids
 */
public class IDCompare implements Comparator<Ride> {

  /**
   * Compare IDs as integers
   * @param ride1 the first object to be compared.
   * @param ride2 the second object to be compared.
   * @return
   */
  @Override
  public int compare(Ride ride1, Ride ride2) {
    return Integer.compare(ride1.getRideID(), ride2.getRideID());
  }

}
