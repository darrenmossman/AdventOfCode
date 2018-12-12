package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_6 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_6 puzzle = new Y2K17_6(new Integer[]{0,2,7,0});
        int i = puzzle.run();
        test(i, 5);

        i = puzzle.run(true);
        test(i, 4);

        puzzle = new Y2K17_6(false);
        i = puzzle.run();
        System.out.printf("day 6: part 1 = %d\n", i);
        //test(i, 7864);

        i = puzzle.run(true);
        System.out.printf("day 6: part 2 = %d\n", i);
        //test(i, 1695);
    }

    //--------------------------------------------------------------------------------------------

    private Integer[] testInput;

    public Y2K17_6(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, "\t");
        ArrayList<Integer[]> vals = Utils.strArraysToNum(str);
        testInput = vals.get(0);

    }
    public Y2K17_6(Integer[] test) {
        testInput = test;
    }

    public int run() {
        return run(false);
    }

    public int run(boolean part2) {
        String init = "";
        if (part2) {
            for (int i : testInput) {
                init = init + i + ",";
            }
        }
        int res = 0;
        ArrayList<String> configs = new ArrayList<>();
        while (true) {
            res++;
            int max = 0, pos = 0;
            for (int i = 0; i < testInput.length; i++) {
                int b = testInput[i];
                if (b > max) {
                    max = b;
                    pos = i;
                }
            }
            testInput[pos] = 0;
            while (max > 0) {
                pos++;
                pos = pos % testInput.length;
                testInput[pos] = testInput[pos] + 1;
                max--;
            }
            String s = "";
            for (int i: testInput) {
                s = s + i + ",";
            }
            if (part2) {
                if (init.equals(s)) {
                    break;
                }
            } else {
                if (configs.contains(s)) {
                    break;
                } else {
                    configs.add(s);
                }
            }
        };
        return res;
    }
}
