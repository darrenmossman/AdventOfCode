package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_8 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_8 puzzle;

        puzzle = new Y2K17_8(true);
        int i = puzzle.run();
        test(i, 1);

        i = puzzle.run(true);
        test(i, 10);

        puzzle = new Y2K17_8(false);
        i = puzzle.run();
        System.out.printf("day 8: part 1 = %d\n", i);
        //test(i, 4647);

        i = puzzle.run(true);
        System.out.printf("day 8: part 2 = %d\n", i);
        //test(i, 5590);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_8(boolean test) {
        input = Utils.readFile(getFilename(test));
    }
    public int run() {
        return run(false);
    }

    public int run(boolean part2) {
        int max = 0;
        HashMap<String, Integer> registers = new HashMap<>(input.size());
        for (String s: input) {
            String[] arr = s.split(" if ");
            String[] arr1 = arr[0].split(" ");
            String[] arr2 = arr[1].split(" ");
            String reg = arr1[0];
            String optr = arr1[1];
            int val = Integer.parseInt(arr1[2]);
            String creg = arr2[0];
            String cond = arr2[1];
            int cval = Integer.parseInt(arr2[2]);

            Integer vr = registers.get(reg);
            if (vr == null) {
                vr = 0;
                registers.put(reg, vr);
            }
            Integer vc = registers.get(creg);
            if (vc == null) {
                vc = 0;
                registers.put(creg, vc);
            }
            boolean condition = false;
            switch(cond) {
                case ">":
                    condition = vc > cval;
                    break;
                case "<":
                    condition = vc < cval;
                    break;
                case ">=":
                    condition = vc >= cval;
                    break;
                case "<=":
                    condition = vc <= cval;
                    break;
                case "==":
                    condition = vc == cval;
                    break;
                case "!=":
                    condition = vc != cval;
                    break;
            }
            if (condition) {
                switch (optr) {
                    case "inc":
                        vr += val;
                        break;
                    case "dec":
                        vr -= val;
                        break;
                }
                if (part2) {
                    if (vr > max) max = vr;
                }
                registers.put(reg, vr);
            }
        }

        if (!part2) {
            for (Integer val : registers.values()) {
                if (val > max) max = val;
            }
        }
        return max;
    }
}
