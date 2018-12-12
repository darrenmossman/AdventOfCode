package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_23 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_23 puzzle = new Y2K17_23(false);

        long i = puzzle.run();
        System.out.printf("day 23: part 1 = %d\n", i);
        //test(i, 3969);

        i = puzzle.run(true);
        System.out.printf("day 23: part 2 = %d\n", i);
        //test(i, 917);
    }
    //--------------------------------------------------------------------------------------------

    private ArrayList<String[]> str;

    public Y2K17_23(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        str = Utils.parseInput(input, " ");
    }

    private static class Program {

        private void setValue(String reg, long value) {
            registers.put(reg, value);
        }

        private long getValue(String reg) {
            Long value = registers.get(reg);
            if (value == null) {
                return 0;
            } else {
                return value;
            }
        }
        private long getVal(String s) {
            long val;
            try {
                val = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                val = getValue(s);
            }
            return val;
        }
        
        private ArrayList<String[]> str;
        private int pos = 0;
        private HashMap<String, Long> registers = new HashMap<>();
        private long mulCount = 0L;

        public Program(ArrayList<String[]> str) {
            this.str = str;
        }

        public boolean process() {
            if (pos < 0 || pos >= str.size()) {
                return false;
            }
            String[] instr = str.get(pos);
            String op = instr[0];
            long x, y;
            switch (op) {
                case "set":
                    y = getVal(instr[2]);
                    setValue(instr[1], y);
                    break;
                case "sub":
                    x = getVal(instr[1]);
                    y = getVal(instr[2]);
                    setValue(instr[1], x-y);
                    break;
                case "mul":
                    x = getVal(instr[1]);
                    y = getVal(instr[2]);
                    setValue(instr[1], x*y);
                    mulCount++;
                    break;
                case "jnz":
                    x = getVal(instr[1]);
                    y = getVal(instr[2]);
                    if (x != 0) {
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

    private int nonPrimeCount(int val, int loops, int step) {
        int result = 0;
        for (int i = 0; i <= loops; i++) {
            boolean prime = true;
            for (int d = 2; d <= Math.sqrt(val); d++) {
                if (val % d == 0) {
                    prime = false;
                    break;
                }
            }
            if (!prime) {
                result++;
            }
            val += step;
        }
        return result;
    }

    public long run(boolean part2) {
        if (part2) {
            int val = Integer.parseInt(str.get(0)[2]);
            val = val * 100 + 100000;
            return nonPrimeCount(val, 1000, 17);
        }
        else {
            Program prog = new Program(str);
            while (prog.process()) ;
            return prog.mulCount;
        }
    }
}

/* input assembly converted to pseudo code, on way to working out logic for nonPrimeCount function
...
set c b			b = 106500
sub c -17000	c = 123500
set f 1			f = 1	<---------------------------<---------------------------------------<
set d 2			d = 2																		^
set e 2			e = 2	<---------------------------<-----------------------------------<	^
set g d					<---------------------------<-------------------------------<	^	^
mul g e																				^	^	^
sub g b																				^	^	^
jnz g 2			if (d*e == b)     													^	^	^
set f 0				f = 0															^	^	^
sub e -1		e++;																^	^	^
set g e																				^	^	^
sub g b																				^	^	^
jnz g -8		if (e != b) >--------------------------------->---------------------^   ^	^
sub d -1		d++ 																	^	^
set g d																					^	^
sub g b																					^	^
jnz g -13		if (d != b) >--------------------------------->-------------------------^   ^
jnz f 2			if (f == 0) 																^
sub h -1			h++;	 																^
set g b			 																			^
sub g c					 																	^
jnz g 2			if (b == c) 																^
jnz 1 3				return 																	^
sub b -17		b -= 17 																	^
jnz 1 -23		>--------------------------------------------->-----------------------------^
*/