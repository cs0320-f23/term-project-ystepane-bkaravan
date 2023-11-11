package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class handles the boundbox queries
 */

public class BoundBox implements Route {
  //private FeatureCollection collection;
  private float minLat;
  private float minLon;
  private float maxLat;
  private float maxLon;
  private float stepF;

  public BoundBox() {
//    this.collection = collection;
  }

  /**
   * The handle method checks for the validity of the required fields and then performs filtering
   * based on coordinates
   *
   * @param request
   * @param response
   * @return serialized json file to the server
   */
  @Override
  public Object handle(Request request, Response response) {
    String file = request.queryParams("filepath");
    String minLat = request.queryParams("minlat");
    String minLon = request.queryParams("minlon");
    String step = request.queryParams("step");

    if (file == null || file.isEmpty() || !isFileValid(file)) {
      return new LoadHandler.FailureResponse("Error: Invalid or empty file name").serialize();
    }
    if (minLat == null || minLon == null || step == null) {
      return new LoadHandler.FailureResponse("Error: missing parameters. Expected parameters: minlat, minlon, step").serialize();
    }
    if (!isCoordValid(minLat, minLon, step)) {
      return new LoadHandler.FailureResponse("Error: wrong params. Check bounds for lat, long, and step").serialize();
    }

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeatureCollection> jsonAdapter = moshi.adapter(FeatureCollection.class);

    try {
      Path filePath = Paths.get(file);
      String json = Files.readString(filePath);
      if (this.stepF == 0.0) {
        return json;
      }

      FeatureCollection data = jsonAdapter.fromJson(json);
      assert(data != null);
      List<Features> fillArray = new ArrayList<>();

      for (Features feat : data.features) {
        if (feat == null || feat.geometry == null) {
          continue;
        }
        Geometry currGeometry = feat.geometry;
        List<List<Float>> coords = currGeometry.coordinates.get(0).get(0);
        if (filterBounds(coords)) {
          fillArray.add(feat);
        }
      }

      FeatureCollection returnData = new FeatureCollection(data.type, fillArray.toArray(new Features[0]));
      return jsonAdapter.toJson(returnData);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * This is a helper method to check if the file path is valid
   * @param fileName file to check
   * @return boolean for validity
   */
  public boolean isFileValid(String fileName) {
    // Check if the file exists and is a valid file (not a directory)
    File file = new File(fileName);
    return file.exists() && file.isFile();
  }

  /**
   * Helper method to check if everyting in the list is in proper bounds
   * @param coords list of pairs to check
   * @return false if even one pair is not in the proper range
   */
  public boolean filterBounds(List<List<Float>> coords) {
    for (List<Float> coordPair : coords) {
      if (!isInBounds(coordPair.get(1), coordPair.get(0))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Helper method to check if the input coordinates are valid; set up the work
   * @param lat lat from input
   * @param lon lon from input
   * @param step step from input
   * @return true if proper bounds, false otherwise
   */
  public boolean isCoordValid(String lat, String lon, String step) {
    try {
      this.minLat = Float.parseFloat(lat);
      this.minLon = Float.parseFloat(lon);
      if (this.minLat < -90.0 || this.minLon < -180.0) {
        return false;
      }
      this.stepF = Float.parseFloat(step);
      if (stepF < 0.0) {
        return false;
      }
      this.maxLat = this.minLat + stepF;
      this.maxLon = this.minLon + stepF;
      return !(this.maxLat > 90.0) && !(this.maxLon > 180.0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Check if x and y from a pair are in proper bounds for the current filter
   * @param x coord of the pair
   * @param y coord of the pair
   * @return true if in bounds, false otherwise
   */
  public boolean isInBounds(float x, float y) {
    return (this.minLat < x) && (this.maxLat > x) && (this.minLon < y) && (this.maxLon > y);
  }

  /**
   * These are helper records to parse the json using moshi
   * @param type
   * @param features
   */
  public record FeatureCollection(String type, Features[] features){}
  public record Features(Geometry geometry, Properties properties, String type){}
  public record Geometry(String type, List<List<List<List<Float>>>> coordinates){}
  public record Properties(AreaDescriptionData area_description_data, String city,
                           String holc_grade, String name){}
  public record AreaDescriptionData(Map<String, String> data){}

}

