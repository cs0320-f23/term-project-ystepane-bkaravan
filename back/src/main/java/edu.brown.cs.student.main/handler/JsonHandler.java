package edu.brown.cs.student.main.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Main.GeoResponse;
import edu.brown.cs.student.main.creator.RawCreator;
import edu.brown.cs.student.main.handler.LoadHandler.FailureResponse;
import edu.brown.cs.student.main.handler.LoadHandler.SuccessResponse;
import edu.brown.cs.student.main.parser.ParserCSV;
import edu.brown.cs.student.main.server.Storage;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import spark.Request;
import spark.Response;
import spark.Route;

public class JsonHandler implements Route {

  private String file;
  private GeoResponse response;

  /**
   * This method sets the storage.
   *
   * @param storage -- the storage.
   */
  public JsonHandler(GeoResponse storage) {
    this.response = storage;
  }

  /**
   * The handle method that handles the loading part by processing in, checking if the inputs are
   * valid and putting it through parser, as well as producing a success or failure response.
   *
   * @param request  -- the file path of the desired file.
   * @param response -- the success or failure message regarding the loading.
   * @return -- the success or failure message regarding the loading.
   * @throws Exception -- throws an exception in case of an error in the serialization.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    this.file = request.queryParams("filepath");

    // Check if the file name is null, empty, or invalid
    if (file == null || file.isEmpty() || !isFileValid(file)) {
      return new LoadHandler.FailureResponse("Error: Invalid or empty file name").serialize();
    }
    String json = new String(Files.readAllBytes(Paths.get(this.file)));
    Moshi moshi = new Moshi.Builder().build();

    JsonAdapter<GeoResponse> adapter = moshi.adapter(GeoResponse.class);
    this.response = adapter.fromJson(json);

    return json;
  }

  /**
   * This method checks if the given file exists.
   *
   * @param fileName -- the provided file name.
   * @return -- a boolean being true if the file exists and is an actual file.
   */
  private boolean isFileValid(String fileName) {
    // Check if the file exists and is a valid file (not a directory)
    File file = new File(fileName);
    return file.exists() && file.isFile();
  }
//
//  /**
//   * This record is responsible for producing the success response.
//   *
//   * @param filepath -- the filepath provided in the input.
//   */
//  public record SuccessResponse(String filepath) {
//
//    String serialize() {
//      try {
//        Moshi moshi = new Moshi.Builder().build();
//        JsonAdapter<LoadHandler.SuccessResponse> adapter = moshi.adapter(
//            LoadHandler.SuccessResponse.class);
//        return adapter.toJson(this);
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw e;
//      }
//    }
//  }
//
//  /**
//   * This record is responsible for producing the failure response.
//   *
//   * @param filepath -- the filepath provided in the input.
//   */
//  public record FailureResponse(String filepath) {
//
//    String serialize() {
//      Moshi moshi = new Moshi.Builder().build();
//      return moshi.adapter(LoadHandler.FailureResponse.class).toJson(this);
//    }
//  }
}
