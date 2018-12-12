package com.mossman.darren.adventofcode.Y2K17;

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

    enum Direction {up, right, down, left};
    Direction[] dirs = Direction.values();

    public Y2K17_22(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private Direction turnRight(Direction dir) {
        return dirs[(dir.ordinal()+1) % dirs.length];
    }
    private Direction reverse(Direction dir) {
        return dirs[(dir.ordinal()+2) % dirs.length];
    }
    private Direction turnLeft(Direction dir) {
        return dirs[dir.ordinal() == 0 ? dirs.length-1 : dir.ordinal()-1];
    }

    private Character get(int r, int c) {
        HashMap<Integer, Character> row = grid.get(r);
        if (row == null) {
            row = new HashMap<>();
            grid.put(r, row);
        }
        Character node = row.get(c);
        if (node == null) {
            node = '.';
            row.put(c, node);
        }
        return node;
    }

    private void put(int r, int c, Character node) {
        HashMap<Integer, Character> row = grid.get(r);
        row.put(c, node);
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

    public int run(int iterations, boolean part2) {
        init();

        HashMap<Integer, Character> row = grid.get(0);
        int r = grid.size() / 2;
        int c = row.size() / 2;
        Direction dir = Direction.up;

        int res = 0;
        for (int i = 0; i < iterations; i++) {
            Character node = get(r, c);
            if (part2) {
                switch (node) {
                    case '.':
                        dir = turnLeft(dir);
                        put(r, c, 'W');
                        break;
                    case 'W':
                        res++;
                        put(r, c, '#');
                        break;
                    case '#':
                        dir = turnRight(dir);
                        put(r, c, 'F');
                        break;
                    case 'F':
                        dir = reverse(dir);
                        put(r, c, '.');
                        break;
                }
            } else {
                switch (node) {
                    case '.':
                        dir = turnLeft(dir);
                        res++;
                        put(r, c, '#');
                        break;
                    case '#':
                        dir = turnRight(dir);
                        put(r, c, '.');
                        break;
                }
            }
            switch (dir) {
                case up:
                    r--;
                    break;
                case right:
                    c++;
                    break;
                case down:
                    r++;
                    break;
                case left:
                    c--;
                    break;
            }
        }
        return res;
    }

}
