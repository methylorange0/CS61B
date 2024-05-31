package deque;

import org.junit.Test;

import static org.junit.Assert.*;

/** Test equal method in ArrayDeque and LinkedListDeque. */
public class EqualsTest {

    @Test
    /** Equal to self. */
    public void equalToSelf() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> ltest1 = new LinkedListDeque<>();
        Deque<Integer> atest2 = atest1;
        Deque<Integer> ltest2 = ltest1;
        assertTrue(atest1.equals(atest2));
        assertTrue(ltest1.equals(ltest2));
    }

    @Test
    /** Test null. */
    public void notEqualToNull() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> ltest1 = new LinkedListDeque<>();
        assertFalse(atest1.equals(null));
        assertFalse(ltest1.equals(null));
    }

    @Test
    /** Should not equal to a wrong class. */
    public void notEqualToWrongClass() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> ltest1 = new LinkedListDeque<>();
        String test = "Wrong class!";
        assertFalse(atest1.equals(test));
        assertFalse(ltest1.equals(test));
    }

    @Test
    /** Test different size. */
    public void notEqToDiSize() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> atest2 = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            atest1.addLast(i);
            atest2.addLast(i);
        }
        atest2.addLast(1000);
        assertFalse(atest1.equals(atest2));
    }

    @Test
    /** Same elements, same class. */
    public void sameElemSaCl() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> atest2 = new ArrayDeque<>();
        Deque<Integer> stest1 = new LinkedListDeque<>();
        Deque<Integer> stest2 = new LinkedListDeque<>();
        for (int i = 0; i < 1000; i++) {
            atest1.addLast(i);
            atest2.addLast(i);
            stest1.addFirst(i);
            stest2.addFirst(i);
        }
        assertTrue(atest1.equals(atest2));
        assertTrue(stest1.equals(stest2));
    }

    @Test
    /** Same elements, two class. */
    public void sameElem2Cl() {
        Deque<Integer> atest1 = new ArrayDeque<>();
        Deque<Integer> atest2 = new LinkedListDeque<>();
        for (int i = 0; i < 5; i++) {
            atest1.addLast(i);
            atest2.addLast(i);
        }
        assertTrue(atest1.equals(atest2));
        assertTrue(atest2.equals(atest1));
    }
}
