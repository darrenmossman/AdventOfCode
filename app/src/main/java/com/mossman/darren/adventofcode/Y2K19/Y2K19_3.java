package com.mossman.darren.adventofcode.Y2K19;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.MovingObject;
import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K19_3 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K19_3 puzzle = new Y2K19_3(true);
        int i = puzzle.part1();
        test(i, 159);
        i = puzzle.part2();
        test(i, 610);

        puzzle = new Y2K19_3(false);
        i = puzzle.part1();
        System.out.printf("day 3: part 1 = %d\n", i);
        test(i, 1337);

        i = puzzle.part2();
        System.out.printf("day 3: part 2 = %d\n", i);
        test(i, 65356);
    }

    //--------------------------------------------------------------------------------------------


    ArrayList<String[]> paths;
    InfiniteGrid<Character> grid;

    public Y2K19_3(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        paths = Utils.parseInput(input, ",");
        grid = new InfiniteGrid('.');
    }

    private int part1() {

        ArrayList<MovingObject> wires = new ArrayList<>();
        wires.add(new MovingObject('1'));
        wires.add(new MovingObject('2'));

        grid.put(0, 0, 'o');

        Integer minDist = null;

        for (int w = 0; w < 2; w++) {
            MovingObject wire = wires.get(w);
            String[] wirePath = paths.get(w);

            for (String path : wirePath) {
                char dir = path.charAt(0);
                wire.setDir(dir);
                int len = Integer.parseInt(path.substring(1));
                for (int a = 0; a < len; a++) {
                    wire.advance();
                    char c = grid.get(wire.x, wire.y);
                    if (c == '.') {
                        grid.put(wire.x, wire.y, wire.mark);
                    }
                    else if (c != wire.mark && c != 'o') {
                        grid.put(wire.x, wire.y, 'X');
                        int dist = wire.manhattenDistance();
                        if (minDist == null || dist < minDist) minDist = dist;
                    }
                }
            }
        }
        return minDist;
    }

    private int part2() {

        ArrayList<MovingObject> wires = new ArrayList<>();
        wires.add(new MovingObject('1'));
        wires.add(new MovingObject('2'));

        Integer minDist = null;

        InfiniteGrid<Integer> intersection = new InfiniteGrid(0);

        for (int w = 0; w < 2; w++) {
            MovingObject wire = wires.get(w);
            String[] wirePath = paths.get(w);

            int dist = 0;
            for (String path : wirePath) {
                char dir = path.charAt(0);
                wire.setDir(dir);
                int len = Integer.parseInt(path.substring(1));
                for (int a = 0; a < len; a++) {
                    wire.advance();
                    dist++;
                    char c = grid.get(wire.x, wire.y);
                    if (c == 'X') {
                        int sum = intersection.get(wire.x, wire.y);
                        sum += dist;
                        intersection.put(wire.x, wire.y, sum);
                        if (w == 1) {
                            if (minDist == null || sum < minDist) minDist = sum;
                        }
                    }
                }
            }
        }
        return minDist;
    }
}