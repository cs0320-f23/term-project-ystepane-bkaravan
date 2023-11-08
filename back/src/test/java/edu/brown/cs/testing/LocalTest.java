package edu.brown.cs.testing;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.creator.Creator;
import edu.brown.cs.student.main.handler.LoadCensusHandler;
import edu.brown.cs.student.main.handler.LoadHandler;
import edu.brown.cs.student.main.handler.SearchHandler;
import edu.brown.cs.student.main.handler.ViewHandler;
import edu.brown.cs.student.main.parser.ParserCSV;
import edu.brown.cs.student.main.parser.SearchCSV;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Files;
import com.squareup.moshi.JsonAdapter;
import java.nio.file.Paths;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import edu.brown.cs.student.main.server.Storage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the actual handlers.
 *
 * On Sprint 2, you'll need to deserialize API responses. Although this demo doesn't need to do that,
 * these _tests_ do.
 *
 * https://junit.org/junit5/docs/current/user-guide/
 *
 * Because these tests exercise more than one "unit" of code, they're not "unit tests"...
 *
 * If the backend were "the system", we might call these system tests. But
 * I prefer "integration test" since, locally, we're testing how the Soup
 * functionality connects to the handler. These distinctions are sometimes
 * fuzzy and always debatable; the important thing is that these ARE NOT
 * the usual sort of unit tests.
 *
 * Note: if we were doing this for real, we might want to test encoding formats
 * other than UTF-8 (StandardCharsets.UTF_8).
 */

class LocalTest {

  @BeforeAll
  public static void setup_before_everything() {

    Spark.port(0);

    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
   * need to replace the reference itself. We clear this state out after every test runs.
   */


  @BeforeEach
  public void setup() {

    Storage csvStorage = new Storage();
    Spark.get("loadCSV", new LoadHandler(csvStorage));
    Spark.get("viewCSV", new ViewHandler(csvStorage));
    Spark.get("searchCSV", new SearchHandler(csvStorage));
    Spark.get("broadband", new LoadCensusHandler(csvStorage));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("loadCSV");
    Spark.unmap("viewCSV");
    Spark.unmap("searchCSV");
    Spark.unmap("broadband");

    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *                structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  static private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();


    clientConnection.connect();
    return clientConnection;
  }

  /**
   * This class shows the response of the filepath resulting from a successful load.
   */
  public static class ResponseLoadCSV {

    public String filepath;
  }
  /**
   * This test checks the successful load of a file with a relative path.
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPILoadCSVFileOK_rel_path() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadCSV?filepath=data/testData/abc.csv");
    assertEquals(200, clientConnection.getResponseCode());


    Moshi moshi = new Moshi.Builder().build();
    ResponseLoadCSV response =
        moshi.adapter(ResponseLoadCSV.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    clientConnection.disconnect();
    assertEquals("Success! File: data/testData/abc.csv", response.filepath);
  }
  /**
   * This class shows the responses generated by a successful view of the file.
   */
  public static class ResponseViewCSV {

    public String result;
    public List<List<String>> view_data;
  }

  /**   This test checks the successful load and view of a file.

   * @throws IOException - throws an exception.
   */
  @Test
  public void testAPILoadCSVFile_and_view() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/abc.csv");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    ResponseLoadCSV response =
        moshi.adapter(ResponseLoadCSV.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("Success! File: data/testData/abc.csv", response.filepath);
    // view
    HttpURLConnection clientConnectionView = tryRequest(
        "viewCSV");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnectionView.getResponseCode());
    Moshi moshiView = new Moshi.Builder().build();
    ResponseViewCSV responseView =
        moshi.adapter(ResponseViewCSV.class)
            .fromJson(new Buffer().readFrom(clientConnectionView.getInputStream()));
    System.out.println(responseView.view_data);
    assertEquals("success", responseView.result);

  }

  /**
   * This class shows the responses of the use of broadband endpoint.
   */
  public static class ResponseBB {

