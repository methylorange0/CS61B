package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    /** This is where data is stored. */
    private class LinkedNode {
        private LinkedNode prev;
        private T val;
        private LinkedNode next;
    }

    /** Head is a pointer to the LinkedNode, size is the size of the deque.*/
    private LinkedNode head;
    private int size;

    /** Create an empty LinkedListDeque.*/
    public LinkedListDeque() {
        head = new LinkedNode();
        head.prev = head;
        head.next = head;
        size = 0;
    }

    @Override
    /** Adds an item in the front of the deque. */
    public void addFirst(T item) {
        LinkedNode node = new LinkedNode();
        if (size == 0) {
            head.prev = node;
        }
        node.val = item;
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
        size += 1;
    }

    @Override
    /** Adds an item to the back of the deque. */
    public void addLast(T item) {
        LinkedNode node = new LinkedNode();
        if (size == 0) {
            head.next = node;
        }
        node.val = item;
        head.prev.next = node;
        node.prev = head.prev;
        head.prev = node;
        node.next = head;
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
        LinkedNode helper = head.next;
        for (int i = 0; i < size - 1; i++) {
            System.out.print(helper.val + " ");
            helper = helper.next;
        }
        System.out.println(helper.val);
    }

    @Override
    /** Removes and returns the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        LinkedNode target = head.next;
        T result = target.val;
        target.val = null;
        target.next.prev = head;
        head.next = target.next;
        size -= 1;
        return result;
    }

    @Override
    /** Removes and returns the item at the back of the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        LinkedNode target = head.prev;
        T result = target.val;
        target.val = null;
        target.prev.next = head;
        head.prev = target.prev;
        size -= 1;
        return result;
    }

    @Override
    /** Gets the item at the given index. */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        LinkedNode target = head.next;
        for (int i = 0; i < index; i++) {
            target = target.next;
        }
        return target.val;
    }

    /** Same as get, but uses recursion. */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, head.next);
    }

    /** The helper of getRecursive. */
    private T getRecursiveHelper(int index, LinkedNode p) {
        if (index == 0) {
            return p.val;
        } else {
            return getRecursiveHelper(index - 1, p.next);
        }
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
        return new LinkedIterator();
    }

    /** The arrayIterator class. */
    private class LinkedIterator implements Iterator {
        private int pos;

        LinkedIterator() {
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
