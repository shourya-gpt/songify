import java.util.ArrayList;
import java.util.Iterator;

public class ISCPlaceholder<T extends Comparable<T>> implements IterableSortedCollection<T> {

  private ArrayList<T> array;

  private T value;

  public ISCPlaceholder() {
    array = new ArrayList<T>();
  }



  public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
    array.add(data);
    return true;
  }

  public boolean contains(Comparable<T> data) {
    return true;
  }

  public boolean isEmpty() {
    return false;
  }

  public int size() {
    return 3;
  }

  public void clear() {
  }

  public void setIterationStartPoint(Comparable<T> startPoint) {
  }

  public Iterator<T> iterator() {

    array.sort(null);
    return array.iterator();
  }
}
