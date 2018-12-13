package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.MovingObject;
import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_22 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_22 puzzle;
        int i;

        puzzle = new Y2K17_22(true);
        i = puzzle.run(10000);
        test(i, 5587);

        i = puzzle.run(100, true);
        test(i, 26);
        i = puzzle.run(10000000, true);
        test(i, 2511944);

        puzzle = new Y2K17_22(false);
        i = puzzle.run(10000);
        System.out.printf("day 22: part 1 = %d\n", i);
        //test(i, 5280);

        i = puzzle.run(10000000, true);
        System.out.printf("day 22: part 2 = %d\n", i);
        //test(i, 2512261);
    }

    //--------------------------------------------------------------------------------------------

    ArrayList<String> input;
    HashMap<Integer, HashMap<Integer, Character>> grid;

    public Y2K17_22(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private Character get(int x, int y) {
        HashMap<Integer, Character> row = grid.get(y);
        if (row == null) {
            row = new HashMap<>();
            grid.put(y, row);
        }
        Character node = row.get(x);
        if (node == null) {
            node = '.';
            row.put(x, node);
        }
        return node;
    }

    private void put(int x, int y, Character node) {
        HashMap<Integer, Character> row = grid.get(y);
        row.put(x, node);
    }

    private void init() {
        grid = new HashMap<>();
        for (int r = 0; r < input.size(); r++) {
            String s = input.get(r);
            HashMap<Integer, Character> row = new HashMap<>();
            grid.put(r, row);
            for (int c = 0; c < s.length(); c++) {
                row.put(c, s.charAt(c));
            }
        }
    }

    public int run(int iterations) {
        return run(iterations, false);
    }

    private static class Virus extends MovingObject {
        private boolean part2;
        public Virus(boolean part2, int x, int y, Direction dir) { 
            super(x,y,dir);
            this.part2 = part2;
        }

        private boolean react(char c) {
            boolean infected = false;
            if (part2) {
                switch (c) {
                    case '.':
                        turnLeft();
                        put(x, y, 'W');
                        break;
                    case 'W':
                        infected = true;
                        put(x, y, '#');
                        break;
                    case '#':
                        turnRight();
                        put(x, y, 'F');
                        break;
                    case 'F':
                        reverse();
                        put(x, y, '.');
                        break;
                }
            } else {
                switch (c) {
                    case '.':
                        turnLeft();
                        infected = true;
                        put(x, y, '#');
                        break;
                    case '#':
                        turnRight();
                        put(x, y, '.');
                        break;
                }
            }
            return infected;
        }
    }

    public int run(int iterations, boolean part2) {
        init();

        HashMap<Integer, Character> row = grid.get(0);

        int x = row.size() / 2;
        int y = grid.size() / 2;
        Virus virus = new Virus(part2, x, y, MovingObject.Direction.up);

        int res = 0;
        for (int i = 0; i < iterations; i++) {
            Character node = get(virus.x, virus.y);
            if (virus.react(node)) {
                res++;
            }
            virus.advance();
        }
        return res;
    }
}
