package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.File;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeoHandler implements Route {
  //private FeatureCollection collection;
  private float minLat;
  private float minLon;
  private float maxLat;
  private float maxLon;

  public GeoHandler() {
//    this.collection = collection;
  }

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
    //System.out.println(this.maxLat + " " + this.minLat + " " + this.minLon + " " + this.maxLon);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeatureCollection> jsonAdapter = moshi.adapter(FeatureCollection.class);

    try {
      Path filePath = Paths.get(file);
      String json = Files.readString(filePath);

      FeatureCollection data = jsonAdapter.fromJson(json);
//      assert(data != null);
      Features[] fillArray = new Features[data.features.length];
      FeatureCollection returnData = new FeatureCollection(data.type, fillArray);
      int i = 0;

      for (Features feat : data.features) {
        System.out.println(feat);
        if (feat.geometry == null) {
          continue;
        }
        Geometry currGeometry = feat.geometry;
        List<List<Float>> coords = currGeometry.coordinates.get(0).get(0);
        boolean pass = true;
        for (List<Float> coordPair : coords) {
          if (!isInBounds(coordPair.get(1), coordPair.get(0))) {
            pass = false;
            break;
          }
        }
        if (pass) {
          fillArray[i] = feat;
          i++;
        }
      }


      System.out.println(data);
      System.out.println(data.features[0]);
      System.out.println(data.features[0].geometry);
      System.out.println(data.features[0].properties.area_description_data.data);
      System.out.println(data.features[0].properties.area_description_data);
      System.out.println(data.features[0].properties.area_description_data.data);

//      return this.collection.features[0].properties.area_description_data.data;
      return jsonAdapter.toJson(returnData);
    } catch (Exception e) {
      //e.printStackTrace();
    }
    return null;
  }

  private boolean isFileValid(String fileName) {
    // Check if the file exists and is a valid file (not a directory)
    File file = new File(fileName);
    return file.exists() && file.isFile();
  }

  private boolean isCoordValid(String lat, String lon, String step) {
    try {
      this.minLat = Float.parseFloat(lat);
      this.minLon = Float.parseFloat(lon);
      if (this.minLat < -90.0 || this.minLon < -180.0) {
        return false;
      }
      float stepF = Float.parseFloat(step);
      if (stepF <= 0.0) {
        return false;
      }
      this.maxLat = this.minLat + stepF;
      this.maxLon = this.maxLon + stepF;
      return !(this.maxLat > 90.0) && !(this.maxLon > 180.0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean isInBounds(float x, float y) {
    return (this.minLat < x) && (this.maxLat > x) && (this.minLon < y) && (this.maxLon > y);
  }

  public record FeatureCollection(String type, Features[] features){}
  public record Features(Geometry geometry, Properties properties, String type){}
  public record Geometry(String type, List<List<List<List<Float>>>> coordinates){}
  public record Properties(AreaDescriptionData area_description_data, String city,
                           String holc_grade, String name){}
  public record AreaDescriptionData(Map<String, String> data){}

}
