package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Y2K18_16 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_16 puzzle;

        puzzle = new Y2K18_16(false);
        puzzle.run();
        System.out.printf("day 16: part 1 = %d\n", puzzle.part1);
        test(puzzle.part1, 640);

        System.out.printf("day 16: part 2 = %d\n", puzzle.part2);
        test(puzzle.part2, 472);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    public int part1, part2;

    public Y2K18_16(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private class Sample {
        int[] areg, breg;
        int[] instr;
        int opcode;

        private Sample(String before, String mid, String after) {
            before = before.replaceAll("Before: *", "");
            before = before.substring(1, before.length()-1);
            after = after.replaceAll("After: *", "");
            after = after.substring(1, after.length()-1);

            int r;
            breg = new int[4];
            r = 0;
            for (String s: before.split(", ")) {
                breg[r++] = Integer.parseInt(s);
            }
            areg = new int[4];
            r = 0;
            for (String s: after.split(", ")) {
                areg[r++] = Integer.parseInt(s);
            }
            instr = new int[4];
            r = 0;
            for (String s: mid.split(" ")) {
                instr[r++] = Integer.parseInt(s);
            }
            opcode = instr[0];
        }
    }
    private void addr(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] + reg[b];
    }
    private void addi(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] + b;
    }
    private void mulr(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] * reg[b];
    }
    private void muli(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] * b;
    }
    private void banr(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] & reg[b];
    }
    private void bani(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] & b;
    }
    private void borr(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] | reg[b];
    }
    private void bori(int[] reg, int a, int b, int c) {
        reg[c] = reg[a] | b;
    }
    private void setr(int[] reg, int a, int b, int c) {
        reg[c] = reg[a];
    }
    private void seti(int[] reg, int a, int b, int c) {
        reg[c] = a;
    }
    private void gtir(int[] reg, int a, int b, int c) {
        if (a > reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void gtri(int[] reg, int a, int b, int c) {
        if (reg[a] > b) reg[c] = 1;
        else reg[c] = 0;
    }
    private void gtrr(int[] reg, int a, int b, int c) {
        if (reg[a] > reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqir(int[] reg, int a, int b, int c) {
        if (a == reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqri(int[] reg, int a, int b, int c) {
        if (reg[a] == b) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqrr(int[] reg, int a, int b, int c) {
        if (reg[a] == reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }

    private int[] execute(int i, int[] before, int[] instr) {
        int[] reg = before.clone();
        switch (i) {
            case 0:
                addr(reg, instr[1], instr[2], instr[3]);
                break;
            case 1:
                addi(reg, instr[1], instr[2], instr[3]);
                break;
            case 2:
                mulr(reg, instr[1], instr[2], instr[3]);
                break;
            case 3:
                muli(reg, instr[1], instr[2], instr[3]);
                break;
            case 4:
                banr(reg, instr[1], instr[2], instr[3]);
                break;
            case 5:
                bani(reg, instr[1], instr[2], instr[3]);
                break;
            case 6:
                borr(reg, instr[1], instr[2], instr[3]);
                break;
            case 7:
                bori(reg, instr[1], instr[2], instr[3]);
                break;
            case 8:
                setr(reg, instr[1], instr[2], instr[3]);
                break;
            case 9:
                seti(reg, instr[1], instr[2], instr[3]);
                break;
            case 10:
                gtir(reg, instr[1], instr[2], instr[3]);
                break;
            case 11:
                gtri(reg, instr[1], instr[2], instr[3]);
                break;
            case 12:
                gtrr(reg, instr[1], instr[2], instr[3]);
                break;
            case 13:
                eqir(reg, instr[1], instr[2], instr[3]);
                break;
            case 14:
                eqri(reg, instr[1], instr[2], instr[3]);
                break;
            case 15:
                eqrr(reg, instr[1], instr[2], instr[3]);
                break;
        }
        return reg;
    }

    public void run() {

        ArrayList<Sample> samples = new ArrayList<>();
        ArrayList<int[]> instructions = new ArrayList<>();
        for (int i = 0; i < input.size(); i += 4) {
            String s = input.get(i);
            if (s.startsWith("Before")) {
                samples.add(new Sample(s, input.get(i+1), input.get(i+2)));
            }
            else {
                for (int j = i + 2; j < input.size(); j++) {
                    s = input.get(j);
                    int[] instr = new int[4];
                    int r = 0;
                    for (String op : s.split(" ")) {
                        instr[r++] = Integer.parseInt(op);
                    }
                    instructions.add(instr);
                }
                break;
            }
        }

        HashMap<Integer, ArrayList<Integer>> maps = new HashMap<>();
        part1 = 0;
        for (Sample sample: samples) {
            int cnt = 0;
            ArrayList<Integer> matches = maps.get(sample.opcode);

            if (matches == null) {
                matches = new ArrayList<>();
                maps.put(sample.opcode, matches);
            }
            for (int idx = 0; idx < 16; idx++) {
                int[] reg = execute(idx, sample.breg, sample.instr);
                if (Arrays.equals(reg, sample.areg)) {
                    if (!matches.contains(idx)) {
                        matches.add(idx);
                    }
                    cnt++;
                }
            }
            if (cnt >= 3) {
                part1++;
            }
        }

        int cnt = 0;
        int[] map = new int[16];
        while (cnt < 16) {
            for (Map.Entry<Integer, ArrayList<Integer>> entry: maps.entrySet()) {
                ArrayList<Integer> matches = entry.getValue();
                int opcode = entry.getKey();
                if (matches.size() == 1) {
                    Integer idx = matches.get(0);
                    map[opcode] = idx;
                    cnt++;
                    for (Map.Entry<Integer, ArrayList<Integer>> e: maps.entrySet()) {
                        ArrayList<Integer> m = e.getValue();
                        m.remove(idx);
                    }
                }
            }
        }

        int[] reg = new int[4];
        for (int[] instr: instructions) {
            int opcode = instr[0];
            int idx = map[opcode];
            reg = execute(idx, reg, instr);
        }
        part2 = reg[0];
    }
}













