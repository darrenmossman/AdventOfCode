package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class Y2K18_2 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_2 puzzle = new Y2K18_2(false);
        int i = puzzle.part1();
        System.out.printf("day 2: part 1 = %d\n", i);
        //test(i, 5478);

        String s = puzzle.part2();
        System.out.printf("day 2: part 2 = %s\n", s);
        //test(s, "qyzphxoiseldjrntfygvdmanu");
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K18_2(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int part1() {
        int cntTwo = 0, cntThree = 0;
        for (String s: input) {
            HashMap<Character, Integer> cnt = new HashMap<>();
            for (Character c: s.toCharArray()) {
                if (cnt.containsKey(c)) {
                    Integer v = cnt.get(c);
                    cnt.put(c, v+1);
                } else {
                    cnt.put(c, 1);
                }
            }
            boolean hasTwo = false, hasThree = false;
            for (Character c: cnt.keySet()) {
                if (cnt.get(c) == 2) {
                    hasTwo = true;
                } else if (cnt.get(c) == 3) {
                    hasThree = true;
                }
                if (hasTwo && hasThree) {
                    break;
                }
            }
            if (hasTwo) {
                cntTwo++;
            }
            if (hasThree) {
                cntThree++;
            }
        }
        return cntTwo * cntThree;
    }

    public String part2() {
        for (int f = 0; f < input.size(); f++) {
            String s1 = input.get(f);
            for (int g = f+1; g < input.size(); g++) {
                String s2 = input.get(g);
                int cnt = 0, pos = 0;
                for (int i = 0; i < s1.length(); i++) {
                    if (s1.charAt(i) != s2.charAt(i)) {
                        cnt++;
                        if (cnt > 1) break;
                        pos = i;
                    }
                }
                if (cnt == 1) {
                    StringBuilder sb = new StringBuilder(s1);
                    sb.deleteCharAt(pos);
                    return sb.toString();
                }
            }
        }
        return null;
    }

}
