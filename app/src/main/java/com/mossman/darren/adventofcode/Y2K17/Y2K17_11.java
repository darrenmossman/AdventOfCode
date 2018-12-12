package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_11 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_11 puzzle = new Y2K17_11();
        int i = puzzle.run(new String[]{"ne","ne","ne"});
        test(i, 3);

        i = puzzle.run(new String[]{"ne","ne","sw","sw"});
        test(i, 0);

        i = puzzle.run(new String[]{"ne","ne","s","s"});
        test(i, 2);

        i = puzzle.run(new String[]{"se","sw","se","sw","sw"});
        test(i, 3);

        ArrayList<String> input = Utils.readFile(puzzle.getFilename(false));
        ArrayList<String[]> str = Utils.parseInput(input, ",");
        i = puzzle.run(str.get(0));
        System.out.printf("day 11: part 1 = %d\n", i);
        //test(i, 664);

        i = puzzle.run(str.get(0), true);
        System.out.printf("day 11: part 2 = %d\n", i);
        //test(i, 1447);
    }

    //--------------------------------------------------------------------------------------------

    static private int distance(int x, int y) {
        int res;
        x = Math.abs(x);
        y = Math.abs(y);
        if (y > x) {
            y = y-x;
            res = y/2 + x;
        } else res = x;
        return res;
    }

    public int run(String[] input) {
        return run(input, false);
    }
    
    public int run(String[] input, boolean furthest) {
        int x = 0, y = 0;
        int max = 0;
        for (String step: input) {
            switch (step) {
                case "n":
                    y-=2;
                    break;
                case "ne":
                    x++; y--;
                    break;
                case "se":
                    x++; y++;
                    break;
                case "s":
                    y+=2;
                    break;
                case "sw":
                    x--; y++;
                    break;
                case "nw":
                    x--; y--;
                    break;
            }
            if (furthest) {
                int steps = distance(x,y);
                if (steps > max) {
                    max = steps;
                }
            }
        }
        if (furthest) {
            return max;
        } else {
            return distance(x,y);
        }
    }
}
