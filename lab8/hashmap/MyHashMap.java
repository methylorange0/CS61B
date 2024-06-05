package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private int bucketsNum;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        size = 0;
        bucketsNum = 16;
        loadFactor = 0.75;
        buckets = createTable(bucketsNum);
    }

    public MyHashMap(int initialSize) {
        size = 0;
        bucketsNum = initialSize;
        loadFactor = 0.75;
        buckets = createTable(bucketsNum);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        bucketsNum = initialSize;
        loadFactor = maxLoad;
        buckets = createTable(bucketsNum);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for(int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        size = 0;
        for (int i = 0; i < bucketsNum; i++) {
            buckets[i] = null;
        }
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), bucketsNum);
        Collection<Node> theCollection = buckets[index];
        if (theCollection == null) {
            return false;
        }
        Iterator<Node> seer = theCollection.iterator();
        while(seer.hasNext()) {
            if (seer.next().key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), bucketsNum);
        Collection<Node> theCollection = buckets[index];
        if (theCollection == null) {
            return null;
        }
        Iterator<Node> seer = theCollection.iterator();
        while(seer.hasNext()) {
            Node which = seer.next();
            if (which.key.equals(key)) {
                return which.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), bucketsNum);
        Node thisOne = createNode(key, value);
        Collection<Node> theCollection = buckets[index];
        Iterator<Node> seer = theCollection.iterator();
        while(seer.hasNext()) {
            Node which = seer.next();
            if (which.key.equals(key)) {
                which.value = value;
                return;
            }
        }
        theCollection.add(thisOne);
        size += 1;
        if ((double)size / (double) bucketsNum >= loadFactor) {
            resize();
        }
    }

    /** Resize the table. */
    private void resize() {
        Collection<Node>[] newBuckets = createTable(bucketsNum * 2);
        for (int i = 0; i < bucketsNum; i++) {
            Collection<Node> theCollection = buckets[i];
            Iterator<Node> seer = theCollection.iterator();
            while(seer.hasNext()) {
                Node theNode = seer.next();
                int index = Math.floorMod(theNode.key.hashCode(), bucketsNum * 2);
                newBuckets[index].add(theNode);
            }
        }
        bucketsNum *= 2;
        buckets = newBuckets;
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        for (int i = 0; i < bucketsNum; i++) {
            Collection<Node> theCollection = buckets[i];
            Iterator<Node> seer = theCollection.iterator();
            while(seer.hasNext()) {
                result.add(seer.next().key);
            }
        }
        return result;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
