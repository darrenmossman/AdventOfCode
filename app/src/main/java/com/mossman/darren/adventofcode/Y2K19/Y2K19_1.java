package com.mossman.darren.adventofcode.Y2K19;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K19_1 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K19_1 puzzle = new Y2K19_1(false);
        int i = puzzle.part1();
        System.out.printf("day 1: part 1 = %d\n", i);
        test(i, 3305301);

        i = puzzle.part2();
        System.out.printf("day 1: part 2 = %d\n", i);
        test(i, 4955106);
    }

    //--------------------------------------------------------------------------------------------


    private ArrayList<Integer> modules;

    public Y2K19_1(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        modules = Utils.strArrayToNum(input);
    }

    public int part1() {
        int sum = 0;
        for (int module: modules) {
            int fuel = (int)Math.floor(module / 3) - 2;
            sum += fuel;
        }
        return sum;
    }

    public int part2() {
        int sum = 0;
        for (int module: modules) {
            int fuel = (int)Math.floor(module / 3) - 2;
            while (fuel > 0) {
                sum += fuel;
                fuel = (int)Math.floor(fuel / 3) - 2;
            }
        }
        return sum;
    }
}
