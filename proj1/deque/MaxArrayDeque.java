package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmpor;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmpor = c;
    }

    /** Returns the maximum element in the deque in the default way to compare. */
    public T max() {
        if (size() == 0) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            int cmp = cmpor.compare(get(i), get(maxIndex));
            if (cmp > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

    /** Returns the maximum element in the deque in the giving way to compare. */
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            int cmp = c.compare(get(i), get(maxIndex));
            if (cmp > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }


}
