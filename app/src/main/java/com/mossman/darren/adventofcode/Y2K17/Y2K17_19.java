package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_19 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_19 puzzle = new Y2K17_19(true);
        String s = puzzle.run();
        test(s, "ABCDEF");

        s = puzzle.run(true);
        test(s, 38);

        puzzle = new Y2K17_19(false);
        s = puzzle.run();
        System.out.printf("day 19: part 1 = %s\n", s);
        //test(s, "ITSZCJNMUO");

        s = puzzle.run(true);
        System.out.printf("day 19: part 2 = %s\n", s);
        //test(s, 17420);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_19(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public String run() {
        return run(false);
    }

    enum Direction {right, up, left, down};

    public String run(boolean part2) {
        Direction dir = Direction.down;
        String res = "";
        int cnt = 0;

        int y = 0;
        String s = input.get(y);
        int x = s.indexOf('|');
        while (true) {
            if (x < 0 || x >= s.length()) break;
            if (y < 0 || y >= input.size()) break;

            char c = s.charAt(x);
            if (Character.isUpperCase(c)) {
                res += c;
            } else if (c == '+') {
                if (dir == Direction.right || dir == Direction.left) {
                    if (y == 0) {
                        dir = Direction.down;
                    } else {
                        s = input.get(y - 1);
                        if (s.charAt(x) == ' ') {
                            dir = Direction.down;
                        } else {
                            dir = Direction.up;
                        }
                    }
                } else /* up/down */ {
                    if (x == 0 || s.charAt(x-1) == ' ') {
                        dir = Direction.right;
                    } else {
                        dir = Direction.left;
                    }
                }
            } else if (c == ' ') {
                break;
            }

            switch (dir) {
                case right:
                    x++;
                    break;
                case up:
                    y--;
                    s = input.get(y);
                    break;
                case left:
                    x--;
                    break;
                case down:
                    y++;
                    s = input.get(y);
                    break;
            }
            cnt++;
        }
        if (part2) {
            return ""+cnt;
        } else {
            return res;
        }

    }
}
