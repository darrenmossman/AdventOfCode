package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.MovingObject;
import com.mossman.darren.adventofcode.MovingObject.Direction;
import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K16_1 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_1 puzzle = new Y2K16_1(new String[]{"R5", "L5", "R5", "R3"});
        int i = puzzle.run(false);
        test(i, 12);

        puzzle = new Y2K16_1(new String[]{"R8","R4","R4","R8"});
        i = puzzle.run(true);
        test(i, 4);

        puzzle = new Y2K16_1(false);
        i = puzzle.run();
        System.out.printf("day 1: part 1 = %d\n", i);
        //test(i, 230);

        i = puzzle.run(true);
        System.out.printf("day 1: part 2 = %d\n", i);
        //test(i, 154);
    }

    //--------------------------------------------------------------------------------------------

    private String[] directions;

    public Y2K16_1(String[] directions) {
        this.directions = directions;
    }

    public Y2K16_1(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, ", ");
        directions = str.get(0);
    }

    public int run() {
        return run(false);
    }

    public int run(boolean part2) {
        InfiniteGrid<Boolean> locs = null;
        if (part2) {
            locs = new InfiniteGrid<>();
            locs.put(0, 0, true);
        }

        MovingObject me = new MovingObject(0,0, Direction.up);
        for (String s: directions) {
            char c = s.charAt(0);
            int blocks = Integer.parseInt(s.substring(1));
            if (c == 'R') {
                me.turnRight();
            } else if (c == 'L') {
                me.turnLeft();
            } else {
                throw new IllegalStateException("Unknown direction");
            }
            if (part2) {
                for (int i = 0 ; i < blocks; i++) {
                    me.advance();
                    if (locs.contains(me.x, me.y)) {
                        return me.manhattenDistance();
                    } else {
                        locs.put(me.x, me.y, true);
                    }
                }
            } else {
                me.advance(blocks);
            }
        }
        if (part2) {
            throw new IllegalStateException("Nowhere twice");
        } else {
            return me.manhattenDistance();
        }
    }
}
