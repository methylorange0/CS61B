package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    /** T[] is the place where data is stored,
     * and size is the size of the deque. */
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int initLength = 8;

    /** Create an empty ArrayDeque. */
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[initLength];
        nextlast = 0;
        nextfirst = items.length - 1;
    }

    /** Rearrange the capacity of the array. */
    private void reSize(int cap) {
        T[] newItems = (T[]) new Object[cap];
        int j = 0;
        for (int i = (nextfirst + 1) % items.length; j < size; i = (i + 1) % items.length) {
            newItems[j] = items[i];
            j += 1;
        }
        nextlast = size;
        nextfirst = newItems.length - 1;
        items = newItems;
    }

    @Override
    /** Adds an item to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            reSize(size * 2);
        }
        items[nextfirst] = item;
        nextfirst = (nextfirst + items.length - 1) % items.length;
        size += 1;
    }

    @Override
    /** Adds an item to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            reSize(size * 2);
        }
        items[nextlast] = item;
        nextlast = (nextlast + 1) % items.length;
        size += 1;
    }


    @Override
    /** Return the size of deque. */
    public int size() {
        return size;
    }

    @Override
    /** Prints the items in the deque from first to the last. */
    public void printDeque() {
        int j = 0;
        int i = 0;
        for (i = (nextfirst + 1) % items.length; j < size - 1; i = (i + 1) % items.length) {
            System.out.print(items[i] + " ");
            j += 1;
        }
        System.out.println(items[i]);
    }

    @Override
    /** Removes and returns the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextfirst = (nextfirst + 1) % items.length;
        T result = items[nextfirst];
        items[nextfirst] = null;
        size -= 1;

        if (size < (items.length / 4) && items.length > 2 * initLength) {
            reSize(items.length / 2);
        }
        return result;
    }

    @Override
    /** Removes and returns the item at the bak of the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextlast = (nextlast + items.length - 1) % items.length;
        T result = items[nextlast];
        items[nextlast] = null;
        size -= 1;

        if (size < (items.length / 4) && items.length > 2 * initLength) {
            reSize(items.length / 2);
        }
        return result;
    }

    @Override
    /** Gets the item at the given index. */
    public T get(int index) {
        return items[(nextfirst + 1 + index) % items.length];
    }

    /** Returns whether the parameter o is equal to the deque. */
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> test = (Deque) o;
        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(test)) {
                return false;
            }
        }
        return true;
    }


    /** Returns an iterator. */
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    /** The arrayIterator class. */
    private class ArrayIterator implements Iterator {
        private int pos;

        ArrayIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            if (!this.hasNext()) {
                throw new ArrayIndexOutOfBoundsException("out of the array");
            }
            T result = get(pos);
            pos += 1;
            return result;
        }
    }
}
