package tester;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import student.StudentArrayDeque;
public class TestArrayDequeEC {

    @Test
    /** Randomized tests between StudentArrayDeque and ArrayDequeSolution. */
    public void randomizedTest() {
        ArrayDequeSolution<Integer> test1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> test2 = new StudentArrayDeque<>();
        int N = 10000;
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0, 7);
            if (op == 0){
                int val = StdRandom.uniform(0, 100);
                test1.addFirst(val);
                test2.addFirst(val);
            } else if (op == 1) {
                int val = StdRandom.uniform(0, 100);
                test1.addLast(val);
                test2.addLast(val);
            } else if (op == 2) {
                assertEquals("Oh no! there is something wrong in isEmpty()!\n" +
                        "excepted: " + test1.isEmpty() + "but actual: " + test2.isEmpty(),
                        test1.isEmpty(), test2.isEmpty());
            } else if (op == 3) {
                assertEquals("Oh no! there is something wrong in size()!\n" +
                                "excepted: " + test1.size() + "but actual: " + test2.size(),
                        test1.size(), test2.size());
            } else if (op == 4 && test1.size() > 0) {
                assertEquals("Oh no! there is something wrong in removeFirst()!\n" +
                                "excepted: " + test1.removeFirst() + "but actual: " + test2.removeFirst(),
                        test1.removeFirst(), test2.removeFirst());
            } else if (op == 5 && test1.size() > 0) {
                assertEquals("Oh no! there is something wrong in removeLast()!\n" +
                                "excepted: " + test1.removeLast() + "but actual: " + test2.removeLast(),
                        test1.removeLast(), test2.removeLast());
            } else if (test1.size() > 0) {
                int index = StdRandom.uniform(0, test1.size());
                assertEquals("Oh no! there is something wrong in get()!\n" +
                                "excepted: " + test1.get(index) + "but actual: " + test2.get(index),
                        test1.get(index), test2.get(index));
            }
        }
    }
}
