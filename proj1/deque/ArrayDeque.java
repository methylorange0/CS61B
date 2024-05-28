package deque;

public class ArrayDeque<T> {
    /** T[] is the place where data is stored,
     * and size is the size of the deque. */
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    /** Create an empty ArrayDeque. */
    public ArrayDeque() {
        int size = 0;
        items = (T[]) new Object[8];
        nextlast = 0;
        nextfirst = items.length - 1;
    }

    /** Rearrange the capacity of the array. */
    private void Resize(int cap) {
        T[] new_items = (T[]) new Object[cap];
        int j = 0;
        for (int i = (nextfirst + 1) % items.length ; j < size ; i = (i + 1) % items.length){
            new_items[j] = items[i];
            j += 1;
        }
        nextlast = size;
        nextfirst = new_items.length - 1;
        items = new_items;
    }

    /** Adds an item to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length){
            Resize(size * 2);
        }
        items[nextfirst] = item;
        nextfirst = (nextfirst + items.length - 1) % items.length;
        size += 1;
    }

    /** Adds an item to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length){
            Resize(size * 2);
        }
        items[nextlast] = item;
        nextlast = (nextlast + 1) % items.length;
        size += 1;
    }

    /** Return true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Return the size of deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to the last. */
    public void printDeque() {
        int j = 0;
        int i = 0;
        for (i = (nextfirst + 1) % items.length ; j < size - 1 ; i = (i + 1) % items.length){
            System.out.print(items[i] + " ");
            j += 1;
        }
        System.out.println(items[i]);
    }

    /** Removes and returns the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextfirst = (nextfirst + 1) % items.length;
        T result = items[nextfirst];
        items[nextfirst] = null;
        size -= 1;

        if (size < (items.length / 4) && items.length > 16) {
            Resize(items.length / 2);
        }
        return result;
    }

    /** Removes and returns the item at the bak of the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextlast = (nextlast + items.length - 1) % items.length;
        T result = items[nextlast];
        items[nextlast] = null;
        size -= 1;

        if (size < (items.length / 4) && items.length > 16) {
            Resize(items.length / 2);
        }
        return result;
    }

    /** Gets the item at the given index. */
    public T get(int index) {
        return items[(nextfirst + 1 + index) % items.length];
    }

    /** Returns whether the parameter o is equal to the deque. */
    public boolean equals(LinkedListDeque o) {
        if (!(o instanceof LinkedListDeque)){
            return false;
        }
        for ( int i = 0 ; i < size ; i++){
            if (this.get(i) != o.get(i)){
                return false;
            }
        }
        return true;
    }

    /** Returns whether the parameter o is equal to the deque. */
    public boolean equals(ArrayDeque o) {
        if (!(o instanceof ArrayDeque)){
            return false;
        }
        for ( int i = 0 ; i < size ; i++){
            if (this.get(i) != o.get(i)){
                return false;
            }
        }
        return true;
    }
}