package edu.brown.cs.student.main.rideshare;

public record City(String name, Double lat, Double lon, Boolean isOrigin) {
  public String getName() {
    return this.name;
  }

}
