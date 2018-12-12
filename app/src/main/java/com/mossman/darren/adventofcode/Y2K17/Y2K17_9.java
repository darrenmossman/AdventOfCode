package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_9 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_9 puzzle = new Y2K17_9(true);
        int i = puzzle.run(0);
        test(i, 1);
        i = puzzle.run(1);
        test(i, 6);
        i = puzzle.run(2);
        test(i, 5);
        i = puzzle.run(3);
        test(i, 16);
        i = puzzle.run(4);
        test(i, 1);
        i = puzzle.run(5);
        test(i, 9);
        i = puzzle.run(6);
        test(i, 9);
        i = puzzle.run(7);
        test(i, 3);

        puzzle = new Y2K17_9(false);
        i = puzzle.run(0);
        System.out.printf("day 9: part 1 = %d\n", i);
        //test(i, 15922);

        i = puzzle.run(0, true);
        System.out.printf("day 9: part 2 = %d\n", i);
        //test(i, 7314);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    
    public Y2K17_9(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run(int index) {
        return run(index, false);
    }

    public int run(int index, boolean countGarbage) {
        String s = input.get(index);
        int total = 0, group = 0;
        boolean garbage = false, bang = false;
        for (char c: s.toCharArray()) {
            if (garbage) {
                if (bang) {
                    bang = false;
                    continue;
                }
                if (c == '!') {
                    bang = true;
                }
                else if (c == '>') {
                    garbage = false;
                }
                else if (countGarbage) {
                    total++;
                }
            }
            else {
                if (c == '{') {
                    group++;
                    if (!countGarbage) {
                        total += group;
                    }
                }
                else if (c == '}') {
                    group--;
                }
                else if (c == '<') {
                    garbage = true;
                }
            }
        }
        return total;
    }
}
