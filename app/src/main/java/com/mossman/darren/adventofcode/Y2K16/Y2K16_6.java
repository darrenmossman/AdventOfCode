package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Y2K16_6 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_6 puzzle = new Y2K16_6(true);
        String s = puzzle.run();
        System.out.printf("day 6: part 1 test = %s\n", s);
        test(s, "easter");

        s = puzzle.run(true);
        System.out.printf("day 6: part 2 test = %s\n", s);
        test(s, "advent");


        puzzle = new Y2K16_6(false);
        s = puzzle.run();
        System.out.printf("day 6: part 1 = %s\n", s);
        test(s, "ikerpcty");

        s = puzzle.run(true);
        System.out.printf("day 6: part 2 = %s\n", s);
        test(s, "uwpfaqrq");
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K16_6(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public String run() {
        return run(false);
    }

    public String run(boolean part2) {

        ArrayList<HashMap<Character, Integer>> list = new ArrayList<>(input.size());

        for (String s : input) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                HashMap<Character, Integer> hash;
                if (i >= list.size()) {
                    hash = new HashMap<Character, Integer>();
                    list.add(i, hash);
                }
                else hash = list.get(i);
                Integer cnt = hash.get(c);
                if (cnt == null) hash.put(c, 1);
                else hash.put(c, cnt+1);
            }
        }
        StringBuilder builder = new StringBuilder();

        for (HashMap<Character, Integer> hash : list) {
            Character c = null;
            if (part2) {
                int min = 0;
                for (Map.Entry<Character, Integer> entry : hash.entrySet()) {
                    int cnt = entry.getValue();
                    if (c == null || cnt < min) {
                        min = cnt;
                        c = entry.getKey();
                    }
                }
            }
            else {
                int max = 0;
                for (Map.Entry<Character, Integer> entry : hash.entrySet()) {
                    int cnt = entry.getValue();
                    if (cnt > max) {
                        max = cnt;
                        c = entry.getKey();
                    }
                }
            }
            builder.append(c);
        }
        String res = builder.toString();
        return res;
    }
}
