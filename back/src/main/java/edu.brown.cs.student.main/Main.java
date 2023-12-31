package edu.brown.cs.student.main;

import static spark.Spark.after;

import edu.brown.cs.student.main.handler.AreaSearch;
import edu.brown.cs.student.main.handler.BoundBox;

import edu.brown.cs.student.main.travelbuddyhandler.DatabaseStart;
import edu.brown.cs.student.main.travelbuddyhandler.DateSubmit;
import edu.brown.cs.student.main.travelbuddyhandler.FilterHandler;
import edu.brown.cs.student.main.travelbuddyhandler.HandleCreate;
import edu.brown.cs.student.main.travelbuddyhandler.HandleJoin;
import edu.brown.cs.student.main.handler.LoadCensusHandler;
//import edu.brown.cs.student.main.handler.LoadData;
import edu.brown.cs.student.main.handler.LoadHandler;
import edu.brown.cs.student.main.handler.ReloadHandler;
import edu.brown.cs.student.main.handler.SearchHandler;
import edu.brown.cs.student.main.travelbuddyhandler.UserSubmit;
import edu.brown.cs.student.main.handler.ViewHandler;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.rideshare.City;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;
import edu.brown.cs.student.main.rideshare.Ride;
import edu.brown.cs.student.main.rideshare.RideType;
import edu.brown.cs.student.main.server.Storage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import spark.Spark;

/**
 * The Main class of our project. This is where execution begins.
 */
public class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */



  public static void main(String[] args) throws IOException, FactoryFailureException {

    int port = 2020;
    Spark.port(port);
    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });

    Storage csvStorage = new Storage();
    Spark.get("loadcsv", new LoadHandler(csvStorage));
    Spark.get("viewcsv", new ViewHandler(csvStorage));
    Spark.get("searchcsv", new SearchHandler(csvStorage));
    Spark.get("broadband", new LoadCensusHandler(csvStorage));
    Spark.get("reload", new ReloadHandler(csvStorage));
    Spark.get("boundbox", new BoundBox());
    Spark.get("areasearch", new AreaSearch());


    // THINGS FOR TERM PROJECT BELOW
    // our handlers
    Database testdata = new Database();
    populateDb(testdata);
    System.out.println(testdata);
    Spark.get("startdb", new DatabaseStart(testdata));
    Spark.post("/dateform", new DateSubmit(testdata));
    Spark.post("/userform", new UserSubmit(testdata));
    Spark.get("createRide", new HandleCreate(testdata));
    Spark.get("joinRide", new HandleJoin(testdata));
    Spark.get("filterRide", new FilterHandler(testdata));


    Spark.init();
    Spark.awaitInitialization();
    System.out.println("running");

    System.out.println("Server started at http://localhost:" + port);
  }

  /**
   * Helper method that populates our injected database with some mock data to output on the front
   * @param db, the database to fill
   */
  public static void populateDb(Database db) {
    City orig1 = new City("BostonTest",10.0, 10.0, true);
    City dest1 = new City("ProvTest", -25.0, 32.0, false);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date newDate1 = null;
    Date newDate2 = null;
    try {
      newDate1 = dateFormat.parse("2023-12-18 13:45");
      newDate2 = dateFormat.parse("2024-01-13 08:30");
    } catch (
        ParseException e) {
      System.out.println(e);
    }

    Guest hostTest = new Guest("Julia", "4134556774", "sample_email@brown.edu");
    Guest hostTest2 = new Guest("Kevin Mighty", "9014569434", "cool_email2@brown.edu");
    db.createRide(orig1, dest1, RideType.DRIVER, 3, hostTest, newDate2);
    Ride testRide = new Ride(orig1, dest1, RideType.TAXI, 2, hostTest2, newDate1);
    db.addRide(testRide);
    //System.out.println(db);
    Guest testGuest = new Guest("Bohdan", "phony", "email");
    db.joinRide(testGuest, testRide);
    //System.out.println(db);
  }

  Main(String[] args) {
  }

}