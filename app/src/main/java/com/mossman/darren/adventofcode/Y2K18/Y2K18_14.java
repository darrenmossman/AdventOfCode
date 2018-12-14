package com.mossman.darren.adventofcode.Y2K18;

import java.util.ArrayList;

public class Y2K18_14 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_14 puzzle = new Y2K18_14();

        String s = puzzle.part1(9);
        test(s, "5158916779");
        s = puzzle.part1(5);
        test(s, "0124515891");
        s = puzzle.part1(18);
        test(s, "9251071085");
        s = puzzle.part1(2018);
        test(s, "5941429882");

        s = puzzle.part1(540391);
        System.out.printf("day 14: part 1 = %s\n", s);
        test(s, "1474315445");

        
        int i = puzzle.part2("51589");
        test(i, 9);
        i = puzzle.part2("01245");
        test(i, 5);
        i = puzzle.part2("92510");
        test(i, 18);
        i = puzzle.part2("59414");
        test(i, 2018);

        i = puzzle.part2("540391");
        System.out.printf("day 14: part 2 = %d\n", i);
        test(i, 20278122);
    }

    //--------------------------------------------------------------------------------------------
   
    public String part1(long input) {
        ArrayList<Integer> recipes = new ArrayList<>();
        recipes.add(3);
        recipes.add(7);

        int pos0 = 0, pos1 = 1;
        while (true) {
            int sc0 = recipes.get(pos0);
            int sc1 = recipes.get(pos1);
            int recipe = sc0 + sc1;
            int r0 = recipe / 10;
            int r1 = recipe % 10;
            if (r0 != 0) {
                recipes.add(r0);
                if (recipes.size() == input+10) break;
            }
            recipes.add(r1);
            if (recipes.size() == input+10) break;
            pos0 = (pos0 + sc0 + 1) % recipes.size();;
            pos1 = (pos1 + sc1 + 1) % recipes.size();;
        }
        String res = "";
        for (int i = recipes.size()-10; i < recipes.size(); i++) {
            res += recipes.get(i);
        }
        return res;
    }

    private boolean matches(ArrayList<Integer> recipes, int[] digits) {
        int sz = recipes.size()-digits.length;
        if (sz < 0) return false;

        for (int i = 0; i < digits.length; i++) {
            if (recipes.get(sz+i) != digits[i]) return false;
        }
        return true;
    }

    public int part2(String input) {

        int[] digits = new int[input.length()];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = input.charAt(i) - '0';
        }

        ArrayList<Integer> recipes = new ArrayList<>();
        recipes.add(3);
        recipes.add(7);

        int pos0 = 0, pos1 = 1;
        while (true) {
            int sc0 = recipes.get(pos0);
            int sc1 = recipes.get(pos1);
            int recipe = sc0 + sc1;
            int r0 = recipe / 10;
            int r1 = recipe % 10;
            if (r0 != 0) {
                recipes.add(r0);
                if (matches(recipes, digits)) {
                    return recipes.size()-digits.length;
                }
            }
            recipes.add(r1);
            if (matches(recipes, digits)) {
                return recipes.size()-digits.length;
            }
            pos0 = (pos0 + sc0 + 1) % recipes.size();
            pos1 = (pos1 + sc1 + 1) % recipes.size();
        }
    }
}
