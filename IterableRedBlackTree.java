import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
public class IterableRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T>
    implements IterableSortedCollection<T> {

  // By default this object has compareTo method that always returns -1 independent of what it is
  // being compared to
  private Comparable<T> startPoint = new Comparable<T>() {

    @Override
    public int compareTo(T o) {
      // Always return -1 regardless of what the object is being compared to
      return -1;
    }

  };

  public void setIterationStartPoint(Comparable<T> startPoint) {
    // If null is ever passed to the setIterationStartPoint method as an argument, then this
    // instance field should return to being a value that is always smaller than any possible value
    // in the tree.
    if (startPoint == null) {
      this.startPoint = new Comparable<T>() {

        @Override
        public int compareTo(T o) {
          // Always return -1 regardless of what the object is being compared to
          return -1;
        }

      };
    } else {
      this.startPoint = startPoint;
    }
  }

  public Iterator<T> iterator() {
    return new RBTIterator<T>(this.root, this.startPoint);
  }

  @Override
  protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
    if (newNode == null)
      throw new NullPointerException("new node cannot be null");

    if (this.root == null) {
      // add first node to an empty tree
      root = newNode;
      size++;
      return true;
    } else {
      // insert into subtree
      Node<T> current = this.root;
      while (true) {
        int compare = newNode.data.compareTo(current.data);
        // if (compare == 0) {
        // return false;
        // }
        // Modified so that a duplicate value gets inserted into left subtree
        if (compare <= 0) {
          // insert in left subtree
          if (current.down[0] == null) {
            // empty space to insert into
            current.down[0] = newNode;
            newNode.up = current;
            this.size++;
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.down[0];
          }
        } else {
          // insert in right subtree
          if (current.down[1] == null) {
            // empty space to insert into
            current.down[1] = newNode;
            newNode.up = current;
            this.size++;
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.down[1];
          }
        }
      }
    }
  }

  private static class RBTIterator<R> implements Iterator<R> {
    private Comparable<R> iteratorStartPoint;
    private Stack<Node<R>> stack;


    public RBTIterator(Node<R> root, Comparable<R> startPoint) {
      // not updating startPoint of RBTIterator class?
      this.iteratorStartPoint = startPoint;
      // Initializes an empty stack with Node<R> references
      stack = new Stack<Node<R>>();
      buildStackHelper(root);
    }

    private void buildStackHelper(Node<R> node) {
      // Base case as described in write up
      // Base case when 'node' argument provided is null i.e. previous node's left child is null
      // Note that this method might be called recursively on the right child of the recently
      // retrieved node in the next() method. If the right child in such case is null, this method
      // should not add any more elements to the stack.
      if (node == null) {
        return;
      } else {
        // If the data within the node argument is smaller than the start value, make a recursive
        // call on the right subtree
        if (iteratorStartPoint.compareTo(node.data) > 0) {
          buildStackHelper(node.down[1]);
        } else if (iteratorStartPoint.compareTo(node.data) <= 0) {
          // Go to left-most node in left subtree and store all the values along the way
          // So recursively call this method on left node until left subtree doesn't exist
          stack.push(node);
          buildStackHelper(node.down[0]);
        }


      }
    }

    public boolean hasNext() {
      // If the stack isn't empty, there are still more node's values to return
      return !stack.isEmpty();
    }

    public R next() {
      // If there are no more values in the stack and next() is called, throw a
      // NoSuchElementException
      if (stack.isEmpty()) {
        throw new NoSuchElementException();
      }
      Node<R> toReturn = stack.pop();
      // Note that we would have to call this method recursively on the right subtree of the
      // recently received value. If the right subtree is null, nothing more will be added to the
      // stack. If it isn't null, some nodes with values which are greater or equal to the value of
      // the startPoint will be added to the stack
      buildStackHelper(toReturn.down[1]);
      return toReturn.data;
    }
  }

  /**
   * Checks the correctness of the IteratbleRedBlackTree.iterator() for a RBT containing some
   * duplicate String values. Here, a starting point will also be provided to the iterator using
   * setIterationStartingPoint()
   */
  @Test
  public void P106Test1() {
    // First, check if the iterator.hasNext() is returning true for a RBT with multiple non-null
    // values
    IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();

    tree.insert("N");
    tree.insert("G");
    tree.insert("V");
    tree.insert("D");
    tree.insert("I");
    tree.insert("T");
    tree.insert("T");
    tree.insert("X");
    tree.insert("H");
    tree.insert("J");
    tree.insert("L");
    tree.insert("L");

    // Set the iteration start point to the letter "H"
    tree.setIterationStartPoint("H");
    
    Iterator<String> iterator = tree.iterator();

    // Fail test if iterator.hasNext() return false
    Assertions.assertTrue(iterator.hasNext());



    // Iterate throug all the elements in the iterator
    // Fail test if any of the returned String objects don't match the expected values

    Assertions.assertEquals("H", iterator.next());
    Assertions.assertEquals("I", iterator.next());
    Assertions.assertEquals("J", iterator.next());
    Assertions.assertEquals("L", iterator.next());
    Assertions.assertEquals("L", iterator.next());
    Assertions.assertEquals("N", iterator.next());
    Assertions.assertEquals("T", iterator.next());
    Assertions.assertEquals("T", iterator.next());
    Assertions.assertEquals("V", iterator.next());
    Assertions.assertEquals("X", iterator.next());

    // Fail test if iterator.hasNext() returns true after all the elements have been iterated
    // through

    Assertions.assertFalse(iterator.hasNext());

  }

  /**
   * Checks the correctness of the IterableRedBlackTree.iterator() for a RBT containig some
   * duplicate Integer values. Here, no starting point will be provided to the iterator.
   */
  @Test
  public void P106Test2() {
    // First, check if the iterator.hasNext() is returning true for a RBT with multiple non-null
    // values
    IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();

    Integer[] add = {14, 14, 7, 20, 4, 10, 18, 23, 23, 8, 12, 13};

    for (int i = 0; i < add.length; i++) {
      tree.insert(add[i]);
    }

    Iterator<Integer> iterator = tree.iterator();

    // Fail test if iterator.hasNext() return false
    Assertions.assertTrue(iterator.hasNext());

    // Iterate throug all the elements in the iterator
    // Fail test if any of the returned String objects don't match the expected values
    Assertions.assertEquals(4, iterator.next());
    Assertions.assertEquals(7, iterator.next());
    Assertions.assertEquals(8, iterator.next());
    Assertions.assertEquals(10, iterator.next());
    Assertions.assertEquals(12, iterator.next());
    Assertions.assertEquals(13, iterator.next());
    Assertions.assertEquals(14, iterator.next());
    Assertions.assertEquals(14, iterator.next());
    Assertions.assertEquals(18, iterator.next());
    Assertions.assertEquals(20, iterator.next());
    Assertions.assertEquals(23, iterator.next());
    Assertions.assertEquals(23, iterator.next());

    // Fail test if iterator.hasNext() returns true after all the elements have been iterated
    // through

    Assertions.assertFalse(iterator.hasNext());
  }

  /**
   * Checks the correctness of the IterableRedBlackTree.iterator() for a RBT containing some
   * non-duplicate Integer values. Here, a starting point will be provded to the iterator using
   * setIterationStartingPoint()
   */
  @Test
  public void P106Test3() {
    // First, check if the iterator.hasNext() is returning true for a RBT with multiple non-null
    // values
    IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();

    Integer[] add = {9, 5, 15, 1, 7, 12, 20, 3, 6, 8, 11, 13, 2};

    for (int i = 0; i < add.length; i++) {
      tree.insert(add[i]);
    }
    
    // Set the iteration start point to the Integer 8
    tree.setIterationStartPoint(8);
    

    Iterator<Integer> iterator = tree.iterator();



    // Fail test if iterator.hasNext() return false
    Assertions.assertTrue(iterator.hasNext());

    // Iterate throug all the elements in the iterator
    // Fail test if any of the returned String objects don't match the expected values

    // In sorted order, this is the expected Integer elements from the iterator
    Integer[] expected = {8, 9, 11, 12, 13, 15, 20};

    for (int j = 0; j < expected.length; j++) {

//      next = iterator.next();
//      System.out.println("DFD" + next);
//      System.out.println(expected[j] + " compared to " + next);
      Assertions.assertEquals(expected[j], iterator.next());
    }

    // Fail test if iterator.hasNext() returns true after all the elements have been iterated
    // through
    Assertions.assertFalse(iterator.hasNext());
  }



}
