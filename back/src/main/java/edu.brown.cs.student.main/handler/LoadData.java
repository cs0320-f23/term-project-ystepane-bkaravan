package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.handler.BoundBox.AreaDescriptionData;
import edu.brown.cs.student.main.handler.BoundBox.FeatureCollection;
import edu.brown.cs.student.main.handler.BoundBox.Features;
import edu.brown.cs.student.main.handler.BoundBox.Geometry;
import edu.brown.cs.student.main.handler.BoundBox.Properties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadData implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<LoadData.FeatureCollection> jsonAdapter = moshi.adapter(
        LoadData.FeatureCollection.class);
    try {
//      String file = "data/GeoJSON/geodata.json";
//
//      Path filePath = Paths.get(file);
//      String json = Files.readString(filePath);
//      System.out.println(jsonAdapter.fromJson(json));
//      return json;
//    }
//    catch (Exception e){
//
//    }
//    return null;
      response.type("application/json");

      // Read the content of your JSON file
      String jsonContent = readFileContent("data/GeoJSON/geodata.json");


      System.out.println(jsonContent);

      // Return rl_data as the response
      return jsonContent;
    } catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }
    // Implement a method to read the content of a file
    private String readFileContent(String filePath) throws IOException, IOException {
      return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }



  public record FeatureCollection(String type, LoadData.Features[] features){}
  public record Features(LoadData.Geometry geometry, LoadData.Properties properties, String type){}
  public record Geometry(String type, List<List<List<List<Float>>>> coordinates){}
  public record Properties(LoadData.AreaDescriptionData area_description_data, String city,
                           String holc_grade, String name){}
  public record AreaDescriptionData(Map<String, String> data){}

}



