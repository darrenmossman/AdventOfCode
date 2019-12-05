package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K16_9 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_9 puzzle = new Y2K16_9(true);
        int i = puzzle.part1();
        System.out.printf("day 9: part 1 test = %d\n", i);
        test(i, 11);

        puzzle = new Y2K16_9(false);
        i = puzzle.part1();
        System.out.printf("day 9: part 1 = %d\n", i);
        test(i, 152851);

        long l = puzzle.part2("X(8x2)(3x3)ABCY");
        test(l, 20);
        l = puzzle.part2("(27x12)(20x12)(13x14)(7x10)(1x12)A");
        test(l, 241920);
        l = puzzle.part2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN");
        test(l, 445);

        l = puzzle.part2();
        System.out.printf("day 9: part 2 = %d\n", l);
        test(l, 11797310782L);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> data;
    private String input;


    public Y2K16_9(boolean test) {
        data = Utils.readFile(getFilename(test));
        input = data.get(0);
    }

    private long expand(String input) {
        long res = 0;
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '(') {
                int p = input.indexOf(')', i+1);
                String s = input.substring(i+1, p);
                String[] arr = s.split("x");
                int l = Integer.parseInt(arr[0]);
                int r = Integer.parseInt(arr[1]);
                i = p+1;
                s = input.substring(i, i+l);
                res += expand(s) * r;
                i += l;
            }
            else {
                res++;
                i++;
            }
        }
        return res;
    }

    public long part2(String input) {
        return expand(input);
    }

    public long part2() {
        return part2(input);
    }

    public int part1() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '(') {
                int p = input.indexOf(')', i+1);
                String s = input.substring(i+1, p);
                String[] arr = s.split("x");
                int l = Integer.parseInt(arr[0]);
                int r = Integer.parseInt(arr[1]);
                i = p+1;
                s = input.substring(i, i+l);
                for (int f = 0; f < r; f++) {
                    builder.append(s);
                }
                i += l;
            }
            else {
                builder.append(c);
                i++;
            }
        }
        return builder.length();
    }
}
