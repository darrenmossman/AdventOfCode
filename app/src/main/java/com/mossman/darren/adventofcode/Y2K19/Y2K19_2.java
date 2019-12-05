package com.mossman.darren.adventofcode.Y2K19;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K19_2 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K19_2 puzzle = new Y2K19_2(false);
        int i = puzzle.part1(12, 2);
        System.out.printf("day 2: part 1 = %d\n", i);
        test(i, 3850704);

        i = puzzle.part2(19690720);
        System.out.printf("day 2: part 2 = %d\n", i);
        test(i, 6718);
    }

    //--------------------------------------------------------------------------------------------



    ArrayList<String> input;
    private Integer[] opcodes;

    public Y2K19_2(Integer[] opcodes) {
        this.opcodes = opcodes;
    }

    public Y2K19_2(boolean test) {
        input = Utils.readFile(getFilename(test));
        initOpCodes();
    }

    private void initOpCodes() {
        ArrayList<String[]> str =  Utils.parseInput(input, ",");
        ArrayList<Integer[]> vals = Utils.strArraysToNum(str);
        opcodes = vals.get(0);
    }

    public int part1(int noun, int verb) {

        opcodes[1] = noun;
        opcodes[2] = verb;

        int pos = 0;
        while (true) {
            int opcode = opcodes[pos];
            if (opcode == 99) {
                break;
            }
            int p1 = opcodes[pos+1];
            int p2 = opcodes[pos+2];
            int p3 = opcodes[pos+3];
            int i1 = opcodes[p1];
            int i2 = opcodes[p2];

            if (opcode == 1) {
                int res = i1 + i2;
                opcodes[p3] = res;
                pos += 4;
            }
            else if (opcode == 2) {
                int res = i1 * i2;
                opcodes[p3] = res;
                pos += 4;
            }
            else {
                System.out.printf("unexpected opcode: %d at pos %d\n", opcode, pos);
                break;
            }
        }

        return opcodes[0];
    }

    public int part2(int expect) {

        int res = 0;
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                initOpCodes();
                int r = part1(noun, verb);
                if (r == expect) {
                    res = 100 * noun + verb;
                    break;
                }
            }
        }
        return res;
    }
}
