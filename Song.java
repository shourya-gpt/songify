
public class Song implements SongInterface{
  private String title;
  private String artist;
  private String genre;
  private int year;
  private int bpm;
  private int nrgy;
  private int dnce;
  private int dB;
  private int live;
  private int val;
  private int dur;
  private int acous;
  private int spch;
  private int pop;
  

  public Song(String title, String artist, String genre, int year, int bpm, int nrgy, int dnce, int dB, int live, int val, int dur, int acous, int spch, int pop) {
    this.title = title;
    this.artist = artist;
    this.genre = genre;
    this.year = year;
    this.bpm = bpm;
    this.nrgy = nrgy;
    this.dnce = dnce;
    this.dB = dB;
    this.live = live;
    this.val = val;
    this.dur = dur;
    this.acous = acous;
    this.spch = spch;
    this.pop = pop;
    
  }
  
  @Override
  public int compareTo(SongInterface o) {
    if(this.bpm<o.getBPM()) {
      return -1;
    }
    else if(this.bpm>o.getBPM()) {
      return 1;
    }
    else {
      return 0;
    }
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getArtist() {
    return this.artist;
  }

  @Override
  public String getGenres() {
    return this.genre;
  }

  @Override
  public int getYear() {
    return this.year;
  }

  @Override
  public int getBPM() {
    return this.bpm;
  }

  @Override
  public int getEnergy() {
    return this.nrgy;
  }

  @Override
  public int getDanceability() {
    return this.dnce;
  }

  @Override
  public int getLoudness() {
    return this.dB;
  }

  @Override
  public int getLiveness() {
    return this.live;
  }

}
