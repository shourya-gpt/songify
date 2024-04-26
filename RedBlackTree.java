// -== CS400 Spring 2024 File Header Information ==-
// Name: Shourya Gupta
// Email: shourya.gupta@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

  protected static class RBTNode<T> extends Node<T> {
    public boolean isBlack = false;

    public RBTNode(T data) {
      super(data);
    }

    public RBTNode<T> getUp() {
      return (RBTNode<T>) this.up;
    }

    public RBTNode<T> getDownLeft() {
      return (RBTNode<T>) this.down[0];
    }

    public RBTNode<T> getDownRight() {
      return (RBTNode<T>) this.down[1];
    }
  }

  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> redNode) {
    // If the redNode's parent is null, then insert() will set redNode.isBlack = true.
    // If redNode's parent is black, then no RBT property is being violated
    if (redNode.getUp() == null || redNode.getUp().isBlack == true) {
      return;
    }

    // Initialize some local variables for help
    RBTNode<T> parent = redNode.getUp();
    // Is redNode's aunt a left or right child of redNode's grandparent?
    RBTNode<T> aunt;
    if (parent.getUp().getDownLeft() == parent) {
      // If parent's the left child, then aunt's the right child
      aunt = parent.getUp().getDownRight();
    } else {
      // If parent's the right child, then aunt's the left child
      aunt = parent.getUp().getDownLeft();
    }

    // If aunt is null or black, follow this algorithm
    if (aunt == null || aunt.isBlack == true) {
      // Check if grandparent has same relationship with parent as parent has with redNode (i.e. if
      // both are left or right children of their respective parents).
      if ((parent.getUp().getDownRight() == aunt && parent.getDownLeft() == redNode)
          || (parent.getUp().getDownLeft() == aunt && parent.getDownRight() == redNode)) {
        // Black aunt line case
        // Rotate and swap the colors of the parent and the grandparent
        this.rotate(parent, parent.getUp());
        parent.isBlack = true;
        if (parent.getDownLeft() == redNode) {
          parent.getDownRight().isBlack = false;
        } else {
          parent.getDownLeft().isBlack = false;
        }
        // Here, no recursive case exists
      } else {
        // Black aunt zig case
        // Rotate the redNode and the grandparent
        this.rotate(redNode, parent);
        // Recursively call enforceRBTreePropertiesAfterInsert(parent) as we are in Black aunt line
        // case
        enforceRBTreePropertiesAfterInsert(parent);
      }
    }
    // If aunt's color is red, follow this algorithm
    else {
      // Recolor the parent and the aunt to be black, and the grandparent to be red instead.
      // We know that the grandparent must be black initially.
      parent.isBlack = true;
      aunt.isBlack = true;
      parent.getUp().isBlack = false;

      // If grandparent's parent is null i.e. grandparent is the root node, exit
      // insert() will make grandparent black
      if (parent.getUp().getUp() == null) {
        return;
      }
      // If grandparent's parent is red, then we have a red-property violation
      // Recursively call enforceRBTPropertiesAfterInsert() to resolve
      else if (parent.getUp().getUp().isBlack == false) {
        enforceRBTreePropertiesAfterInsert(parent.getUp());
      }
    }



  }

  @Override
  public boolean insert(T data) throws NullPointerException {
    RBTNode<T> newNode = new RBTNode<T>(data);
    Boolean bool = this.insertHelper(newNode);
    // If any properties (the red parent one!) are being violated, fix them
    enforceRBTreePropertiesAfterInsert(newNode);
    // After the enforce algorithm, set root to black
    ((RBTNode<T>) root).isBlack = true;

    return bool;
  }

  /**
   * Checks insertion into a RBT where new node's aunt is a red node. Tests the recursive case when
   * enforceRBTreePropertiesAfterInsert() has to be called again.
   */
  @Test
  public void RBTtest1() {

    RedBlackTree<String> tree = new RedBlackTree<String>();

    tree.insert("N");
    tree.insert("G");
    tree.insert("V");
    tree.insert("D");
    tree.insert("I");
    tree.insert("T");
    tree.insert("X");
    tree.insert("H");
    tree.insert("J");

    // After this internal state, we will insert a new node with data L
    tree.insert("L");

    // Now, we will verify the tree's nodes' colors and their in-order and level-order traversals

    // Verify colors:
    Assertions.assertEquals(checkRedColor("GNLTX", (RBTNode<String>) this.root), true,
        "Problem with the color of nodes!");

    // Verify level-order traversal:
    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String[] expected = {"I", "G", "N", "D", "H", "J", "V", "L", "T", "X"};
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    Assertions.assertEquals(expectedString, tree.toLevelOrderString(),
        "Problem with level-order traversal!");
    // Verify in-order traversal:

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toInOrderString()
    expected = new String[] {"D", "G", "H", "I", "J", "L", "N", "T", "V", "X"};
    expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }
    Assertions.assertEquals(expectedString, tree.toInOrderString(),
        "Problem with in-order traversal");

  }

  /**
   * Recursive private helper method which returns true if the nodes corresponding to the data
   * provided as a paramter are all red. Returns false if any other node is found to be red.
   * 
   * @param str  the concatenation of the data values of nodes which should be red
   * @param node the node to check the color of
   * @return true iff the only red nodes in the tree are those corresponding to data in 'str'
   */
  private static boolean checkRedColor(String str, RBTNode<String> node) {

    if (node == null) {
      return true;
    }
    // The for loop and if condition check if node.data is a substring of str
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == node.data.charAt(0)) {
        // If the node isn't red, return false;
        if (node.isBlack != false) {
          return false;
        }
      } else {
        // If the node is red, return false;
        if (node.isBlack == false) {
          return false;
        }
      }
    }

    return checkRedColor(str, node.getDownLeft()) && checkRedColor(str, node.getDownRight());


  }

  /**
   * Checks insertion into a RBT where new node's aunt is a black node. This test method also checks
   * the case when new node is parent's right child and parent is grand's left child. In this case,
   * enforceRBTreePropertiesAfterInsert() has to be called recursively. Please note that the case
   * mentioned above is tested in one of the recursive casees that take place.
   */
  @Test
  public void RBTtest2() {

    RedBlackTree<String> tree = new RedBlackTree<String>();

    tree.insert("N");
    tree.insert("G");
    tree.insert("T");
    tree.insert("D");
    tree.insert("J");
    tree.insert("R");
    tree.insert("W");
    tree.insert("H");
    tree.insert("L");

    // After this internal state, we will insert a new node with data M
    tree.insert("M");

    // Now, we will verify the tree's nodes' colors and their in-order and level-order traversals

    // Verify colors:
    Assertions.assertEquals(checkRedColor("GNRW", (RBTNode<String>) this.root), true,
        "Problem with the color of nodes!");

    // Verify level-order traversal:
    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String[] expected = {"J", "G", "N", "D", "H", "L", "T", "M", "R", "W"};
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    Assertions.assertEquals(expectedString, tree.toLevelOrderString(),
        "Problem with level-order traversal!");

    // Verify in-order traversal:
    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toInOrderString()
    expected = new String[] {"D", "G", "H", "J", "L", "M", "N", "R", "T", "W"};
    expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }
    Assertions.assertEquals(expectedString, tree.toInOrderString(),
        "Problem with in-order traversal");

  }

  /**
   * Checks insertion into a RBT where new node's aunt is a null node. This test method also checks
   * the case when new node is parent's left child and parent is grand's right child. In this case,
   * enforceRBTreePropertiesAfterInsert() has to be called recursively.
   */
  @Test
  public void RBTtest3() {
    RedBlackTree<String> tree = new RedBlackTree<String>();

    tree.insert("I");
    tree.insert("E");
    tree.insert("O");
    tree.insert("A");
    tree.insert("G");
    tree.insert("L");
    tree.insert("T");
    tree.insert("C");
    tree.insert("F");
    tree.insert("H");
    tree.insert("K");
    tree.insert("M");

    // After this internal state, we will insert a new node with data B
    tree.insert("B");

    // Now, we will verify the tree's nodes' colors and their in-order and level-order traversals

    // Verify colors:
    Assertions.assertEquals(checkRedColor("LAC", (RBTNode<String>) this.root), true,
        "Problem with the color of nodes!");

    // Verify level-order traversal:
    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toLevelOrderString()
    String[] expected = {"I", "E", "O", "B", "G", "L", "T", "A", "C", "F", "H", "K", "M"};
    String expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }

    Assertions.assertEquals(expectedString, tree.toLevelOrderString(),
        "Problem with level-order traversal!");

    // Verify in-order traversal:

    // Using a for loop, this array is represented using a String to easily compare it to the return
    // value of .toInOrderString()
    expected = new String[] {"A", "B", "C", "E", "F", "G", "H", "I", "K", "L", "M", "O", "T"};
    expectedString = "[ ";
    for (int i = 0; i < expected.length; i++) {
      if (i != expected.length - 1) {
        expectedString += expected[i] + ", ";
      } else {
        expectedString += expected[i] + " ]";
      }

    }
    Assertions.assertEquals(expectedString, tree.toInOrderString(),
        "Problem with in-order traversal");

  }

  public static void main(String[] args) {

  }

}

