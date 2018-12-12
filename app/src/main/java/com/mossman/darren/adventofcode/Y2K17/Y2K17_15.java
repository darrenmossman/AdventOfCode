package com.mossman.darren.adventofcode.Y2K17;

public class Y2K17_15 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_15 puzzle = new Y2K17_15();
        int i = puzzle.run(65, 8921);
        test(i, 588);

        i = puzzle.run(65, 8921, true);
        test(i, 309);

        i = puzzle.run(883, 879);
        System.out.printf("day 15: part 1 = %d\n", i);
        //test(i, 609);

        i = puzzle.run(883, 879, true);
        System.out.printf("day 15: part 2 = %d\n", i);
        //test(i, 253);
    }

    //--------------------------------------------------------------------------------------------

    public int run(long a, long b) {
        return run(a, b, false);
    }

    public int run(long a, long b, boolean part2) {
        final long aFac = 16807;
        final long bFac = 48271;
        final long div = 2147483647;

        int count;
        if (part2) {
            count = 5000000;
        } else {
            count = 40000000;
        }

        int res = 0;
        for (int i = 0; i < count; i++) {
            a = (a * aFac) % div;
            b = (b * bFac) % div;
            if (part2) {
                while (a % 4 != 0) a = (a * aFac) % div;
                while (b % 8 != 0) b = (b * bFac) % div;
            }
            long a16 = a & 0xFFFF;
            long b16 = b & 0xFFFF;
            if (a16 == b16) {
                res++;
            }
        }
        return res;
    }

}
