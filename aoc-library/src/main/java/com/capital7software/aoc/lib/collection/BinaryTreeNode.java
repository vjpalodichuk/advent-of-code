package com.capital7software.aoc.lib.collection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The BinaryTreeNode&lt;T&gt; provides a node for a binary tree with a reference
 * to a T object that implements the Comparable interface. The BinaryTreeNode&lt;T&gt;
 * implements the Comparable interface and as such can be used in a Binary Search Tree.
 *
 * <p><br>
 * <b>Important Notes:</b>
 * <ul>
 * <li>T must implement the Comparable interface.</li>
 * <li>T should override Object.toString() so that the print and log methods
 * output useful information.</li>
 * <li>Beyond Int.MAX_VALUE elements, treeSize is wrong.</li>
 * </ul>
 *
 * @param <T> The type of the value held by a BinaryTreeNode.
 * @author Vincent J Palodichuk
 *     <a href="mailto:vincent@capital7software.com"> (e-mail me) </a>
 * @version 01/02/2024
 */
public class BinaryTreeNode<T extends Comparable<T>>
    implements TreeNode<T>, Comparable<BinaryTreeNode<T>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(BinaryTreeNode.class);
  private static final String INDENT = "    ";
  private static final String DASH = "--";
  private BinaryTreeNode<T> left;
  private BinaryTreeNode<T> right;
  private BinaryTreeNode<T> parent;
  private T value;

  /**
   * Initializes this node with no value, no parent, and no links to child nodes.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The new node contains no value, no parent, and no links to child nodes.</li>
   * </ul>
   */
  public BinaryTreeNode() {
    this(null, null, null, null);
  }

  /**
   * Initializes this node with no value, no parent, and no links to child nodes.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The new node contains no value, no parent, and no links to child nodes.</li>
   * </ul>
   *
   * @param value The initial value of the node.
   */
  public BinaryTreeNode(T value) {
    this(value, null, null, null);
  }

  /**
   * Initializes this node with specified value and links to child nodes. All parameters
   * may be null.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The new node contains the specified value and links to child nodes.</li>
   * </ul>
   *
   * @param value The initial value of the node.
   * @param left  The initial left child.
   * @param right The initial right child.
   */
  @SuppressFBWarnings
  public BinaryTreeNode(
      final T value,
      final BinaryTreeNode<T> left,
      final BinaryTreeNode<T> right
  ) {
    this(value, null, left, right);
  }

  /**
   * Initializes this node with specified value, parent, and links to child nodes. All parameters
   * may be null.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The new node contains the specified value, parent, and links to child nodes.</li>
   * </ul>
   *
   * @param value  The initial value of the node.
   * @param parent The initial parent.
   * @param left   The initial left child.
   * @param right  The initial right child.
   */
  @SuppressFBWarnings
  public BinaryTreeNode(
      T value,
      BinaryTreeNode<T> parent,
      BinaryTreeNode<T> left,
      BinaryTreeNode<T> right
  ) {
    this.value = value;
    this.parent = parent;
    this.left = left;
    this.right = right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BinaryTreeNode<?> that)) {
      return false;
    }
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public int compareTo(final @NotNull BinaryTreeNode<T> other) {
    return compareTo(value, other.value);
  }

  /**
   * A class helper method to compare two T objects.
   *
   * @param value1 The first value to compare.
   * @param value2 The second value to compare.
   * @return -1 if value1 is less than value2, 0 if they are equal, and 1 if value2 is greater
   *     than value1.
   * @see Comparable#compareTo(Object)
   */
  private static <T extends Comparable<T>> int compareTo(final T value1, final T value2) {
    // If only one argument is null
    if ((value1 == null) ^ (value2 == null)) {
      return (value1 == null) ? -1 : 1;
    } else if (value1 == null) { // both are null
      return 0;
    } else { // both are not null
      return value1.compareTo(value2);
    }
  }

  @Override
  @SuppressFBWarnings
  public BinaryTreeNode<T> getLeft() {
    return left;
  }

  @Override
  @SuppressFBWarnings
  public BinaryTreeNode<T> getRight() {
    return right;
  }

  /**
   * An accessor method to get the value of the left most node of the tree below this node.
   *
   * @return The T value from the deepest node that can be reached from this node
   *     following the left children.
   */
  public T getLeftMostValue() {
    if (left == null) {
      return value;
    } else {
      return left.getLeftMostValue();
    }
  }

  /**
   * An accessor method to get the value of the right most node of the tree below this node.
   *
   * @return The T value from the deepest node that can be reached from this node
   *     following the right children.
   */
  public T getRightMostValue() {
    if (right == null) {
      return value;
    } else {
      return right.getRightMostValue();
    }
  }

  @Override
  public T getValue() {
    return value;
  }

  /**
   * Traverses the tree in-order to print the T value from each node at or below this node
   * of the binary tree.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The T value of this node and all its descendants have been
   * written by LOGGER.info in in-order.</li>
   * </ul>
   */
  public void inorderPrint() {
    if (left != null) {
      left.inorderPrint();
    }

    LOGGER.info(Objects.toString(value));

    if (right != null) {
      right.inorderPrint();
    }
  }

  public boolean isLeaf() {
    return (left == null) && (right == null);
  }

  /**
   * Traverses the tree post-order to print the T value from each node at or below this
   * node of the binary tree.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The T value of this node and all its descendants have been
   * written by LOGGER.info in post-order.</li>
   * </ul>
   */
  public void postorderPrint() {
    if (left != null) {
      left.postorderPrint();
    }

    if (right != null) {
      right.postorderPrint();
    }

    LOGGER.info(Objects.toString(value));
  }

  /**
   * Traverses the tree pre-order to print the T value from each node at
   * or below this node of the binary tree.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The T value of this node and all its descendants have been
   * written by LOGGER.info in pre-order.</li>
   * </ul>
   */
  public void preorderPrint() {
    LOGGER.info(Objects.toString(value));

    if (left != null) {
      left.preorderPrint();
    }

    if (right != null) {
      right.preorderPrint();
    }
  }

  /**
   * Uses an in-order traversal to print the T value from each node at or
   * below this node of the binary tree, with indentation to indicate the
   * depth of each node.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The T value of this node and all its descendants have been
   * written by LOGGER.info using an in-order traversal.</li>
   * <li>The indentation of each line of T values is four times its depth
   * in the tree.</li>
   * <li>A dash (--) is printed at any place where a child has no sibling.
   * </li>
   * </ul>
   *
   * <p><br>
   * <b>Notes:</b>
   * <ul>
   * <li>Should be called from the root node to print the entire tree.</li>
   * </ul>
   */
  public void print() {
    print(0);
  }

  /**
   * Uses an in-order traversal to print the T value from each node at or
   * below this node of the binary tree, with indentation to indicate the
   * depth of each node.
   *
   * <p><br>
   * <b>Precondition:</b>
   * <ul>
   * <li>The parameter depth is equal to the depth of this node in the
   * tree.</li>
   * </ul>
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The T value of this node and all its descendants have been
   * written by LOGGER.info using an in-order traversal.</li>
   * <li>The indentation of each line of T values is four times its depth
   * in the tree.</li>
   * <li>A dash (--) is printed at any place where a child has no sibling.
   * </li>
   * </ul>
   *
   * @param depth The depth of this node (with 0 for the root, 1 for the root's
   *              children,...,n for the nth child).
   */
  public void print(int depth) {
    // Print the indentation and the data from the current node:
    LOGGER.info(INDENT.repeat(depth));

    LOGGER.info(Objects.toString(value));

    // Print the left subtree (or a dash if there is a right child and no left child).
    if (left != null) {
      left.print(depth + 1);
    } else if (right != null) {
      LOGGER.info(INDENT.repeat(depth));
      LOGGER.info(DASH);
    }

    // Print the right subtree (or a dash if there is a left child and no right child).
    if (right != null) {
      right.print(depth + 1);
    } else if (left != null) {
      LOGGER.info(INDENT.repeat(depth));

      LOGGER.info(DASH);
    }
  }

  /**
   * Remove the left most node of the tree with this node as its root.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The tree starting at this node has had its left most node (i.e.,
   * the deepest node that can be reached by following left children)
   * removed.</li>
   * <li>The return value is a reference to the root of the new (smaller)
   * tree.</li>
   * <li>This return value could be null if the original tree has only
   * one node (since that one node has now been removed).</li>
   * </ul>
   *
   * <p><br>
   * <b>Example:</b><br>
   * <code>
   * BinaryTreeNode&lt;String&gt; root = new BinaryTreeNode&lt;&gt;();<br>
   * <br>
   * // Build up a small binary tree of names.<br>
   * root.setLeft(new BinaryTreeNode&lt;&gt;("Mary", null, null));<br>
   * root.setRight(new BinaryTreeNode&lt;&gt;("Nancy", null, null));<br>
   * root.getLeft().setLeft(new BinaryTreeNode&lt;&gt;("Laura", null, null));<br>
   * root.getLeft().setRight(new BinaryTreeNode&lt;&gt;("Mindy", null, null));<br>
   * root.getRight().setLeft(new BinaryTreeNode&lt;&gt;("Miranda", null, null));<br>
   * root.getRight().setRight(new BinaryTreeNode&lt;&gt;("Oprah", null, null));<br>
   * <br>
   * // Remove Miranda from the tree.<br>
   * root.setRight(root.getRight().removeLeftMost());<br>
   * </code>
   *
   * @return A reference to the root of the new (smaller) tree.
   */
  @SuppressFBWarnings
  public BinaryTreeNode<T> removeLeftMost() {
    if (left == null) { // The left most node is at the root because there is no left child.
      return right;
    } else { // A recursive call removes the left most node from this node's left child.
      left = left.removeLeftMost();
      return this;
    }
  }

  /**
   * Remove the right most node of the tree with this node as its root.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>The tree starting at this node has had its right most node (i.e.,
   * the deepest node that can be reached by following right children)
   * removed.</li>
   * <li>The return value is a reference to the root of the new (smaller)
   * tree.</li>
   * <li>This return value could be null if the original tree has only
   * one node (since that one node has now been removed).</li>
   * </ul>
   *
   * <p><br>
   * <b>Example:</b><br>
   * <code>
   * BinaryTreeNode&lt;String&gt; root = new BinaryTreeNode&lt;&gt;();<br>
   * <br>
   * // Build up a small binary tree of names.<br>
   * root.setLeft(new BinaryTreeNode&lt;&gt;("Mary", null, null));<br>
   * root.setRight(new BinaryTreeNode&lt;&gt;("Nancy", null, null));<br>
   * root.getLeft().setLeft(new BinaryTreeNode&lt;&gt;("Laura", null, null));<br>
   * root.getLeft().setRight(new BinaryTreeNode&lt;&gt;("Mindy", null, null));<br>
   * root.getRight().setLeft(new BinaryTreeNode&lt;&gt;("Miranda", null, null));<br>
   * root.getRight().setRight(new BinaryTreeNode&lt;&gt;("Oprah", null, null));<br>
   * <br>
   * // Remove Mindy from the tree.<br>
   * root.setLeft(root.getLeft().removeRightMost());<br>
   * </code>
   *
   * @return A reference to the root of the new (smaller) tree.
   */
  @SuppressFBWarnings
  public BinaryTreeNode<T> removeRightMost() {
    if (right == null) { // The right most node is at the root because there is no right child.
      return left;
    } else { // A recursive call removes the right most node from this node's right child.
      right = right.removeRightMost();
      return this;
    }
  }

  /**
   * Copy a binary tree.
   *
   * @param source A reference to the root node of the tree to copy. source may be null.
   * @param <T>    The type of the value stored in the binary tree.
   * @return A reference to the root node of the new tree starting at source.
   * @throws OutOfMemoryError Indicates that there is insufficient memory for the new tree.
   */
  public static <T extends Comparable<T>> BinaryTreeNode<T> treeCopy(BinaryTreeNode<T> source) {
    BinaryTreeNode<T> leftCopy;
    BinaryTreeNode<T> rightCopy;

    if (source == null) {
      return null;
    } else {
      leftCopy = treeCopy(source.left);
      rightCopy = treeCopy(source.right);
      return new BinaryTreeNode<>(source.value, leftCopy, rightCopy);
    }
  }

  /**
   * Computes the actual number of nodes in a binary tree.
   *
   * <p><br>
   * <b>Note:</b>
   * <ul>
   * <li>A wrong answer occurs for trees larger than Int.MAX_VALUE.</li>
   * </ul>
   *
   * @param root The reference to the root of a binary tree (which may be an empty
   *             tree with a null root).
   * @param <T>  The type of the value stored in the binary tree.
   * @return The actual number of nodes in the tree with the given root node.
   */
  public static <T extends Comparable<T>> int treeSize(TreeNode<T> root) {
    if (root == null) {
      return 0;
    } else {
      return 1 + treeSize(root.getLeft()) + treeSize(root.getRight());
    }
  }

  @Override
  @SuppressFBWarnings
  public TreeNode<T> getParent() {
    return parent;
  }

  @Override
  @SuppressFBWarnings
  public void setLeft(TreeNode<T> left) {
    this.left = (BinaryTreeNode<T>) left;
  }

  @Override
  @SuppressFBWarnings
  public void setParent(TreeNode<T> parent) {
    this.parent = (BinaryTreeNode<T>) parent;
  }

  @Override
  @SuppressFBWarnings
  public void setRight(TreeNode<T> right) {
    this.right = (BinaryTreeNode<T>) right;
  }

  @Override
  public void setValue(T value) {
    this.value = value;
  }
}
