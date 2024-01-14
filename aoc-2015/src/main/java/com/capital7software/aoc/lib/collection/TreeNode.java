package com.capital7software.aoc.lib.collection;


/**
 * Represents a node in a BinaryTree.
 * 
 * @author Vincent J Palodichuk
 * <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 *
 * @param <T> The type of the value held by a TreeNode.
 */
public interface TreeNode<T extends Comparable<T>> {
    /**
     * Gets the left child of this node.
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null reference is returned if this node has no left child.</li>
     * </ul>
     *
     * @return The left child for this node, which may not exist.
     */
    TreeNode<T> getLeft();

    /**
     * Gets the parent of this node.
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null reference is returned if this node has no parent.</li>
     * </ul>
     *
     * @return The parent for this node, which may not exist.
     */
    TreeNode<T> getParent();

    /**
     * Gets the right child of this node.
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null reference is returned if this node has no right child.</li>
     * </ul>
     *
     * @return The right child for this node, which may not exist.
     */
    TreeNode<T> getRight();

    /**
     * Gets the value of this node.
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null reference is returned if this node has no value.</li>
     * </ul>
     *
     * @return The value for this node, which may not exist.
     */
    T getValue();

    /**
     * Determines whether this node is a leaf. This node is
     * a leaf if it has no children.
     *
     * @return Returns true if this node is a leaf; false otherwise.
     */
    boolean isLeaf();

    /**
     * Sets the left child of this node.
     * <p>
     * <b>Postcondition:</b>
     * <ul>
     * <li>This node's left child has been set to the specified node.</li>
     * </ul>
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null value for <code>left</code> indicates this node has no left child.</li>
     * </ul>
     *
     * @param left The new left child for this node, which may be null.
     */
    void setLeft(TreeNode<T> left);

    /**
     * Sets the parent of this node.
     * <p>
     * <b>Postcondition:</b>
     * <ul>
     * <li>This node's parent has been set to the specified node.</li>
     * </ul>
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null value for <code>parent</code> indicates this node has no parent.</li>
     * </ul>
     *
     * @param parent The new parent for this node, which may be null.
     */
    void setParent(TreeNode<T> parent);

    /**
     * Sets the right child of this node.
     * <p>
     * <b>Postcondition:</b>
     * <ul>
     * <li>This node's right child has been set to the specified node.</li>
     * </ul>
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null value for <code>right</code> indicates this node has no right child.</li>
     * </ul>
     *
     * @param right The new right child for this node, which may be null.
     */
    void setRight(TreeNode<T> right);

    /**
     * Sets the value of this node.
     * <p>
     * <b>Postcondition:</b>
     * <ul>
     * <li>This node's T value has been set to the specified value.</li>
     * </ul>
     * <p>
     * <b>Notes:</b>
     * <ul>
     * <li>A null value for <code>value</code> indicates this node has no T value.</li>
     * </ul>
     *
     * @param value The new T value for this node, which may be null.
     */
    void setValue(T value);
}
