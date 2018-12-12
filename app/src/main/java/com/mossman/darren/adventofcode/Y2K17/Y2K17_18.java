package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_18 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_18 puzzle = new Y2K17_18(true);
        long i = puzzle.run();
        test(i, 4);

        puzzle = new Y2K17_18(false);
        i = puzzle.run();
        System.out.printf("day 18: part 1 = %d\n", i);
        //test(i, 7071);

        i = puzzle.run(true);
        System.out.printf("day 18: part 2 = %d\n", i);
        //test(i, 8001);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String[]> str;

    public Y2K17_18(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        str = Utils.parseInput(input, " ");
    }

    private static long getValue(HashMap<String, Long> registers, String reg) {
        Long value = registers.get(reg);
        if (value == null) {
            return 0;
        } else {
            return value;
        }
    }
    private static long getVal(HashMap<String, Long> registers, String s) {
        long val;
        try {
            val = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            val = getValue(registers, s);
        }
        return val;
    }

    private static void setValue(HashMap<String, Long> registers, String reg, long value) {
        registers.put(reg, value);
    }

    private static class Program {
        ArrayList<String[]> str;
        boolean part2;
        int pos = 0;
        HashMap<String, Long> registers = new HashMap<>();
        ArrayList<Long> rcvQueue = new ArrayList<>();
        long sound = 0;
        Long lastSound = null;
        boolean blocked = false;
        Long sent = 0L;

        public Program(ArrayList<String[]> str, boolean part2, int id) {
            this.str = str;
            this.part2 = part2;
            setValue(registers, "p", id);
        }

        public boolean process(ArrayList<Long> sndQueue) {
            if (pos < 0 || pos >= str.size()) {
                return false;
            }
            blocked = false;
            String[] instr = str.get(pos);
            String op = instr[0];
            long x, y;
            switch (op) {
                case "snd":
                    if (part2) {
                        x = getVal(registers, instr[1]);
                        sndQueue.add(x);
                        sent++;
                    } else {
                        sound = getVal(registers, instr[1]);
                    }
                    break;
                case "rcv":
                    if (part2) {
                        if (rcvQueue.size() > 0) {
                            x = rcvQueue.remove(0);
                            setValue(registers, instr[1], x);
                        } else {
                            blocked = true;
                            return false;
                        }
                    } else {
                        x = getVal(registers, instr[1]);
                        if (x != 0) {
                            lastSound = sound;
                        }
                    }
                    break;
                case "set":
                    y = getVal(registers, instr[2]);
                    setValue(registers, instr[1], y);
                    break;
                case "add":
                    x = getVal(registers, instr[1]);
                    y = getVal(registers, instr[2]);
                    setValue(registers, instr[1], x+y);
                    break;
                case "mul":
                    x = getVal(registers, instr[1]);
                    y = getVal(registers, instr[2]);
                    setValue(registers, instr[1], x*y);
                    break;
                case "mod":
                    x = getVal(registers, instr[1]);
                    y = getVal(registers, instr[2]);
                    setValue(registers, instr[1], x%y);
                    break;
                case "jgz":
                    x = getVal(registers, instr[1]);
                    y = getVal(registers, instr[2]);
                    if (x > 0) {
                        pos += y;
                        pos--;
                    }
                    break;
            }
            pos++;
            return true;
        }
    }

    public long run() {
        return run(false);
    }

    public long run(boolean part2) {
        if (part2) {
            Program prog0 = new Program(str, true, 0);
            Program prog1 = new Program(str, true, 1);
            while (true) {
                boolean res0 = prog0.process(prog1.rcvQueue);
                boolean res1 = prog1.process(prog0.rcvQueue);
                if ((!res0 && !res1) || (prog0.blocked && prog1.blocked)) break;
            }
            return prog1.sent;
        } else {
            Program prog = new Program(str, false, 0);
            while (prog.pos < prog.str.size() && prog.lastSound == null) {
                prog.process(null);
            }
            return prog.lastSound;
        }
    }
}
