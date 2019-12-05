package com.mossman.darren.adventofcode.Y2K19;

import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

public class Y2K19_4 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K19_4 puzzle = new Y2K19_4(284639, 748759);
        int i = puzzle.part1();
        System.out.printf("day 4: part 1 = %d\n", i);
        test(i, 895);

        i = puzzle.part2();
        System.out.printf("day 4: part 2 = %d\n", i);
        test(i, 591);
    }

    //--------------------------------------------------------------------------------------------

    private int low;
    private int high;

    public Y2K19_4(int low, int high) {
        this.low = low;
        this.high = high;
    }

    private int part1() {
        return run(false);
    }

    private int part2() {
        return run(true);
    }

    private int run(boolean part2) {

        int res = 0;

        for (int p = low; p <= high; p++) {
            String password = String.valueOf(p);
            if (test(password, part2)) {
                res++;
            }
        }
        return res;
    }

    private boolean test(String password, boolean part2) {
        char[] digits = password.toCharArray();

        boolean decreasing = false;
        boolean priorMatch = false;
        int min = 0;
        char prior = ' ';
        int matchCount = 0;

        for (char c : digits) {
            int i = c - '0';
            if (i > min) {
                min = i;
            }
            else if (i < min) {
                decreasing = true;
                break;
            }
            if (!priorMatch) {
                if (part2) {
                    if (c == prior) {
                        matchCount++;
                    } else {
                        if (matchCount == 1) priorMatch = true;
                        matchCount = 0;
                    }
                }
                else if (c == prior) priorMatch = true;
                prior = c;
            }
        }
        if (part2) {
            priorMatch = priorMatch || (matchCount == 1);
        }

        boolean res = (priorMatch && !decreasing);
        return res;

    }

}