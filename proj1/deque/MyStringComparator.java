package deque;

import java.util.Comparator;

public class MyStringComparator {
    /** String alphabet comparator. */
    private static class StringComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    /** Returns a string alphabet comparator. */
    public static Comparator<String> getStringComparator() {
        return new StringComparator();
    }

    /** String length comparator */
    private static class LengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    /** Returns a length comparator. */
    public static Comparator<String> getlengthComparator() {
        return new LengthComparator();
    }
}
