import java.io.BufferedReader;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Backend implements BackendInterface {

  // A String list of song data for use in this class
  private List<String> unparsed;
  // The maxYear threshold from the filterOldSongs() method for uses by other methods
  private int maxYear;
  // A list of Song objects corresponding to the Song titles returned by getRange()
  private List<SongInterface> getRange;
  // The RBT we're adding Song objects to
  private IterableSortedCollection<SongInterface> tree;

  public Backend(IterableSortedCollection<SongInterface> tree) {
    this.tree = tree;
    unparsed = new ArrayList<String>();
    maxYear = -1;
    getRange = new ArrayList<SongInterface>();
    getRange.add(null);
  }

  /**
   * Loads data from the .csv file referenced by filename.
   * 
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  @Override
  public void readData(String filename) throws IOException {
    

    String object = "";

    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));

      while ((object = br.readLine()) != null) {
        unparsed.add(object);
      }
    } catch (FileNotFoundException e) {
      // Note that initializing br or using it may throw any type of Exception, including
      // FileNotFoundException() and IOException(). But, we are only interested in throwing an
      // IOException()
      throw new IOException();
    } catch (IOException e) {
      throw new IOException();
    }

    // for (int i = 1; i < unparsed.size(); i++) {
    // System.out.println(unparsed.get(i));
    // }

    // Organize the songs data from this.unparsed into this.parsed by parsing the String objects
    // which represent comma separated values. this.parsed will contain lists for each Song obeject
    // from this.unparsed, separated according to their fields.

    // A String list for every song object, which we will finally add to this.parsed
    ArrayList<String> addToParsed = new ArrayList<String>();
    // The number of quotes encountered within a String object for a song
    int quotes = 0;
    String field = "";
    // The String object for the song
    String song;
    char quote = '\"';
    char comma = ',';


    // Iterate through each String object i.e. song in this.unparsed
    for (int j = 1; j < unparsed.size(); j++) {
      song = unparsed.get(j);
      // Iterate through each character in song
      for (int k = 0; k < song.length(); k++) {

        // If you encounter a quote, increment the counter
        if (song.charAt(k) == quote) {
          quotes++;
        }

        // If the character is a comma and the number of quotes encounted so far is even, add this
        // character to addToParsed
        // This takes care of skipping the comma itself, and not adding it to the field addToParsed
        if (song.charAt(k) == comma && quotes % 2 == 0) {
          addToParsed.add(field);
          field = "";
        }
        // If this is just a regular character or a regular comma (which succeeds an odd number of
        // quotes), then add this to the field
        else {
          field += song.charAt(k);
        }

      }

      // Add the last field to addToParsed as well
      addToParsed.add(field);

      // Add the different fields to the tree
      tree.insert(new Song(addToParsed.get(0), addToParsed.get(1), addToParsed.get(2),
          Integer.parseInt(addToParsed.get(3)), Integer.parseInt(addToParsed.get(4)),
          Integer.parseInt(addToParsed.get(5)), Integer.parseInt(addToParsed.get(6)),
          Integer.parseInt(addToParsed.get(7)), Integer.parseInt(addToParsed.get(8)),
          Integer.parseInt(addToParsed.get(9)), Integer.parseInt(addToParsed.get(10)),
          Integer.parseInt(addToParsed.get(11)), Integer.parseInt(addToParsed.get(12)),
          Integer.parseInt(addToParsed.get(13))));

      // System.out.println("Next: " + it.next());

      // parsed.add(addToParsed);

      // Reset the addToParsed and field variable for use by the next iteration
      addToParsed = new ArrayList<String>();
      field = "";
    }



    // System.out.println("Parsed size: " + parsed.size());
    //
    // for (int m = 0; m < parsed.size(); m++) {
    // System.out.println(parsed.get(m).size());
    // System.out.println("Song " + m + ": " + parsed.get(m));
    // }

  }

  /**
   * Retrieves a list of song titles for songs that have a Speed (BPM) within the specified range
   * (sorted by BPM in ascending order). If a maxYear filter has been set using filterOldSongs(),
   * then only songs on Billboard during or before that maxYear should be included in the list that
   * is returned by this method.
   *
   * Note that either this bpm range, or the resulting unfiltered list of songs should be saved for
   * later use by the other methods defined in this class.
   *
   * @param low   is the minimum Speed (BPM) of songs in the returned list
   * @param hight is the maximum Speed (BPM) of songs in the returned list
   * @return List of titles for all songs in specified range
   */
  @Override
  public List<String> getRange(int low, int high) {
    // int bpm;
    // ArrayList<String> toReturn = new ArrayList<Integer>();

    // for (int i = 0; i < parsed.size(); i++) {
    // bpm = Integer.parseInt(parsed.get(i).get(4));
    // if (bpm >= low && bpm <= high) {
    // indices.add(i);
    // }
    // }

    // int swap;

    // System.out.println(indices);
    // for (int j = 0; j < indices.size(); j++) {
    // for (int k = j + 1; k < indices.size(); k++) {
    // if (Integer.parseInt(parsed.get(indices.get(k)).get(4)) < Integer
    // .parseInt(parsed.get(indices.get(j)).get(4))) {
    // swap = indices.get(j);
    // indices.set(j, indices.get(k));
    // indices.set(k, swap);
    // }
    // }
    // }

    // for (int m = 0; m < indices.size(); m++) {
    // System.out.println("Song: " + parsed.get(indices.get(m)).get(0) + " BPM: "
    // + parsed.get(indices.get(m)).get(4));
    // }

    getRange.remove(0);


    Iterator<SongInterface> it = tree.iterator();
    int bpm;
    // An empty list to store the Song objects to return
    ArrayList<SongInterface> toReturnSongs = new ArrayList<SongInterface>();
    SongInterface song;

    // Iterate through the tree
    while (it.hasNext() != false) {
      song = it.next();
      bpm = song.getBPM();
      // If bpm within range, add it to the list of songs
      if (bpm >= low && bpm <= high) {
        toReturnSongs.add(song);
      }
    }

    // int min;
    //
    // for(int i=0;i<toReturnSongs.size();i++) {
    // min = i;
    // for(int j=i+1;j<toReturnSongs.size();j++) {
    // if(toReturnSongs.get(j).getBPM()<toReturnSongs.get(min).getBPM()) {
    // min = j;
    // }
    //
    // }
    // if(min!=i) {
    // SongInterface swap = toReturnSongs.get(i);
    // toReturnSongs.set(i, toReturnSongs.get(min));
    // toReturnSongs.set(min, swap);
    // }
    // }


    // This is the list we will return
    List<String> toReturnTitles = new ArrayList<String>();

    // Add the titles of the songs in toReturnSongs to this list
    for (int l = 0; l < toReturnSongs.size(); l++) {
      getRange.add(toReturnSongs.get(l));
    }
    boolean notFound = true;

    // Further filter these song titles if filterOldSongs() has been previously called
    if (maxYear != -1) {
      List<String> filteredTitles = this.filterOldSongs(maxYear);

      // If for all combinations of songs in toReturnSongs(), if it is not present in filteredTitles(), then remove it
      ArrayList<SongInterface> toRemove = new ArrayList<SongInterface>();
      for (int m = 0; m < toReturnSongs.size(); m++) {
        notFound = true;
        for (int h = 0; h < filteredTitles.size(); h++) {
          // If song found, set notFound to false
          if (filteredTitles.get(h).equals(toReturnSongs.get(m).getTitle())) {
            notFound = false;
            break;
          }
        }
        // Remove the songs
        if (notFound) {
          toRemove.add(toReturnSongs.get(m));
        }

        
    

//         if (filteredTitles.contains(toReturnSongs.get(m).getTitle())) {
////         System.out.println("To be removed: " + toReturnSongs.get(m).getTitle());
//         toReturnSongs.remove(m);
//         }
      }

      toReturnSongs.removeAll(toRemove);

    }

    // Add the titles of the songs in toReturnSongs to toReturnTitles
    for (int k = 0; k < toReturnSongs.size(); k++) {
      toReturnTitles.add(toReturnSongs.get(k).getTitle());
    }


    // Making a deep (not deepest copy of toReturnSongs)
    getRange.clear();
    for (int l = 0; l < toReturnSongs.size(); l++) {
      getRange.add(toReturnSongs.get(l));
    }
    
    return toReturnTitles;
  }

  /**
   * Filters the list of songs returned by future calls of getRange() and fiveMostDanceable() to
   * only include older songs. If getRange() was previously called, then this method will return a
   * list of song titles (sorted in ascending order by Speed BPM) that only includes songs on
   * Billboard on or before the specified maxYear. If getRange() was not previously called, then
   * this method should return an empty list.
   *
   * Note that this maxYear threshold should be saved for later use by the other methods defined in
   * this class.
   *
   * @param maxYear is the maximum year that a returned song was on Billboard
   * @return List of song titles, empty if getRange was not previously called
   */
  @Override
  public List<String> filterOldSongs(int maxYear) {

    // Three cases
    // 1. getRange() not called -- return empty list
    // 2. getRange() called -- filter that list


    this.maxYear = maxYear;

    List<String> toReturnTitles = new ArrayList<String>();
    if (getRange.size() != 0 && getRange.get(0) == null) {
      return toReturnTitles;
    } else {
      for (int i = 0; i < getRange.size(); i++) {
        if (getRange.get(i).getYear() <= maxYear) {
          toReturnTitles.add(getRange.get(i).getTitle());
        }
      }

      return toReturnTitles;
    }

  }

  /**
   * This method makes use of the attribute range specified by the most recent call to getRange().
   * If a maxYear threshold has been set by filterOldSongs() then that will also be utilized by this
   * method. Of those songs that match these criteria, the five most danceable will be returned by
   * this method as a List of Strings in increasing order of danceability. Each string contains the
   * danceability followed by a colon, a space, and then the song's title. If fewer than five such
   * songs exist, return all of them.
   *
   * @return List of five most danceable song titles and their danceabilities
   * @throws IllegalStateException when getRange() was not previously called.
   */
  @Override
  public List<String> fiveMostDanceable() {
    List<String> toReturnTitles = new ArrayList<String>();
    // The constructor sets getRange's 0th element to null. This gets changed in a call of the
    // getRange() method.
    if (getRange.size() != 0 && getRange.get(0) == null) {
      throw new IllegalStateException(
          "getRange() not previously called, but fiveMostDanacable() called");
    } else {


      List<SongInterface> toReturnSongs = new ArrayList<SongInterface>();
      if (getRange.size() == 0) {
        return toReturnTitles;
      }
      // At this point, we know that getRange() has definitely been called. If we get an empty
      // string from filterOldSongs(), that means that getRange() returned an empty string.
      if (maxYear != -1) {
        toReturnTitles = this.filterOldSongs(maxYear);

        Iterator<SongInterface> it = tree.iterator();
        toReturnSongs = new ArrayList<SongInterface>();
        SongInterface song;

        // Store the songs returned by filterOldSongs(maxYear) into an ArrayList of SongInterface
        // type
        while (it.hasNext() != false) {
          song = it.next();
          for (int i = 0; i < toReturnTitles.size(); i++) {
            if (toReturnTitles.get(i) == song.getTitle()) {
              toReturnSongs.add(song);
            }
          }
        }

        // Sort toReturnSongs according to their danceability according using ananonymous class
        Collections.sort(toReturnSongs, new Comparator<SongInterface>() {
          public int compare(SongInterface song1, SongInterface song2) {
            return ((Integer) song1.getDanceability())
                .compareTo((Integer) (song2.getDanceability()));
          }
        });

        toReturnTitles.clear();

        // Add min(toReturnSongs.size(), 5) SongInterface.getTitle() to toReturnTitles()
        int q = Math.min(toReturnSongs.size(), 5);
        for (int t = 0; t < q; t++) {
          toReturnTitles.add(toReturnSongs.get(toReturnSongs.size() - q + t).getDanceability()
              + ": " + toReturnSongs.get(toReturnSongs.size() - q + t).getTitle());
        }


        // Sorts the collection filterOldSongs(maxYear) in


        // toReturnTitles.clear();
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        //
        // // Gets the 5 most dancable songs from the filtered list provided by toReturnTitles();
        // List<SongInterface> copyOfSongs = new ArrayList<SongInterface>();
        // // Creating a deep (not deepest) copy of toReturnSongs
        //
        // for (int l = 0; l < toReturnSongs.size(); l++) {
        // copyOfSongs.add(toReturnSongs.get(l));
        // }
        // int count = 0;
        // ArrayList<SongInterface> toRemove = new ArrayList<SongInterface>();
        // for(int j=0; j<Math.min(copyOfSongs.size(), 5); j++) {
        // count++;
        // System.out.println("j" + j);
        // SongInterface max = copyOfSongs.get(0);
        // for (int k = 0; k < copyOfSongs.size(); k++) {
        // if (copyOfSongs.get(k).getDanceability() > max.getDanceability()) {
        // max = copyOfSongs.get(k);
        // }
        //
        // }
        // toRemove.add(max);
        // toReturnTitles.set(4 - j, max.getDanceability() + ": " + max.getTitle());
        //
        // }
        // System.out.println("toRemove size " + toRemove.size());
        // for(int y=0; y<toRemove.size();y++) {
        // copyOfSongs.remove(toRemove.get(y));
        // }
        // System.out.println("final count: " + count);

      } else {
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // toReturnTitles.add(null);
        // // Gets the 5 most dancable songs from the filtered list provided by toReturnTitles();
        toReturnSongs = new ArrayList<SongInterface>();
        // Creating a deep (not deepest) copy of getRange
        for (int l = 0; l < getRange.size(); l++) {
          toReturnSongs.add(getRange.get(l));
        }

        // sort toReturnSongs according to their danceability using an anonymous class
        Collections.sort(toReturnSongs, new Comparator<SongInterface>() {
          public int compare(SongInterface song1, SongInterface song2) {
            return ((Integer) song1.getDanceability())
                .compareTo((Integer) (song2.getDanceability()));
          }
        });

        toReturnTitles.clear();

        // Add min(toReturnSongs.size(), 5) SongInterface.getTitle() to toReturnTitles()
        int q = Math.min(toReturnSongs.size(), 5);
        for (int t = 0; t < q; t++) {
          toReturnTitles.add(toReturnSongs.get(toReturnSongs.size() - q + t).getDanceability()
              + ": " + toReturnSongs.get(toReturnSongs.size() - q + t).getTitle());
        }

        // for(int j=0; j<Math.min(copyOfSongs.size(), 5); j++) {
        // SongInterface max = copyOfSongs.get(0);
        // for (int k = 0; k < copyOfSongs.size(); k++) {
        // if (copyOfSongs.get(k).getDanceability() > max.getDanceability()) {
        // max = copyOfSongs.get(k);
        // }
        //
        // }
        // copyOfSongs.remove(max);
        // toReturnTitles.set(4 - j, max.getDanceability() + ": " + max.getTitle());
        //
        // }
        //
        // for (Iterator<String> itr = toReturnTitles.iterator(); itr.hasNext();) {
        // if (itr.next() == null) { itr.remove(); }
      }


    }
    
    return toReturnTitles;
  }



  public static void main(String[] args) throws IOException {

  }}
