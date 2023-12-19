package edu.brown.cs.student.main.rideshare;

/**
 * This is a record that defines a city
 * @param name
 * @param lat
 * @param lon
 * @param isOrigin
 */
public record City(String name, Double lat, Double lon, Boolean isOrigin) {
  public String getName() {
    return this.name;
  }
  public Double getLat() {return this.lat;}
  public Double getLon() {return this.lon;}

  /**
   * This helper method is used in sorting when we need to get the distance from another city
   * @param other City to get the distance
   * @return distance as a double
   */
  public Double compareDistance(City other) {
    double d1 = Math.pow((this.lat - other.getLat()), 2);
    double d2 = Math.pow((this.lon - other.getLon()), 2);
    return Math.sqrt(d1 + d2);
  }

}
