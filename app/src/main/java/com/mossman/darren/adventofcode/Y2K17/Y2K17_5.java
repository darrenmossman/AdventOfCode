package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Y2K17_5 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_5 puzzle = new Y2K17_5(new Integer[]{0,3,0,1,-3});
        int i = puzzle.part1();
        test(i, 5);

        i = puzzle.part2();
        test(i, 10);

        puzzle = new Y2K17_5(false);
        i = puzzle.part1();
        System.out.printf("day 5: part 1 = %d\n", i);
        //test(i, 356945);

        i = puzzle.part2();
        System.out.printf("day 5: part 2 = %d\n", i);
        //test(i, 28372145);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private Integer[] testInput;
    private ArrayList<Integer> vals;

    public Y2K17_5(boolean test) {
        input = Utils.readFile(getFilename(test));
    }
    public Y2K17_5(Integer[] test) {
        testInput = test;
    }

    private void init() {
        if (testInput != null) {
            vals = new ArrayList<>(Arrays.asList(testInput));
        } else {
            vals = Utils.strArrayToNum(input);
        }
    }

    public int part1() {
        init();

        int steps = 0, pos = 0;
        while (pos >= 0 && pos < vals.size()) {
            steps++;
            int j = vals.get(pos);
            vals.set(pos, j+1);
            pos += j;
        }
        return steps;
    }

    public int part2() {
        init();

        int steps = 0, pos = 0;
        while (pos >= 0 && pos < vals.size()) {
            steps++;
            int j = vals.get(pos);
            if (j >= 3) {
                vals.set(pos, j-1);
            } else {
                vals.set(pos, j+1);
            }
            pos += j;
        }
        return steps;
    }


}
