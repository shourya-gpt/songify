import java.util.LinkedList;
import java.util.Stack;


/**
 * Binary Search Tree implementation with a Node inner class for representing the nodes of the tree.
 * We will turn this Binary Search Tree into a self-balancing tree as part of project 1 by modifying
 * its insert functionality. In week 0 of project 1, we will start this process by implementing tree
 * rotations.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

  /**
   * This class represents a node holding a single value within a binary tree.
   */
  protected static class Node<T> {
    public T data;

    // up stores a reference to the node's parent
    public Node<T> up;
    // The down array stores references to the node's children:
    // - down[0] is the left child reference of the node,
    // - down[1] is the right child reference of the node.
    // The @SupressWarning("unchecked") annotation is use to supress an unchecked
    // cast warning. Java only allows us to instantiate arrays without generic
    // type parameters, so we use this cast here to avoid future casts of the
    // node type's data field.
    @SuppressWarnings("unchecked")
    public Node<T>[] down = (Node<T>[]) new Node[2];

    public Node(T data) {
      this.data = data;
    }

    /**
     * @return true when this node has a parent and is the right child of that parent, otherwise
     *         return false
     */
    public boolean isRightChild() {
      return this.up != null && this.up.down[1] == this;
    }

  }

  protected Node<T> root; // reference to root node of tree, null when empty
  protected int size = 0; // the number of values in the tree

  /**
   * Inserts a new data value into the tree. This tree will not hold null references, nor duplicate
   * data values.
   * 
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided data argument is null
   */
  public boolean insert(T data) throws NullPointerException {
    if (data == null)
      throw new NullPointerException("Cannot insert data value null into the tree.");
    return this.insertHelper(new Node<>(data));
  }

  /**
   * Performs a naive insertion into a binary search tree: adding the new node in a leaf position
   * within the tree. After this insertion, no attempt is made to restructure or balance the tree.
   * 
   * @param node the new node to be inserted
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided node is null
   */
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
        if (compare == 0) {
          return false;
        } else if (compare < 0) {
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

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will throw
   * an IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
    // If the this.child is a right child of this.parent
    // Here, we perform a left rotation
    if (parent.down[1] == child && child.up == parent) {

      // If child's left subtree exists, it becomes right subtree of parent, otherwise set right
      // subtree of parent to null
      if (child.down[0] != null) {
        parent.down[1] = child.down[0];
        parent.down[1].up = parent;
      } else {
        parent.down[1] = null;
      }

      // Note that we haven't yet set child.down[0] to null because it will be reassigned

      // Set this.child's parent to parent's parent (might be null if root node)
      child.up = parent.up;

      // Also make sure that down reference is properly set
      if (child.up != null) {
        if (child.up.down[0] == parent) {
          child.up.down[0] = child;
        } else {
          child.up.down[1] = child;
        }
      }


      // Now, the parent-child relationship converses. The child's left subtree becomes parent.
      child.down[0] = parent;
      child.down[0].up = child;

      // If this.parent is the root node, this.child should become the root node.
      if (child.up == null) {
        this.root = child;
      }

    }
    // If the child is a left child of the parent
    // Here, we perform a right rotation
    else if (parent.down[0] == child && child.up == parent) {

      // If the child has a right subtree, it becomes the left subtree of the parent. If not,
      // child's right subtree becomes null.
      if (child.down[1] != null) {
        parent.down[0] = child.down[1];
        parent.down[0].up = parent;
      } else {
        parent.down[0] = null;
      }

      // Note that we haven't yet set child.down[0] to null because it will be reassigned

      // Set this.child's parent to parent's parent (might be null if root node)
      child.up = parent.up;

      // Also make sure that down reference is properly set
      if (child.up != null) {
        if (child.up.down[0] == parent) {
          child.up.down[0] = child;
        } else {
          child.up.down[1] = child;
        }
      }

      // Now, the parent-child relationship converses. The child's right subtree becomes parent.
      child.down[1] = parent;
      child.down[1].up = child;

      // If this.parent is the root node, this.child should become the root node.
      if (child.up == null) {
        this.root = child;
      }


    }
    // The provided child and parent node references do not have a parent-child relationship. We
    // know this because either the child node is a root node or the child node doesn't have the
    // this.parent node as a parent
    else {
      throw new IllegalArgumentException(
          "The provided child and parent references do not have a parent-child relationship");
    }

  }

  /**
   * Get the size of the tree (its number of nodes).
   * 
   * @return the number of nodes in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * 
   * @return true of this.size() returns 0, false if this.size() != 0
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Checks whether the tree contains the value *data*.
   * 
   * @param data a comparable for the data value to check for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  public boolean contains(Comparable<T> data) {
    // null references will not be stored within this tree
    if (data == null) {
      throw new NullPointerException("This tree cannot store null references.");
    } else {
      Node<T> nodeWithData = this.findNode(data);
      // return false if the node is null, true otherwise
      return (nodeWithData != null);
    }
  }

  /**
   * Removes all keys from the tree.
   */
  public void clear() {
    this.root = null;
    this.size = 0;
  }

  /**
   * Helper method that will return the node in the tree that contains a specific key. Returns null
   * if there is no node that contains the key.
   * 
   * @param data the data value for which we want to find the node that contains it
   * @return the node that contains the data value or null if there is no such node
   */
  protected Node<T> findNode(Comparable<T> data) {
    Node<T> current = this.root;
    while (current != null) {
      int compare = data.compareTo(current.data);
      if (compare == 0) {
        // we found our value
        return current;
      } else if (compare < 0) {
        if (current.down[0] == null) {
          // we have hit a null node and did not find our node
          return null;
        }
        // keep looking in the left subtree
        current = current.down[0];
      } else {
        if (current.down[1] == null) {
          // we have hit a null node and did not find our node
          return null;
        }
        // keep looking in the right subtree
        current = current.down[1];
      }
    }
    return null;
  }

  /**
   * This method performs an inorder traversal of the tree. The string representations of each data
   * value within this tree are assembled into a comma separated string within brackets (similar to
   * many implementations of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
   * 
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    int nodesVisited = 0;
    if (this.root != null) {
      Stack<Node<T>> nodeStack = new Stack<>();
      Node<T> current = this.root;
      while (!nodeStack.isEmpty() || current != null) {
        if (current == null) {
          Node<T> popped = nodeStack.pop();
          if (++nodesVisited > this.size()) {
            throw new RuntimeException(
                "visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
          }
          sb.append(popped.data.toString());
          if (!nodeStack.isEmpty() || popped.down[1] != null)
            sb.append(", ");
          current = popped.down[1];
        } else {
          nodeStack.add(current);
          current = current.down[0];
        }
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  /**
   * This method performs a level order traversal of the tree. The string representations of each
   * data value within this tree are assembled into a comma separated string within brackets
   * (similar to many implementations of java.util.Collection). This method will be helpful as a
   * helper for the debugging and testing of your rotation implementation.
   * 
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    int nodesVisited = 0;
    if (this.root != null) {
      LinkedList<Node<T>> q = new LinkedList<>();
      q.add(this.root);
      while (!q.isEmpty()) {
        Node<T> next = q.removeFirst();
        if (++nodesVisited > this.size()) {
          throw new RuntimeException(
              "visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
        }
        if (next.down[0] != null)
          q.add(next.down[0]);
        if (next.down[1] != null)
          q.add(next.down[1]);
        sb.append(next.data.toString());
        if (!q.isEmpty())
          sb.append(", ");
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  public String toString() {
    return "level order: " + this.toLevelOrderString() + "\nin order: " + this.toInOrderString();
  }

  // Implement at least 3 tests using the methods below. You can
  // use your notes from lecture for ideas of rotation examples to test with.
  // Make sure to include rotations at the root of a tree in your test cases.
  // Give each of the methods a meaningful header comment that describes what is being
  // tested and make sure your tests have inline comments that help with reading your test code.
  // If you'd like to add additional tests, then name those methods similar to the ones given below.
  // Eg: public static boolean test4() {}
  // Do not change the method name or return type of the existing tests.
  // You can run your tests through the static main method of this class.

  /**
   * This test method confirms that the rotate() method throws an IllegalArgumentException when
   * given a non parent-child pair of nodes.
   * 
   * @return true if the test passes, false otherwise
   */
  public static boolean test1() {

    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();

    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);

    try {
      bst.rotate(bst.findNode(28), bst.findNode(55));
      return false;
    } catch (IllegalArgumentException e) {
      return true;
    }


  }

  /**
   * 
   * This test method compares the level-order traversal string of a BST on which a left rotation
   * has been performed using the .rotate() method to the expected level-order string. Here, the
   * parent node being provided is the root node.
   * 
   * @return true if the test passes, false otherwise
   */
  public static boolean test2() {

    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();

    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 62 and 43
    bst.rotate(bst.findNode(62), bst.findNode(43));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {62, 43, 77, 28, 55, 20, 34};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }


    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;
  }

  /**
   * This test method compares the level-order traversal string of a BST on which a right rotation
   * has been performed using the .rotate() method to the expected level-order string. Here, the
   * parent node being provided is the root node.
   * 
   * @return true if the test passes, false otherwise
   */
  public static boolean test3() {

    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();


    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 28 and 43
    bst.rotate(bst.findNode(28), bst.findNode(43));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {28, 20, 43, 34, 62, 55, 77};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;
  }

  /**
   * This test method compares the level-order traversal string of another BST on which a left
   * rotation has been performed using the .rotate() method to the expected level-order string.
   * 
   * @return true if the test passes, false otherwise.
   */
  public static boolean test4() {

    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77, 30
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();


    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);
    bst.insert(30);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 34 and 28
    bst.rotate(bst.findNode(34), bst.findNode(28));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {43, 34, 62, 28, 55, 77, 20, 30};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }


    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;

  }

  /**
   * This test method compares the level-order traversal string of another BST on which a left
   * rotation has been performed using the .rotate() method to the expected level-order string.
   * Here, no middle value exists between the values of the parent-child node pair.
   * 
   * @return true is the test passes, false otherwise
   */
  public static boolean test5() {


    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();


    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 77 and 62
    bst.rotate(bst.findNode(77), bst.findNode(62));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {43, 28, 77, 20, 34, 62, 55};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }


    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;
  }

  /**
   * This test method compares the level-order traversal string of another BST on which a right
   * rotation has been performed using the .rotate() method to the expected level-order string.
   * 
   * @return true if the test passes, false otherwise.
   */
  public static boolean test6() {


    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77, 57
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();


    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);
    bst.insert(57);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 55 and 62
    bst.rotate(bst.findNode(55), bst.findNode(62));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {43, 28, 55, 20, 34, 62, 57, 77};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;

  }

  /**
   * This test method compares the level-order traversal string of another BST on which a right
   * rotation has been performed using the .rotate() method to the expected level-order string.
   * Here, no middle value exists between the values of the parent-child node pair.
   * 
   * @return true is the test passes, false otherwise
   */
  public static boolean test7() {

    // Creating a BST with the level-order traversal: 43, 28, 62, 20, 34, 55, 77
    BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();


    bst.insert(43);
    bst.insert(28);
    bst.insert(62);
    bst.insert(20);
    bst.insert(34);
    bst.insert(55);
    bst.insert(77);

    // In-order traversal before .rotate() has been performed used for comparison
    String orderBeforeRotate = bst.toInOrderString();

    // Using .rotate(Node<T> child, Node<T> parent), we will perform a roation of the nodes with
    // value 20 and 28
    bst.rotate(bst.findNode(20), bst.findNode(28));

    // Actual level-order traversal after .rotate() has been performed used for comparison
    String afterRotate = bst.toLevelOrderString();

    // In-order traversal after .rotate() has been performed used for comparison
    String orderAfterRotate = bst.toInOrderString();

    // Create a new array with expected level-order traversal after the rotation has been performed
    Integer[] expected = new Integer[] {43, 20, 62, 28, 55, 77, 34};

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    // If the rotation has been perfomed correctly and if proper modifications have been made,
    // afterRotate and expectedString should be exactly identical.

    if (expectedString.compareTo(afterRotate) != 0
        || orderBeforeRotate.compareTo(orderAfterRotate) != 0) {
      return false;
    }

    return true;
  }


  /**
   * Main method to run tests. If you'd like to add additional test methods, add a line for each of
   * them.
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("Test 1 passed: " + test1());
    System.out.println("Test 2 passed: " + test2());
    System.out.println("Test 3 passed: " + test3());
    System.out.println("Test 4 passed: " + test4());
    System.out.println("Test 5 passed: " + test5());
    System.out.println("Test 6 passed: " + test6());
    System.out.println("Test 7 passed: " + test7());

  }

}
