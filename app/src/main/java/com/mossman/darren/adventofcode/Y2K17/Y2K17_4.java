package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Y2K17_4 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_4 puzzle = new Y2K17_4(false);
        int i = puzzle.part1();
        System.out.printf("day 4: part 1 = %d\n", i);
        //test(i, 383);

        i = puzzle.part2();
        System.out.printf("day 4: part 2 = %d\n", i);
        //test(i, 265);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String[]> passphrases;

    public Y2K17_4(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        passphrases = Utils.parseInput(input, " ");
    }

    private static boolean isUniquePassphrase(String[] passphrase) {
        HashSet<String> set = new HashSet<>();
        for (String str: passphrase) {
            if (set.contains(str)) {
                return false;
            } else {
                set.add(str);
            }
        }
        return true;
    }

    public int part1() {
        int res = 0;
        for (String[] passphrase : passphrases) {
            if (isUniquePassphrase(passphrase)) {
                res++;
            }
        }
        return res;
    }


    private static boolean isValidPassphrase(String[] passphrase) {
        HashSet<String> set = new HashSet<>();
        for (String str: passphrase) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            str = new String(chars);
            if (set.contains(str)) {
                return false;
            } else {
                set.add(str);
            }
        }
        return true;
    }

    public int part2() {
        int res = 0;
        for (String[] passphrase : passphrases) {
            if (isValidPassphrase(passphrase)) {
                res++;
            }
        }
        return res;
    }


}
