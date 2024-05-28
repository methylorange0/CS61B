package deque;

public class LinkedListDeque<T> {

    /** This is where data is stored. */
    private class LinkedNode {
        public LinkedNode prev;
        public T val;
        public LinkedNode next;
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

    /** Adds an item in the front of the deque. */
    public void addFirst(T item) {
        LinkedNode node = new LinkedNode();
        if (size == 0){
            head.prev = node;
        }
        node.val = item;
        node.next = head.next;
        node.prev = head;
        head.next = node;
        size += 1;
    }

    /** Adds an item to the back of the deque. */
    public void addLast(T item) {
        LinkedNode node = new LinkedNode();
        if (size == 0){
            head.next = node;
        }
        node.val = item;
        head.prev.next = node;
        node.prev = head.prev;
        head.prev = node;
        node.next = head;
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
        LinkedNode helper = head.next;
        for (int i = 0 ; i < size - 1 ; i++){
            System.out.print(helper.val + " ");
            helper = helper.next;
        }
        System.out.println(helper.val);
    }

    /** Removes and returns the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0){
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

    /** Removes and returns the item at the back of the deque. */
    public T removeLast() {
        if (size == 0){
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

    /** Gets the item at the given index. */
    public T get(int index){
        if (index >= size){
            return null;
        }
        LinkedNode target = head.next;
        for (int i = 0 ; i < index ; i++){
            target = target.next;
        }
        return target.val;
    }

    /** Same as get, but uses recursion. */
    public T getRecursive(int index){
        if (index >= size){
            return null;
        }
        return getRecursiveHelper(index, head.next);
    }

    /** The helper of getRecursive. */
    private T getRecursiveHelper(int index, LinkedNode p){
        if (index == 0){
            return p.val;
        } else {
            return getRecursiveHelper(index - 1, p.next);
        }
    }
}