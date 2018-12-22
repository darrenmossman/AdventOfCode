package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_21 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_21 puzzle;
        int i;
        long l;

        l = System.currentTimeMillis();
        puzzle = new Y2K18_21(false);
        i = puzzle.part1();
        l = System.currentTimeMillis() - l;
        System.out.printf("day 21: part 1 = %d, %d milliseconds\n", i, l);
        //test(i, 12980435);

        l = System.currentTimeMillis();
        i = puzzle.part2();
        l = (System.currentTimeMillis() - l) / 1000L;
        System.out.printf("day 21: part 2 = %d, %d seconds\n", i, l);
        //test(i, 14431711);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<Instruction> program;
    private int[] reg;
    private int ip, ipreg;

    public Y2K18_21(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        String[] arr = input.get(0).split(" ");
        ipreg = Integer.parseInt(arr[1]);
        input.remove(0);
        program = buildProgram(input);
    }

    // region register routines
    private void addr(int a, int b, int c) {
        reg[c] = reg[a] + reg[b];
    }
    private void addi(int a, int b, int c) {
        reg[c] = reg[a] + b;
    }
    private void mulr(int a, int b, int c) {
        reg[c] = reg[a] * reg[b];
    }
    private void muli(int a, int b, int c) {
        reg[c] = reg[a] * b;
    }
    private void banr(int a, int b, int c) {
        reg[c] = reg[a] & reg[b];
    }
    private void bani(int a, int b, int c) {
        reg[c] = reg[a] & b;
    }
    private void borr(int a, int b, int c) {
        reg[c] = reg[a] | reg[b];
    }
    private void bori(int a, int b, int c) {
        reg[c] = reg[a] | b;
    }
    private void setr(int a, int b, int c) {
        reg[c] = reg[a];
    }
    private void seti(int a, int b, int c) {
        reg[c] = a;
    }
    private void gtir(int a, int b, int c) {
        if (a > reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void gtri(int a, int b, int c) {
        if (reg[a] > b) reg[c] = 1;
        else reg[c] = 0;
    }
    private void gtrr(int a, int b, int c) {
        if (reg[a] > reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqir(int a, int b, int c) {
        if (a == reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqri(int a, int b, int c) {
        if (reg[a] == b) reg[c] = 1;
        else reg[c] = 0;
    }
    private void eqrr(int a, int b, int c) {
        if (reg[a] == reg[b]) reg[c] = 1;
        else reg[c] = 0;
    }
    // endregion

    @FunctionalInterface
    private interface InstructionMethod {
        void execute(int a, int b, int c);
    }

    private class Instruction {
        private InstructionMethod method;
        private int a, b, c;

        private Instruction(InstructionMethod method, int a, int b, int c) {
            this.method = method;
            this.a = a;
            this.b = b;
            this.c = c;

        }
        private void execute() {
            reg[ipreg] = ip;
            method.execute(a, b, c);
            ip = reg[ipreg];
        }
    }

    private ArrayList<Instruction> buildProgram(ArrayList<String> input) {

        // build program parsing input just once to speed up execution time
        ArrayList<Instruction> program = new ArrayList<>(input.size());

        for (String line: input) {
            String[] arr = line.split(" ");
            String instr = arr[0];
            int a = Integer.parseInt(arr[1]);
            int b = Integer.parseInt(arr[2]);
            int c = Integer.parseInt(arr[3]);

            InstructionMethod method = null;
            switch (instr) {
                case "addr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { addr (a, b, c); }
                    };
                    break;
                case "addi":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { addi (a, b, c); }
                    };
                    break;
                case "mulr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { mulr (a, b, c); }
                    };
                    break;
                case "muli":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { muli (a, b, c); }
                    };
                    break;
                case "banr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { banr (a, b, c); }
                    };
                    break;
                case "bani":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { bani (a, b, c); }
                    };
                    break;
                case "borr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { borr (a, b, c); }
                    };
                    break;
                case "bori":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { bori (a, b, c); }
                    };
                    break;
                case "setr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { setr (a, b, c); }
                    };
                    break;
                case "seti":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { seti (a, b, c); }
                    };
                    break;
                case "gtir":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { gtir (a, b, c); }
                    };
                    break;
                case "gtri":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { gtri (a, b, c); }
                    };
                    break;
                case "gtrr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { gtrr (a, b, c); }
                    };
                    break;
                case "eqir":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { eqir (a, b, c); }
                    };
                    break;
                case "eqri":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { eqri (a, b, c); }
                    };
                    break;
                case "eqrr":
                    method = new InstructionMethod() {
                        public void execute(int a, int b, int c) { eqrr (a, b, c); }
                    };
                    break;
            }

            Instruction instruction = new Instruction(method, a, b, c);
            program.add(instruction);
        }
        return program;
    }

    private int executeProgram(ArrayList<Instruction> program, boolean part2) {
        ArrayList<Integer> arr = new ArrayList<>();
        reg = new int[6];
        ip = 0;

        while (ip >= 0 && ip < program.size()) {
            Instruction instruction = program.get(ip);

            if (ip == 28) {
                if (part2) {
                    int idx = arr.indexOf(reg[2]);
                    if (idx == -1) {
                        arr.add(reg[2]);
                    } else {
                        return arr.get(arr.size()-1);
                    }
                }
                else {
                    return reg[2];
                }
            }
            instruction.execute();
            ip++;
        }
        return 0;
    }

    public int part1() {
        return executeProgram(program, false);
    }

    public int part2() {
        return executeProgram(program, true);
    }
}
