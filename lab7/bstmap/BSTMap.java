package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    /** Every K-V pair store in one BSTNode. */
    private class BSTNode<K, V> {
        private K key;
        private V val;
        private BSTNode left;
        private BSTNode right;

        private BSTNode(K key, V val) {
            this.key = key;
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }

    /** The root of the BST. */
    private BSTNode root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    /** Helper method of containskey(). */
    private boolean containsKey(K key, BSTNode root) {
        if (root == null) {
            return false;
        } else if (key.compareTo((K) root.key) < 0) {
            return containsKey(key, root.left);
        } else if (key.compareTo((K) root.key) > 0) {
            return containsKey(key, root.right);
        }
        return true;
    }

    @Override
    /** Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(key, root);
    }

    /** Helper method of get(). */
    private V get(K key, BSTNode root) {
        if (root == null) {
            return null;
        } else if (key.compareTo((K) root.key) < 0) {
            return (V) get(key, root.left);
        } else if (key.compareTo((K) root.key) > 0) {
            return (V) get(key, root.right);
        }
        return (V) root.val;
    }

    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    /** Helper method of put(). */
    private BSTNode put(K key, V value, BSTNode root) {
        if (root == null) {
            size += 1;
            return new BSTNode(key, value);
        } else if (key.compareTo((K) root.key) < 0) {
            root.left = put(key, value, root.left);
        } else if (key.compareTo((K) root.key) > 0) {
            root.right = put(key, value, root.right);
        }
        return root;
    }

    @Override
    /** Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't impleapment this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Sorry, I haven't finish keySet() method");
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException("Sorry, I haven't finish remove(K key) method");
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Sorry, I haven't finish remove(K key, V value) method");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Sorry, I haven't finish iterator() method");
    }

    /** Print the map in order. */
    public void printInOrder() {
        printInOrder(root);
    }

    /** Helper method of printInOrder(). */
    private void printInOrder(BSTNode root) {
        if (root.left != null) {
            printInOrder(root.left);
        }
        System.out.println(root.key + " -- " + root.val);
        if (root.right != null) {
            printInOrder(root.right);
        }
    }

}
