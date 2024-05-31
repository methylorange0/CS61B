package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;
import java.util.Iterator;

/** Test my MaxArrayDeque. */
public class MaxArrayDequeTest {

    @Test
    public void constructMaxArrayTest() {
        Comparator length = MyStringComparator.getlengthComparator();
        Comparator string = MyStringComparator.getStringComparator();

        MaxArrayDeque<String> test = new MaxArrayDeque<>(length);
        test.addLast("I");
        test.addLast("Love");
        test.addLast("CS61B");
        test.addLast("Thank you");
        test.addLast("Professor");
        test.addLast("Z");
        System.out.println(test.max());
        System.out.println(test.max(string));
    }
}