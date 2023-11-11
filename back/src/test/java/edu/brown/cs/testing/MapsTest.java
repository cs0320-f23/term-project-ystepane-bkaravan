package edu.brown.cs.testing;


import com.beust.ah.A;
import edu.brown.cs.student.main.handler.BoundBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class MapsTest {
  BoundBox box = new BoundBox();


  // UNIT TESTING //

  /**
   * test for valid file absolute path
   */
  @Test
  public void validFile(){
    String file ="C:\\Users\\karav\\OneDrive\\Documents\\CS32\\maps-bkaravan-ibrauns\\back\\data\\csvtest\\duplicate.csv";
    assertTrue(box.isFileValid(file));
  }

  /**
   * test for good file
   */
  @Test
  public void validFileRel(){
    String file ="data/stars/ten-star.csv";
    assertTrue(box.isFileValid(file));
  }

  /**
   * test for bad file
   */
  @Test
  public void invalidFile(){
    String file ="data/stars/nofile.csv";
    assertFalse(box.isFileValid(file));
  }

  /**
   * Test for validCoordinates
   */
  @Test
  public void validCoord(){
    String lat = "10";
    String lon = "50";
    String step = "3";
    assertTrue(box.isCoordValid(lat,lon,step));
  }

  /**
   * Test that every permutation of wrong coordinates is false
   */
  @Test
  public void invalidCoord(){
    String lat = "-100";
    String lon = "500";
    String step = "3";
    assertFalse(box.isCoordValid(lat,lon,step));

    String lat1 = "-100";
    String lon1 = "0";
    String step1 = "3";
    assertFalse(box.isCoordValid(lat1,lon1,step1));

    String lat2 = "0";
    String lon2 = "181";
    String step2 = "3";
    assertFalse(box.isCoordValid(lat2,lon2,step2));

    String lat3 = "0";
    String lon3 = "170";
    String step3 = "-1";
    assertFalse(box.isCoordValid(lat3,lon3,step3));

    String lat4 = "0";
    String lon4 = "170";
    String step4 = "20";
    assertFalse(box.isCoordValid(lat4,lon4,step4));
  }

  /**
   * Test for checking that things are properly in bounds
   */
  @Test
  public void isInBounds(){
    box.isCoordValid("10.0", "50.0", "3.0");
    float x = Float.parseFloat("11.0");
    float y = Float.parseFloat("52.0");
    assertTrue(box.isInBounds(x,y));

    box.isCoordValid("-20.0", "130.0", "14.2");
    float x1 = Float.parseFloat("-11.4");
    float y1 = Float.parseFloat("137.8");
    assertTrue(box.isInBounds(x1,y1));
  }

  /**
   * Test for checking that every permutation of coordinates not in bounds stays false
   */

  @Test
  public void isNotInBounds(){
    box.isCoordValid("10.0", "50.0", "3.0");
    float x = Float.parseFloat("16.0");
    float y = Float.parseFloat("52.0");
    assertFalse(box.isInBounds(x,y));

    box.isCoordValid("10.0", "50.0", "3.0");
    float x1 = Float.parseFloat("11.0");
    float y1 = Float.parseFloat("70.0");
    assertFalse(box.isInBounds(x1,y1));

    box.isCoordValid("10.0", "50.0", "3.0");
    float x2 = Float.parseFloat("16.0");
    float y2 = Float.parseFloat("42.0");
    assertFalse(box.isInBounds(x2,y2));
  }

  // Random Testing //
int ITERATIONS = 1000;
int PAIRS = 500;

  /**
   * FuzzTest to never crash
   */

  @Test
  public void crushTest() {
    for (int i = 0; i < ITERATIONS; i++) {
      List<List<Float>> testList = new ArrayList<>();
      Random rand = new Random();
      for (int j = 0; j < PAIRS; j++) {
        ArrayList<Float> pair = new ArrayList<>();
        pair.add((rand.nextFloat() * 300) - 150);
        pair.add((rand.nextFloat() * 300) - 150);
        testList.add(pair);
      }
      // if we run this many times, it should not crash
      box.filterBounds(testList);
    }
  }

  /**
   * Propertybased test that checks that everyting in the right range should return true
   */

  @Test
  public void propertyTrueCrushTest() {
    // set up the range of coords from -40 to 40
    box.isCoordValid("-41", "-41", "82");
    for (int i = 0; i < ITERATIONS; i++) {
      List<List<Float>> testList = new ArrayList<>();
      Random rand = new Random();
      for (int j = 0; j < PAIRS; j++) {
        ArrayList<Float> pair = new ArrayList<>();
        // every pair should be in the above range
        pair.add((rand.nextFloat() * 80) - 40);
        pair.add((rand.nextFloat() * 80) - 40);
        testList.add(pair);
      }
      //should return true every time
      assertTrue(box.filterBounds(testList));
    }
  }

  /**
   * property based test to check that every coordinate in the wrong range should return False
   *
   */

  @Test
  public void propertyFalseCrushTest() {
    // only want negative coordinates
    box.isCoordValid("-41", "-41", "20");
    for (int i = 0; i < ITERATIONS; i++) {
      List<List<Float>> testList = new ArrayList<>();
      Random rand = new Random();
      for (int j = 0; j < PAIRS; j++) {
        ArrayList<Float> pair = new ArrayList<>();
        // every pair has positive coordinates
        pair.add((rand.nextFloat() * 80));
        pair.add((rand.nextFloat() * 80));
        testList.add(pair);
//        System.out.println(pair);
      }
      // so everything should be false
      assertFalse(box.filterBounds(testList));
    }
  }
}