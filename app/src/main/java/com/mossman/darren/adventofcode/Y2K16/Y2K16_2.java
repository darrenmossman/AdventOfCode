package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K16_2 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_2 puzzle = new Y2K16_2(true);
        String s = puzzle.run();
        test(s, "1985");

        s = puzzle.run(true);
        test(s, "5DB3");


        puzzle = new Y2K16_2(false);
        s = puzzle.run();
        System.out.printf("day 2: part 1 = %s\n", s);
        //test(s, "38961");

        s = puzzle.run(true);
        System.out.printf("day 2: part 2 = %s\n", s);
        //test(s, "46C92");
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K16_2(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public String run() {
        return run(false);
    }

    public String run(boolean part2) {
        final String start = "5";
        final String[] arr;
        if (part2) {
            arr = new String[]{"  1  "," 234 ","56789"," ABC ","  D  "};
        } else {
            arr = new String[]{"123","456","789"};
        }
        char[][] pad = new char[arr.length][];
        int x = 0, y = 0;
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            pad[i] = s.toCharArray();
            if (s.contains(start)) {
                x = s.indexOf(start);
                y = i;
            }
        }
        String res = "";
        for (String s: input) {
            for (char c: s.toCharArray()) {
                switch (c) {
                    case 'U':
                        y--;
                        if (y < 0 || pad[y][x] == ' ') y++;
                        break;
                    case 'D':
                        y++;
                        if (y >= pad.length || pad[y][x] == ' ') y--;
                        break;
                    case 'L':
                        x--;
                        if (x < 0 || pad[y][x] == ' ') x++;
                        break;
                    case 'R':
                        x++;
                        if (x >= pad.length || pad[y][x] == ' ') x--;
                        break;
                }
            }
            res += pad[y][x];
        }
        return res;
    }
}