    public String result;
    public String timestamp;
    public String address;
    public String bbNumber;
    public String error_type;


  }
  /**
   * This test checks the broadband working.
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPICensusBB() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "broadband?state=Rhode%20Island&county=Providence");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    ResponseBB response =
        moshi.adapter(ResponseBB.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    assertEquals("success", response.result);
    assertEquals("Providence County, Rhode Island", response.address);
    assertEquals("85.4", response.bbNumber);
  }


  /**
   * This test checks a failing broadband request because no state was inputted.
   * @throws IOException - throws an exception
   */

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPICensusBB_NoState() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "broadband?");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    ResponseBB response =
        moshi.adapter(ResponseBB.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    assertEquals("error", response.result);
    assertEquals("state or county not provided", response.error_type);
  }
  /**
   * This test checks a failing broadband request because no state was found (wrong input).
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPICensusBB_NotFound() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "broadband?state=Rode%20Island&county=Providence");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    ResponseBB response =
        moshi.adapter(ResponseBB.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    assertEquals("error", response.result);
    assertEquals("seeking state Rode Island not found", response.error_type);
  }
  /**
   * This test checks a failing broadband request because no such county was found. (wrong input)
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPICensusBB_NotFound_County() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "broadband?state=Rhode%20Island&county=Cumberland");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    ResponseBB response =
        moshi.adapter(ResponseBB.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    assertEquals("error", response.result);
    assertEquals("county Cumberland in Rhode Island. Not found", response.error_type);
  }

  /**
   * This test checks the successful load of a file with an absolute path.
   * @throws IOException - throws an exception
   */
