package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

/**
 * Simple class that shows how to compare time
 */
public class DateCompare implements Comparator<Ride> {

  /**
   * Time is recorded as a Date class, which is why getTime is called twice (once to get the field
   * of the ride, another to get time as a long)
   * @param ride1 the first object to be compared.
   * @param ride2 the second object to be compared.
   * @return
   */
  @Override
  public int compare(Ride ride1, Ride ride2) {

    return Long.compare(ride1.getTime().getTime(), ride2.getTime().getTime()); //potentially -1 *
  }

}