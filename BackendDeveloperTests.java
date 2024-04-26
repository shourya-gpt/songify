import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BackendDeveloperTests {

  /**
   * Tests if Backend.readData(String filename) throws an IOException when passed the name of a file
   * which doesn't exist.
   */
  @Test
  public void test1() {
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<SongInterface>();
    BackendInterface backend = new Backend(tree);
    try {
      backend.readData("this file doesn't exist");
      Assertions.fail();
    } catch (IOException e) {

    }

  }

  /**
   * Tests if the Backend.getRange(int low, int high) gives a correct list of titles sorted by BPM
   * withing the range provided.
   */
  @Test
  public void test2() {

    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<SongInterface>();
    BackendInterface backend = new Backend(tree);

    try {
      backend.readData("songs.csv");
    }
    // for compiler satisfaction
    catch (IOException e) {

    }

    List<String> result = backend.getRange(60, 80);
    List<String> expected = new ArrayList<String>();

    String[] toAdd = new String[] {"1+1", "Baby", "Praying", "Jar of Hearts", "Let Her Go",
        "Tee Shirt - Soundtrack Version", "I Knew You Were Trouble.", "Mirrors - Radio Edit",
        "Steal My Girl", "Ferrari", "American Oxygen", "Thinking out Loud", "Dear Future Husband",
        "Ghosttown", "Castle Walls (feat. Christina Aguilera)", "Cheers (Drink To That)",
        "Castle Walls (feat. Christina Aguilera)", "We Can't Stop", "Come & Get It",
        "See You Again (feat. Charlie Puth)"};

    for (int i = 0; i < toAdd.length; i++) {
      expected.add(toAdd[i]);
    }

    if (result.size() != expected.size()) {
      Assertions.fail();
    }

    for (int j = 0; j < result.size(); j++) {
      if (!result.get(j).equals(expected.get(j))) {
        Assertions.fail();
      }
    }



  }

  /**
   * Tests if 1. Backend.filterOldSongs(maxYear) returns an empty list if Backend.getRange(int low,
   * int high) has't previously been called. 2. Backend.filterOldSongs(maxYear) appropriately
   * filters the returned list of Songs from Backend.getRange(int low, int high).
   */
  @Test
  public void test3() {
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<SongInterface>();
    BackendInterface backend = new Backend(tree);

    try {
      backend.readData("songs.csv");
    }
    // for compiler satisfaction
    catch (IOException e) {

    }

    // Test 1, as mentioned in the header comment
    List<String> result = backend.filterOldSongs(2015);

    if (result.size() != 0) {
      Assertions.fail();
    }

    // Test 2, as metioned in the header comment
    List<String> getRange = backend.getRange(60, 80);
    List<String> filterOld = backend.filterOldSongs(2013);

    List<String> expected = new ArrayList<String>();

    String[] toAdd = new String[] {"1+1", "Baby", "Jar of Hearts", "I Knew You Were Trouble.",
        "Mirrors - Radio Edit", "Castle Walls (feat. Christina Aguilera)", "Cheers (Drink To That)",
        "Castle Walls (feat. Christina Aguilera)", "We Can't Stop", "Come & Get It"};

    for (int i = 0; i < toAdd.length; i++) {
      expected.add(toAdd[i]);
    }

    if (filterOld.size() != expected.size()) {
      Assertions.fail();
    }

    for (int j = 0; j < filterOld.size(); j++) {
      if (!filterOld.get(j).equals(expected.get(j))) {
        Assertions.fail();
      }
    }
  }

  /**
   * Tests if 1. Backend.fiveMostDanceable() throws an exception if Backend.getRange(int low, int
   * high) hasn't been called previously 2. Backend.fiveMostDanceable() returns the correct 5 most
   * danceable songs in increasing order of danceability if both getRange(int low, int high) and
   * filterOldSongs(maxYear) have been called earlier
   */
  @Test
  public void test4() {
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<SongInterface>();
    BackendInterface backend = new Backend(tree);

    try {
      backend.readData("songs.csv");
    }
    // for compiler satisfaction
    catch (IOException e) {

    }

    List<String> getRange = backend.getRange(60, 80);

    List<String> filterOldSongs = backend.filterOldSongs(2013);
    //
    List<String> fiveMostDanceable = backend.fiveMostDanceable();

    List<String> expected = new ArrayList<String>();

    String[] toAdd = new String[] {"58: Mirrors - Radio Edit", "58: Cheers (Drink To That)",
        "61: We Can't Stop", "62: I Knew You Were Trouble.", "73: Baby"};

    for (int i = 0; i < toAdd.length; i++) {
      expected.add(toAdd[i]);
    }

    if (fiveMostDanceable.size() != expected.size()) {
      Assertions.fail();
    }


    for (int j = 0; j < fiveMostDanceable.size(); j++) {
      if (!fiveMostDanceable.get(j).equals(expected.get(j))) {
        System.out.println("Actual: " + fiveMostDanceable.get(j));
        System.out.println("Expected: " + expected.get(j));
        Assertions.fail();
      }
    }

  }

  /**
   * Tests if Backend.fiveMostDanceable() returns a list which contains less than 5 titles when the
   * combination of getRange(int low, int high) and filterOldSongs() being applied results in a list
   * of Song titles which is less than 5 elements long.
   */
  @Test
  public void test5() {
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<SongInterface>();
    BackendInterface backend = new Backend(tree);

    try {
      backend.readData("songs.csv");
    }
    // for compiler satisfaction
    catch (IOException e) {

    }

    List<String> getRange = backend.getRange(60, 80);

    List<String> filterOldSongs = backend.filterOldSongs(2010);
    //
    List<String> fiveMostDanceable = backend.fiveMostDanceable();

    List<String> expected = new ArrayList<String>();

    String[] toAdd = new String[] {"45: Castle Walls (feat. Christina Aguilera)", "73: Baby"};

    for (int i = 0; i < toAdd.length; i++) {
      expected.add(toAdd[i]);
    }

    if (fiveMostDanceable.size() != expected.size()) {
      Assertions.fail();
    }

    for (int j = 0; j < fiveMostDanceable.size(); j++) {
      if (!fiveMostDanceable.get(j).equals(expected.get(j))) {
        Assertions.fail();
      }
    }
  }

  /**
   * Checks if my partner's frontend implementation displays the correct songs when the user inputs
   * G to get songs within the BPM range 90 - 100. Also tests if further using F and D commands
   * correctly filters and displays the correct list of songs.
   */
  @Test
  public void integrationTest1() {
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();

    TextUITester uiTester = new TextUITester("R\nsongs.csv\nG\n60 80\nF\n2013\nD\nQ\n");
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(tree);
    FrontendInterface frontend = new Frontend(scanner, backend);

    frontend.runCommandLoop();
    String output = uiTester.checkOutput();

    // A String list which contains (songs with equal parameters may be disordered) items returned
    // by G
    String[] getRangeCheck = new String[] {"1+1", "Baby", "Praying", "Let Her Go", "Jar of Hearts",
        "Tee Shirt - Soundtrack Version", "Ferrari", "Steal My Girl", "Mirrors - Radio Edit",
        "I Knew You Were Trouble.", "American Oxygen", "Ghosttown", "Dear Future Husband",
        "Thinking out Loud", "See You Again (feat. Charlie Puth)", "Come & Get It", "We Can't Stop",
        "Castle Walls (feat. Christina Aguilera)", "Cheers (Drink To That)",
        "Castle Walls (feat. Christina Aguilera)"};

    Assertions.assertTrue(output.contains("20 songs found between 60 - 80:"));
    for (String expected : getRangeCheck) {
      Assertions.assertTrue(output.contains(expected));
    }

    // Splits the output around the last returned song by "G" so that we can check the latter part
    // of the output
    int index = output.indexOf("Castle Walls (feat. Christina Aguilera)");
    String output1 = output.substring(index + "Castle Walls (feat. Christina Aguilera)".length());

    // A String list which contains (songs with equal parameters may be disordered) items returned
    // by F
    String[] filterOldCheck = new String[] {"1+1", "Baby", "Jar of Hearts", "Mirrors - Radio Edit",
        "I Knew You Were Trouble.", "Come & Get It", "We Can't Stop",
        "Castle Walls (feat. Christina Aguilera)", "Cheers (Drink To That)",
        "Castle Walls (feat. Christina Aguilera)"};

    Assertions.assertTrue(output.contains("10 songs found between 60 - 80 from 2013:"));
    for (String expected : filterOldCheck) {
      Assertions.assertTrue(output1.contains(expected));
    }

    // Splits the output around the last returned song by "R" so that we can check the latter part
    // of the output
    index = output1.indexOf("Castle Walls (feat. Christina Aguilera)");
    String output2 = output1.substring(index + "Castle Walls (feat. Christina Aguilera)".length());

    // A String list which contains (songs with equal parameters may be disordered) items returned
    // by F
    String[] fiveMostCheck = new String[] {"58: Mirrors - Radio Edit", "58: Cheers (Drink To That)",
        "61: We Can't Stop", "62: I Knew You Were Trouble.", "73: Baby"};

    Assertions.assertTrue(output.contains("10 songs found between 60 - 80 from 2013:"));
    for (String expected : fiveMostCheck) {
      Assertions.assertTrue(output2.contains(expected));
    }

  }

  /**
   * Checks if the frontend implementation gives the expected output if the following invalid inputs
   * are entered 1) F is entered before G 2) D is entered before G
   */
  @Test
  public void integrationTest2() {
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();

    TextUITester uiTester = new TextUITester("R\nsongs.csv\nF\n2013\nD\nQ\n");
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(tree);
    FrontendInterface frontend = new Frontend(scanner, backend);

    frontend.runCommandLoop();
    String output = uiTester.checkOutput();

    // If [F]ilterOldSongs is called before getRange(), 0 songs should be returned
    // If [D]isplay Five Most Danceable is called before getRange(), an invalid input statement
    // should be returned.
    Assertions.assertTrue(output.contains("0 songs"));
    Assertions.assertTrue(output.contains("must call [G]et"));

  }

  /**
   * Verifies if a request for of [D]anceable Songs in the implementation of Frontend.java correctly
   * returns a limited number of songs when a filter by [F]ilter Old Songs ad has been set in the
   * respective order. This is differnt than the test above as it calls G and F in an opposite order
   */
  @Test
  public void partnerTest1() {
    TextUITester test = new TextUITester("R\nsongs.csv\nF\n2014\nG\n139 140\nD\nQ");
    Scanner scanner = new Scanner(System.in);
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
    BackendInterface backend = new Backend(tree);
    FrontendInterface testHolder = new Frontend(scanner, backend);
    testHolder.runCommandLoop();

    String output = test.checkOutput();

    String[] danceableCheck =
        new String[] {"45: Paradise", "50: \"Let It Go - From \"\"Frozen / Single Version\"",
            "59: Drunk in Love", "67: Applause", "72: Gorilla"};

    for (String expected : danceableCheck) {
      Assertions.assertTrue(output.contains(expected));
    }
  }

  /**
   * Verifies if the implementation of Frontend.java returns the correct # of songs when [F]ilter
   * Old Songs is requested and a certain # of such songs exist (due to filters by [G]et Range)
   */
  @Test
  public void partnerTest2() {
    TextUITester test = new TextUITester("R\nsongs.csv\nG\n122 123\nF\n2010\nD\nQ" + "");
    Scanner scanner = new Scanner(System.in);
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<SongInterface>();
    BackendInterface backend = new Backend(tree);
    FrontendInterface testHolder = new Frontend(scanner, backend);
    testHolder.runCommandLoop();

    String output = test.checkOutput();

    Assertions.assertTrue(output.contains("2 songs"));
  }

}