//  @Test
//  public void testAPILoadCSVFileOK_abs_path() throws IOException {
//    HttpURLConnection clientConnection = tryRequest(
//        "loadCSV?filepath=C:\\Users\\julia\\Brown\\sophomore\\s1\\cs32\\server-ystepane-Hamad1020\\data\\testData\\abc.csv");
//    assertEquals(200, clientConnection.getResponseCode());
//    Moshi moshi = new Moshi.Builder().build();
//    ResponseLoadCSV response =
//        moshi.adapter(ResponseLoadCSV.class)
//            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
//
//    clientConnection.disconnect();
//    assertEquals(
//        "Success! File: C:\\Users\\julia\\Brown\\sophomore\\s1\\cs32\\server-ystepane-Hamad1020\\data\\testData\\abc.csv",
//        response.filepath);
//  }

  /**
   * This class shows the responses if errors occur.
   */
  public static class ResponseError {

    public String result;
    public String error_type;
  }

  /**
   * This test checks a load of a bad file.
   * @throws IOException -- throws exceptions.
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testAPILoadCSVFileError() throws IOException {
    // HttpURLConnection clientConnection = tryRequest("loadCensus?state=Maine");
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/stardataFALSE.csv");

    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());

    // Now we need to see whether we've got the expected Json response.
    // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here

    ResponseLoadCSV response =
        moshi.adapter(ResponseLoadCSV.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    // System.out.println(response.filepath);
    // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception, but a real Json reply.

    clientConnection.disconnect();
    assertEquals("Error: Invalid or empty file name", response.filepath);

  }
  /**
   * This class shows the response to the search.
   */
  public static class SearchResponse {

    public String result;
    public List<String[]> data;
    public String error_type;
  }
  /** This test checks a successful search result.
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testSearchCSV_OK() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/abc.csv");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    //assertEquals("Success! File: data/stars/ten-star.csv",response.filepath);
    // search
    HttpURLConnection clientConnectionSearch = tryRequest(
        "searchCSV?search=abc&I:2&y");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnectionSearch.getResponseCode());
    Moshi moshiSearch = new Moshi.Builder().build();
    SearchResponse responseSearch =
        moshiSearch.adapter(SearchResponse.class)
            .fromJson(new Buffer().readFrom(clientConnectionSearch.getInputStream()));
    assertEquals("success", responseSearch.result);
    String[] resList = {"abc", "def", "ghi","jkl"};
    String responseString = resList[0];
    String responseString1 = resList[1];
    String responseString2= resList[2];
    String responseString3 = resList[3];

    assertEquals(responseString, responseSearch.data.get(0)[0]);
    assertEquals(responseString1, responseSearch.data.get(0)[1]);
    assertEquals(responseString2, responseSearch.data.get(0)[2]);
    assertEquals(responseString3, responseSearch.data.get(0)[3]);

  }
  /** This test checks a failing search result because there is not a valid keyword.
   * @throws IOException - throws an exception
   */
  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testSearchCSV_NonExistentKeyword() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/abc.csv");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    //assertEquals("Success! File: data/stars/ten-star.csv",response.filepath);
    // search
    HttpURLConnection clientConnectionSearch = tryRequest(
        "searchCSV?search=Sol1&I:2&y");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnectionSearch.getResponseCode());
    Moshi moshiSearch = new Moshi.Builder().build();
    SearchResponse responseSearch =
        moshiSearch.adapter(SearchResponse.class)
            .fromJson(new Buffer().readFrom(clientConnectionSearch.getInputStream()));
    assertEquals("error", responseSearch.result);
    assertEquals("seeking word not found", responseSearch.error_type);
  }

  /**
   * This test searches through an empty dataset.
   * @throws IOException - throws an exception
   */

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testSearchCSV_Empty() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/empty.csv");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    //assertEquals("Success! File: data/stars/ten-star.csv",response.filepath);
    // search
    HttpURLConnection clientConnectionSearch = tryRequest(
        "searchCSV?search=Sol&I:2&y");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnectionSearch.getResponseCode());
    Moshi moshiSearch = new Moshi.Builder().build();
    SearchResponse responseSearch =
        moshiSearch.adapter(SearchResponse.class)
            .fromJson(new Buffer().readFrom(clientConnectionSearch.getInputStream()));
    assertEquals("error", responseSearch.result);
    assertEquals("error_datasource", responseSearch.error_type);
  }


  /** This test checks a failing search result because there is no keyword.
   * @throws IOException - throws an exception
   */

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type checker
  public void testSearchCSV_NoKeyword() throws IOException {
    HttpURLConnection clientConnection = tryRequest(
        "loadCSV?filepath=data/testData/abc.csv");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());
    //assertEquals("Success! File: data/stars/ten-star.csv",response.filepath);
    // search
    HttpURLConnection clientConnectionSearch = tryRequest(
        "searchCSV?");// Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnectionSearch.getResponseCode());
    Moshi moshiSearch = new Moshi.Builder().build();
    SearchResponse responseSearch =
        moshiSearch.adapter(SearchResponse.class)
            .fromJson(new Buffer().readFrom(clientConnectionSearch.getInputStream()));
    assertEquals("error", responseSearch.result);
    assertEquals("Search not provided", responseSearch.error_type);
  }
  @Test
  public void testCorrectCSVError() {
    String ms =
        """
        aaa, bbbb, Proxima
        vvv, dddd, ggggggg
        0, Proxima
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    assertTrue(testParser.isError);
  }

  /** this test shows an example of a correct input. */
  @Test
  public void testCorrectCSV() {
    String ms =
        """
        aaa, bbbb, Proxima
        vvv, dddd, ggggggg
        0, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    assertFalse(testParser.isError);
  }


  /** successful search by index number of the column. */
  @Test
  public void testSearchIndex() {
    String ms =
        """
        letter, name, star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.result, true);
    boolean searchindex = search.searchByIndex("Proxima", 1);

    assertTrue(searchindex);
  }

  /** test of searching by index number where there is no header. */
  @Test
  public void testSearchIndexNoHeader() {
    String ms =
        """
        letter, name, star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.result, false);
    boolean searchindex = search.searchByIndex("Proxima", 1);

    assertTrue(searchindex);
  }

  /** test searching the testing by using the name of the column. */
  @Test
  public void testSearchName() {
    String ms =
        """
        letter,name,star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.result, true);
    boolean searchindex = search.searchByName("Proxima", "star");

    assertTrue(searchindex);
  }

  /** testing the search using the name of the column where the name of the column is invalid. */
  @Test
  public void testSearhNameFalse() {
    String ms =
        """
        letter,name,star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.result, true);
    boolean searchindex = search.searchByName("Proxima", "noname");

    assertFalse(searchindex);
  }

  /** searching the word that only exists in the header (checking that we skip the 0 row). */
  @Test
  public void testSearhNameHeaderOnly() {
    String ms =
        """
        letter,name,star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.result, true);
    boolean searchindex = search.searchByAll("letter");

    assertFalse(searchindex);
  }

  /** this test checks the case when the keyword exists but not in the specified column */
  @Test
  public void testSearhIndexFalse() {
    String ms =
        """
        letter,name,star
        aaa, leah, Proxima
        vvv, katie, Sun
        ccc, Proxima, 1
        """;
    StringReader sr = new StringReader(ms);
    ParserCSV testParser = new ParserCSV(sr, new Creator());
    SearchCSV search = new SearchCSV(testParser.getParseResult(), true);
    boolean searchindex = search.searchByIndex("letter", 2);

    assertFalse(searchindex);
  }

  /**
   * @throws FileNotFoundException testing the searcher and passing in the file path for one of the
   *     source files.
   */
  @Test
  public void testFindFromPath() throws FileNotFoundException {
    String filepath = "data/stars/ten-star.csv";
    ParserCSV testParser = new ParserCSV(new FileReader(filepath), new Creator());
    SearchCSV search = new SearchCSV(testParser.result, true);
    boolean searchindex = search.searchByIndex("Sol", 1);

    assertTrue(searchindex);
  }

  /**
   * @throws FileNotFoundException testing the exception of inputting the file name that doesn't
   *     exist.
   */
  @Test
  public void testFileNotFound() throws FileNotFoundException {
    String filepath = "This is not a file";
    assertThrows(
        FileNotFoundException.class, () -> new ParserCSV(new FileReader(filepath), new Creator()));
  }

  /** This test shows syntax for a basic assertFalse assertion -- can be deleted */

}