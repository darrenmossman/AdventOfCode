package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_12 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_12 puzzle = new Y2K17_12(true);
        int i = puzzle.run();
        test(i, 6);

        i = puzzle.run(true);
        test(i, 2);

        puzzle = new Y2K17_12(false);
        i = puzzle.run();
        System.out.printf("day 12: part 1 = %d\n", i);
        //test(i, 141);

        i = puzzle.run(true);
        System.out.printf("day 12: part 2 = %d\n", i);
        //test(i, 171);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_12(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private static class Program {
        private HashMap<String, Program> owner;
        private String id;
        private String[] progs;

        private Program(HashMap<String, Program> owner, String id, String[] progs) {
            this.owner = owner;
            this.id = id;
            this.progs = progs;
            owner.put(id, this);
        }

        private void addIds(ArrayList<String> res) {
            res.add(id);
            for (String id: progs) {
                if (!res.contains(id)) {
                    Program prog = owner.get(id);
                    prog.addIds(res);
                }
            }
        }

    }

    public int run() {
        return run(false);
    }

    public int run(boolean countGroup) {
        HashMap<String, Program> owner = new HashMap<>(input.size());
        for (String s: input) {
            s = s.replaceAll(" ", "");
            String[] arr = s.split("<->");
            String id = arr[0];
            String[] progs = arr[1].split(",");
            new Program(owner, id, progs);
        }
        ArrayList<String> res = new ArrayList<>();

        if (countGroup) {
            int cnt = 0;
            for (String id: owner.keySet()) {
                Program program = owner.get(id);
                if (!res.contains(id)) {
                    program.addIds(res);
                    cnt++;
                }
            }
            return cnt;
        } else {
            Program program = owner.get("0");
            program.addIds(res);
            return res.size();
        }
    }
}
