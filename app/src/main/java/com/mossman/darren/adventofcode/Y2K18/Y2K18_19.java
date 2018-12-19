package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_19 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_19 puzzle;
        puzzle = new Y2K18_19(false);

        int i = puzzle.part1();
        System.out.printf("day 19: part 1 = %d\n", i);
        //test(i, 1694);

        i = puzzle.part2();
        System.out.printf("day 19: part 2 = %d\n", i);
        //test(i, 18964204);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private int[] reg;
    private int ip, ipreg;

    public Y2K18_19(boolean test) {
        input = Utils.readFile(getFilename(test));
        String[] arr = input.get(0).split(" ");
        ipreg = Integer.parseInt(arr[1]);
        input.remove(0);
    }

    // region assembly routines
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
    // endregion

    private void execute(String line) {
        String[] arr = line.split(" ");
        String instr = arr[0];
        int[] op = new int[3];
        op[0] = Integer.parseInt(arr[1]);
        op[1] = Integer.parseInt(arr[2]);
        op[2] = Integer.parseInt(arr[3]);

        reg[ipreg] = ip;
        switch (instr) {
            case "addr":
                addr(reg, op[0], op[1], op[2]);
                break;
            case "addi":
                addi(reg, op[0], op[1], op[2]);
                break;
            case "mulr":
                mulr(reg, op[0], op[1], op[2]);
                break;
            case "muli":
                muli(reg, op[0], op[1], op[2]);
                break;
            case "banr":
                banr(reg, op[0], op[1], op[2]);
                break;
            case "bani":
                bani(reg, op[0], op[1], op[2]);
                break;
            case "borr":
                borr(reg, op[0], op[1], op[2]);
                break;
            case "bori":
                bori(reg, op[0], op[1], op[2]);
                break;
            case "setr":
                setr(reg, op[0], op[1], op[2]);
                break;
            case "seti":
                seti(reg, op[0], op[1], op[2]);
                break;
            case "gtir":
                gtir(reg, op[0], op[1], op[2]);
                break;
            case "gtri":
                gtri(reg, op[0], op[1], op[2]);
                break;
            case "gtrr":
                gtrr(reg, op[0], op[1], op[2]);
                break;
            case "eqir":
                eqir(reg, op[0], op[1], op[2]);
                break;
            case "eqri":
                eqri(reg, op[0], op[1], op[2]);
                break;
            case "eqrr":
                eqrr(reg, op[0], op[1], op[2]);
                break;
        }
        ip = reg[ipreg];
        ip++;
    }

    private int executeProgram(int initialValue) {
        reg = new int[6];
        reg[0] = initialValue;

        ip = 0;
        while (ip >= 0 && ip < input.size()) {
            String line = input.get(ip);
            execute(line);
            if (line.equals("seti 0 5 4")) { // e.g. jump to beginning
                // reg[5] contains the target value to calculate sum of factors
                return reg[5];
            }
        }
        return reg[0];
    }

    public int part1() {
        return executeProgram(0);
    }

    public int part2() {
        int value = executeProgram(1);
        return sumFactors(value);
    }

    private int sumFactors(int value) {
        int res, i;
        res = 0;
        i = 1;
        while (i <= Math.sqrt(value)) {
            if (value % i == 0) {
                res = res + i;
                res = res + (value/i);
            }
            i++;
        }
        return res;
    }
}
