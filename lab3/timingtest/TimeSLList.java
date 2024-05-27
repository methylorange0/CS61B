package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> N = new AList<>();
        AList<Integer> ops = new AList<>();
        AList<Double> times = new AList<>();
        N.addLast(1000);
        N.addLast(2000);
        N.addLast(4000);
        N.addLast(8000);
        N.addLast(16000);
        N.addLast(32000);
        N.addLast(64000);
        N.addLast(128000);
        for (int i = 0 ; i < N.size() ; i++){
            ops.addLast(10000);
        }
        for (int i = 0 ; i < N.size() ; i++){
            SLList<Integer> test = new SLList<>();
            for (int j = 0 ; j < N.get(i) ; j++){
                test.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0 ; j < 10000 ; j++){
                int last = test.getLast();
            }
            double time = sw.elapsedTime();
            times.addLast(time);
        }
        printTimingTable(N, times, ops);
    }

}
