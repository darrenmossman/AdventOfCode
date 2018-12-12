package com.mossman.darren.adventofcode.Y2K17;

import java.util.ArrayList;
import java.util.Collections;

public class Y2K17_10 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_10 puzzle = new Y2K17_10();
        int[] input = new int[]{3,4,1,5};
        int i = puzzle.part1(5, input);
        test(i, 12);

        String s = puzzle.part2("");
        test(s, "a2582a3a0e66e6e86e3812dcb672a272");
        s = puzzle.part2("AoC 2017");
        test(s, "33efeb34ea91902bb2f59c9920caa6cd");
        s = puzzle.part2("1,2,3");
        test(s, "3efbe78a8d82f29979031a4aa0b16a9d");
        s = puzzle.part2("1,2,4");
        test(s, "63960835bcdc130f0b66d7ff4f6a5a8e");

        input = new int[]{212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164};
        i = puzzle.part1(256, input);
        System.out.printf("day 10: part 1 = %d\n", i);
        //test(i, 212);

        s = puzzle.part2("212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164");
        System.out.printf("day 10: part 2 = %s\n", s);
        //test(s, "96de9657665675b51cd03f0b3528ba26");
    }

    //--------------------------------------------------------------------------------------------
    
    public int part1(int size, int[] input) {
        int[] list = new int[size];
        for (int i = 0; i < size; i++) {
            list[i] = i;
        }
        int pos = 0;
        int skip = 0;
        for (int len: input) {
            // reverse len numbers from pos
            if (len > 1) {
                ArrayList<Integer> rev = new ArrayList<>(len);
                for (int i = 0; i < len; i++) {
                    rev.add(list[(pos+i) % list.length]);
                }
                Collections.reverse(rev);
                for (int i = 0; i < len; i++) {
                    list[(pos + i) % list.length] = rev.get(i);
                }
            }
            // move forward
            pos += len + skip;
            pos = pos % list.length;
            skip++;
        }
        return list[0]*list[1];
    }

    public String part2(String s) {

        final int size = 256;
        int[] list = new int[size];
        for (int i = 0; i < size; i++) {
            list[i] = i;
        }

        final int[] suffix = {17, 31, 73, 47, 23};
        int[] input = new int[s.length()+suffix.length];
        for (int i = 0; i < s.length(); i++) {
            input[i] = s.charAt(i);
        }
        for (int i = 0; i < suffix.length; i++) {
            input[input.length-5+i] = suffix[i];
        }

        int pos = 0;
        int skip = 0;
        for (int round = 0; round < 64; round++) {
            for (int len : input) {
                // reverse len numbers from pos
                if (len > 1) {
                    ArrayList<Integer> rev = new ArrayList<>(len);
                    for (int i = 0; i < len; i++) {
                        rev.add(list[(pos + i) % list.length]);
                    }
                    Collections.reverse(rev);
                    for (int i = 0; i < len; i++) {
                        list[(pos + i) % list.length] = rev.get(i);
                    }
                }
                // move forward
                pos += len + skip;
                pos = pos % list.length;
                skip++;
            }
        }
        String res = "";
        int p = 0;
        for (int i = 0; i < 16; i++) {
            int xor = 0;
            for (int j = 0; j < 16; j++) {
                int v = list[p];
                xor = xor ^ v;
                p++;
            }
            String h = Integer.toHexString(xor);
            if (h.length() < 2) h = "0" + h;
            res += h;
        }
        return res;
    }
}
