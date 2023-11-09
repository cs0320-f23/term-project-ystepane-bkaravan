package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeoHandler implements Route {

  public GeoHandler() {
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeatureCollection> jsonAdapter = moshi.adapter(FeatureCollection.class);

    try {
      Path filePath = Paths.get("data/GeoJSON/geodata.json");
      String json = Files.readString(filePath);

      FeatureCollection data = jsonAdapter.fromJson(json);

      // Now 'data' contains your parsed FeatureCollection
      System.out.println(data);
      System.out.println(data.features[0]);
      System.out.println(data.features[0].properties);
      System.out.println(data.features[0].properties.area_description_data.data);
      System.out.println(data.features[0].properties.area_description_data);
      System.out.println(data.features[0].properties.area_description_data.data);
      return data.features[0].properties.area_description_data.data;
    } catch (Exception e) {
      //e.printStackTrace();
    }
    return null;
  }

  public record FeatureCollection(String type, Features[] features){}
  public record Features(Geometry geometry, Properties properties, String type){}
  public record Geometry(String type){}
  public record Properties(AreaDescriptionData area_description_data, String city,
                           String holc_grade, String name){}
  public record AreaDescriptionData(Map<String, String> data){}
}
