package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class Y2K18_1 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_1 puzzle = new Y2K18_1(false);
        int i = puzzle.part1();
        System.out.printf("day 1: part 1 = %d\n", i);
        //test(i, 582);

        i = puzzle.part2();
        System.out.printf("day 1: part 2 = %d\n", i);
        //test(i, 488);
    }

    //--------------------------------------------------------------------------------------------


    private ArrayList<Integer> freq;

    public Y2K18_1(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        freq = Utils.strArrayToNum(input);
    }

    public int part1() {
        // sum frequency
        int sum = 0;
        for (int f: freq) {
            sum += f;
        }
        return sum;
    }

    public int part2() {
        // findRepeatFrequency
        int sum = 0;
        HashSet<Integer> found = new HashSet<>();
        while (true) {
            for (int f : freq) {
                sum += f;
                if (found.contains(sum)) {
                    return sum;
                } else {
                    found.add(sum);
                }
            }
        }
    }

}
