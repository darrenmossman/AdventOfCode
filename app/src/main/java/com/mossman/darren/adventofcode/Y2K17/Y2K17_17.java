package com.mossman.darren.adventofcode.Y2K17;

import java.util.ArrayList;

public class Y2K17_17 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_17 puzzle = new Y2K17_17();
        int i = puzzle.run(3);
        test(i, 638);

        i = puzzle.run(366);
        System.out.printf("day 17: part 1 = %d\n", i);
        //test(i, 1025);

        i = puzzle.run(366, true);
        System.out.printf("day 17: part 2 = %d\n", i);
        //test(i, 37803463);
    }

    //--------------------------------------------------------------------------------------------

    public int run(int steps) {
        return run(steps, false);
    }

    public int run(int steps, boolean part2) {
        if (part2) {
            final int count = 50000000;
            int p = 0; int v = 1; int val = 0;
            for (int i = 0; i < count-1; i++) {
                p += steps;
                p = p % v;
                p++;
                if (p == 1) {
                    val = v;
                }
                v++;
            }
            int res = val;
            return res;
        } else {
            final int count = 2018;
            ArrayList<Integer> buffer = new ArrayList<>(count);
            buffer.add(0);
            int p = 0; int v = 1;
            for (int i = 0; i < count-1; i++) {
                p += steps;
                p = p % buffer.size();
                p++;
                buffer.add(p, v);
                v++;
            }
            int res = buffer.get((p+1) % buffer.size());
            return res;
        }
    }

}
