package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> test1 = new AListNoResizing<>();
        BuggyAList<Integer> test2 = new BuggyAList<>();
        for (int i = 3; i < 6; i++) {
            test1.addLast(i);
            test2.addLast(i);
        }
        for (int i = 0; i < test1.size(); i++) {
            assertEquals(test1.removeLast(), test2.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> test1 = new AListNoResizing<>();
        BuggyAList<Integer> test2 = new BuggyAList<>();
        int N = 500;
        for (int i = 0 ; i < N ; i++) {
            int operation = StdRandom.uniform(0,3);
            if (operation == 0) {
                int val = StdRandom.uniform(0, 100);
                test1.addLast(val);
                test2.addLast(val);

            }else if (operation == 1 && test1.size() > 0 && test2.size() > 0){
                assertEquals(test1.getLast(), test2.getLast());
            }else if (operation == 2 && test1.size() > 0 && test2.size() > 0){
                assertEquals(test1.removeLast(), test2.removeLast());
            }
        }
    }


}
