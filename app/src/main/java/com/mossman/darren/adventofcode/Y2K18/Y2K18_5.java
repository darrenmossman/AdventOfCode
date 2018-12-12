package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_5 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_5 puzzle = new Y2K18_5("dabAcCaCBAcCcaDA");
        int i = puzzle.run();
        test(i, 10);
        i = puzzle.run(true);
        test(i, 4);

        puzzle = new Y2K18_5(false);
        i = puzzle.run();
        System.out.printf("day 5: part 1 = %d\n", i);
        //test(i, 10450);

        i = puzzle.run(true);
        System.out.printf("day 5: part 2 = %d\n", i);
        //test(i, 4624);
    }

    //--------------------------------------------------------------------------------------------

    private String inputStr;

    public Y2K18_5(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        inputStr = input.get(0);
    }

    public Y2K18_5(String s) {
        inputStr = s;
    }

    private void reactPolymer(ArrayList<Character> polymer) {
        boolean removed;
        do {
            removed = false;
            for (int i = polymer.size()-1; i > 1; i--) {
                char c1 = polymer.get(i);
                char c0 = polymer.get(i-1);
                if (c1 != c0) {
                    char u1 = Character.toUpperCase(c1);
                    char u0 = Character.toUpperCase(c0);
                    if (u1 == u0) {
                        polymer.remove(i);
                        polymer.remove(i - 1);
                        removed = true;
                        i--;
                    }
                }
            }
        } while (removed);
    }

    public int run() {
        return run(false);
    }

    public int run(boolean part2) {
        ArrayList<Character> polymer = new ArrayList<>(inputStr.length());
        for (int i = 0; i < inputStr.length(); i++) {
            polymer.add(inputStr.charAt(i));
        }
        if (part2) {
            Integer min = null;
            for (char c = 'a'; c <= 'z'; c++) {
                char u = Character.toUpperCase(c);
                ArrayList<Character> poly = new ArrayList<>(polymer);
                for (int i = poly.size() - 1; i > 0; i--) {
                    char p = poly.get(i);
                    if (p == c || p == u) {
                        poly.remove(i);
                    }
                }
                reactPolymer(poly);
                int sz = poly.size();
                if (min == null || sz < min) {
                    min = sz;
                }
            }
            return min;
        } else {
            reactPolymer(polymer);
        }
        return polymer.size();
    }
}
