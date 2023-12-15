package edu.brown.cs.student.main;

import static spark.Spark.after;

import edu.brown.cs.student.main.handler.AreaSearch;
import edu.brown.cs.student.main.handler.BoundBox;

import edu.brown.cs.student.main.handler.DatabaseStart;
import edu.brown.cs.student.main.handler.DateSubmit;
import edu.brown.cs.student.main.handler.HandleCreate;
import edu.brown.cs.student.main.handler.HandleJoin;
import edu.brown.cs.student.main.handler.LoadCensusHandler;
//import edu.brown.cs.student.main.handler.LoadData;
import edu.brown.cs.student.main.handler.LoadHandler;
import edu.brown.cs.student.main.handler.ReloadHandler;
import edu.brown.cs.student.main.handler.SearchHandler;
import edu.brown.cs.student.main.handler.ViewHandler;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.rideshare.City;
import edu.brown.cs.student.main.rideshare.Database;
import edu.brown.cs.student.main.rideshare.Guest;
import edu.brown.cs.student.main.rideshare.Ride;
import edu.brown.cs.student.main.rideshare.RideType;
import edu.brown.cs.student.main.server.Storage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import spark.Spark;

/**
 * The Main class of our project. This is where execution begins.
 */
public class Main {

  private ArrayList<String> endpoints;

  public static class GeoResponse {
  }

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

    // our handlers
    Database testdata = new Database();
    populateDb(testdata);
    Storage csvStorage = new Storage();
    Spark.get("loadcsv", new LoadHandler(csvStorage));
    Spark.get("viewcsv", new ViewHandler(csvStorage));
    Spark.get("searchcsv", new SearchHandler(csvStorage));
    Spark.get("broadband", new LoadCensusHandler(csvStorage));
    Spark.get("reload", new ReloadHandler(csvStorage));
    Spark.get("boundbox", new BoundBox());
    Spark.get("areasearch", new AreaSearch());
    Spark.get("startdb", new DatabaseStart(testdata));
    Spark.post("/dateform", new DateSubmit(testdata));
    Spark.get("createRide", new HandleCreate(testdata));
    Spark.get("joinRide", new HandleJoin(testdata));


    Spark.init();
    Spark.awaitInitialization();
    System.out.println("running");

    System.out.println("Server started at http://localhost:" + port);
  }

  public static void populateDb(Database db) {
    City orig1 = new City("BostonTest",10.0, 10.0, true);
    City dest1 = new City("ProvTest", -25.0, 32.0, false);
    Guest hostTest = new Guest("Julia", "phnoe", "email");
    Guest hostTest2 = new Guest("Juliasika", "phnoe", "email2");
    db.createRide(orig1, dest1, RideType.DRIVER, 3, hostTest);
    Ride testRide = new Ride(orig1, dest1, RideType.TAXI, 2, hostTest2);
    db.addRide(testRide);
    //System.out.println(db);
    Guest testGuest = new Guest("Bohdan", "phony", "email");
    db.joinRide(testGuest, testRide);
    //System.out.println(db);
  }

  Main(String[] args) {
  }

}