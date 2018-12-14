package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Y2K16_3 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_3 puzzle = new Y2K16_3(false);
        int i = puzzle.run();
        System.out.printf("day 3: part 1 = %d\n", i);
        //test(i, 983);

        i = puzzle.run(true);
        System.out.printf("day 3: part 2 = %d\n", i);
        //test(i, 1836);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String[]> str;

    public Y2K16_3(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        str = Utils.parseInput(input, " ");
    }

    public int run() {
        return run(false);
    }

    public int run(boolean part2) {

        ArrayList<Integer[]> vals = Utils.strArraysToNum(str);

        int cnt = 0;

        if (part2) {
            for (int i = 0; i < vals.size()-2; i+=3) {
                Integer[][] v = new Integer[3][];
                for (int j = 0; j < 3; j++) {
                    v[j] = vals.get(i+j);
                }
                List<Integer> arr = new ArrayList<>(3);
                for (int j = 0; j < 3; j++) {
                    arr.add(v[0][j]);
                    arr.add(v[1][j]);
                    arr.add(v[2][j]);
                    Collections.sort(arr);
                    if (arr.get(0) + arr.get(1) > arr.get(2)) {
                        cnt++;
                    }
                    arr.clear();
                }
            }
        }
        else {
            for (Integer[] v : vals) {
                List<Integer> arr = Arrays.asList(v);
                Collections.sort(arr);
                if (arr.get(0) + arr.get(1) > arr.get(2)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
