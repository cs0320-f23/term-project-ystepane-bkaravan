package edu.brown.cs.student.main.rideshare;

/**
 * This is a record that keeps track of the user information
 * @param name
 * @param number
 * @param email
 */

public record Guest(String name, String number, String email) {
  public String getName() {
    return this.name;
  }
}
