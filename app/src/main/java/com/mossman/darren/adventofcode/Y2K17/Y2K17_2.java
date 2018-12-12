package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_2 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_2 puzzle = new Y2K17_2(false);
        int i = puzzle.part1();
        System.out.printf("day 2: part 1 = %d\n", i);
        //test(i, 42378);

        i = puzzle.part2();
        System.out.printf("day 2: part 2 = %d\n", i);
        //test(i, 246);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<Integer[]> vals;

    public Y2K17_2(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, "\t");
        vals = Utils.strArraysToNum(str);

    }

    public int part1() {
        int sum = 0;
        for (Integer[] row: vals) {
            Integer max = null, min = null;
            for (int val: row) {
                if (max == null || val > max) max = val;
                if (min == null || val < min) min = val;
            }
            sum += max-min;
        }
        return sum;
    }

    public int part2() {
        int sum = 0;
        for (Integer[] row: vals) {
            Integer max = null, min = null;
            for (int i = 0; i < row.length; i++) {
                int v0 = row[i];
                for (int j = 0; j < row.length; j++) {
                    if (i == j) continue;
                    int v1 = row[j];
                    if (v0 % v1 == 0) {
                        sum += v0 / v1;
                    }
                }
            }
        }
        return sum;
    }
}
