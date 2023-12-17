package edu.brown.cs.student.main.ridesorters;

import edu.brown.cs.student.main.rideshare.Ride;
import java.util.Comparator;

public class ScoreCompare implements Comparator<Ride> {

  @Override
  public int compare(Ride ride1, Ride ride2) {
    return -1 * Integer.compare(ride1.getRideScore(), ride2.getRideScore());
  }

}