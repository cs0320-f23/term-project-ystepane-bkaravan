package edu.brown.cs.student.main.rideshare;

public record Guest(String name, String number, String email) {
  public String getName() {
    return this.name;
  }
}
