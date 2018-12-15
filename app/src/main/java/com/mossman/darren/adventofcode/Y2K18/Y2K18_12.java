package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Y2K18_12 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_12 puzzle;
        long i;

        puzzle = new Y2K18_12(true);
        i = puzzle.run(20);
        test(i, 325);

        puzzle = new Y2K18_12(false);
        i = puzzle.run(20);
        System.out.printf("day 12: part 1 = %d\n", i);
        //test(i, 6201);

        i = puzzle.run(50000000000L, true);
        System.out.printf("day 12: part 2 = %d\n", i);
        //test(i, 9300000001023L);
    }

    //--------------------------------------------------------------------------------------------

    private String initialState;
    private HashMap<String, Character> map;

    public Y2K18_12(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        initialState = input.get(0);
        initialState = initialState.replaceAll("initial state: ", "");
        input.remove(0);
        input.remove(0);
        ArrayList<String[]> str = Utils.parseInput(input, " => ");
        map = new HashMap<>(str.size());
        for (String[] s: str) {
            map.put(s[0], s[1].charAt(0));
        }
    }

    public long run(long generations) {
        return run(generations, false);
    }

    public long run(long generations, boolean part2) {
        // After 200 generations result increases by fixed amount.
        // Should probably scan for when this occurs, rather than picking arbitrary value
        if (part2 && generations > 200) {
            long fixed = run(201) - run(200);
            long res = run(200) + (generations-200) * fixed;
            return res;
        }

        TreeMap<Integer, Character> plants = new TreeMap<>();
        for (int i = 0; i < initialState.length(); i++) {
            plants.put(i, initialState.charAt(i));
        }
        for (long g = 0; g < generations; g++) {
            TreeMap<Integer, Character> nextGen = new TreeMap<>();
            int first = plants.firstKey() - 2;
            int last = plants.lastKey() + 2;
            for (int i = first; i <= last; i++) {
                String s = "";
                for (int j = 0; j < 5; j++) {
                    Character c = plants.get(i - 2 + j);
                    s += c == null ? '.' : c;
                }
                if (map.containsKey(s)) {
                    Character c = map.get(s);
                    if (c == '#') nextGen.put(i, c);
                }
            }
            plants = nextGen;
        }
        long res = 0;
        for (Integer i : plants.keySet()) res += i;
        return res;
    }
}













