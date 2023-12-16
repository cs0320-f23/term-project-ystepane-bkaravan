package edu.brown.cs.student.main.rideshare;

public record City(String name, Double lat, Double lon, Boolean isOrigin) {
  public String getName() {
    return this.name;
  }
  public Double getLat() {return this.lat;}
  public Double getLon() {return this.lon;}

  public Double compareDistance(City other) {
    double d1 = Math.pow((this.lat - other.getLat()), 2);
    double d2 = Math.pow((this.lon - other.getLon()), 2);
    return Math.sqrt(d1 + d2);
  }

}
