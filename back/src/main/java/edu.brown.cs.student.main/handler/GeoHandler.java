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
  public GeoHandler() {
//    this.collection = collection;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String file = request.queryParams("filepath");

    if (file == null || file.isEmpty() || !isFileValid(file)) {
      return new LoadHandler.FailureResponse("Error: Invalid or empty file name").serialize();
    }

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeatureCollection> jsonAdapter = moshi.adapter(FeatureCollection.class);

    try {
      Path filePath = Paths.get(file);
      String json = Files.readString(filePath);

      FeatureCollection data = jsonAdapter.fromJson(json);

      // create FeatureCollectiono returnData = it will hold our returning things
      // we loop through an element of data, where we can get coordinates. Then, we check every coordinate pair and at every pair,
      // we have a choice: if the pair is inside our range, we just keep going, if it's not, we don't add it to returnData.
      // once we added everything, we can call jsonAdapter.toJson(returnData) and it should show up on the website

      // Now 'data' contains your parsed FeatureCollection
      System.out.println(data);
      System.out.println(data.features[0]);
      System.out.println(data.features[0].geometry);
      System.out.println(data.features[0].properties.area_description_data.data);
      System.out.println(data.features[0].properties.area_description_data);
      System.out.println(data.features[0].properties.area_description_data.data);

//      return this.collection.features[0].properties.area_description_data.data;
      return json;
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

  public record FeatureCollection(String type, Features[] features){}
  public record Features(Geometry geometry, Properties properties, String type){}
  public record Geometry(String type, List coordinates){}
  public record Properties(AreaDescriptionData area_description_data, String city,
                           String holc_grade, String name){}
  public record AreaDescriptionData(Map<String, String> data){}

}
