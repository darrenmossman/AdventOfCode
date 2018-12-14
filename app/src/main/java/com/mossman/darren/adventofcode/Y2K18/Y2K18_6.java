package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K18_6 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_6 puzzle = new Y2K18_6(true);
        int i = puzzle.run();
        test(i, 17);
        i = puzzle.run(32);
        test(i, 16);

        puzzle = new Y2K18_6(false);
        i = puzzle.run();
        System.out.printf("day 6: part 1 = %d\n", i);
        //test(i, 4143);

        i = puzzle.run(10000);
        System.out.printf("day 6: part 2 = %d\n", i);
        //test(i, 35039);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<Integer[]> vals;
    
    public Y2K18_6(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, ", ");
        vals = Utils.strArraysToNum(str);
    }

    public int run() {
        return run(null);
        
    }
    
    public int run(Integer safeDist) {
        //InfiniteGrid<Character> grid = new InfiniteGrid<>();
        HashMap<Character, Coord> coords = new HashMap<>(vals.size());

        char id = 'A';
        Integer xmin = null, xmax = null, ymin = null, ymax = null;
        for (Integer[] arr: vals) {
            int x = arr[0];
            int y = arr[1];
            Coord coord = new Coord(x, y, id);
            coords.put(id, coord);
            //grid.put(x, y, id);
            if (xmin == null) {
                xmin = x; xmax = x; ymin = y; ymax = y;
            } else {
                if (x < xmin) xmin = x;
                if (x > xmax) xmax = x;
                if (y < ymin) ymin = y;
                if (y > ymax) ymax = y;
            }
            id++;
        }

        int b = 1;
        int safeCount = 0;
        for (int y = ymin-b; y <= ymax+b; y++) {
            for (int x = xmin-b; x <= xmax+b; x++) {
                Integer min = null;
                Coord closest = null; boolean dot = false;
                int total = 0;
                for (Coord coord: coords.values()) {
                    int d = Math.abs(coord.x-x)+ Math.abs(coord.y-y);
                    if (safeDist != null) {
                        total += d;
                    } else {
                        if (min == null || d < min) {
                            min = d;
                            closest = coord;
                            dot = false;
                        } else if (d == min) {
                            closest = null;
                            dot = true;
                        }
                    }
                }
                if (safeDist != null) {
                    if (total < safeDist) {
                        safeCount++;
                    }
                } else {
                    char c;
                    if (dot) {
                        c = '.';
                    } else if (min == 0) {
                        c = closest.id;
                        closest.count++;
                    } else if (closest != null) {
                        c = Character.toLowerCase(closest.id);
                        closest.count++;
                        if (y == ymin || y == ymax || x == xmin || x == xmax) {
                            closest.infinite = true;
                        }
                    }
                    //grid.put(x, y, id);
                }
            }
        }
        if (safeDist != null) {
            return safeCount;
        } else {
            int max = 0;
            Coord largest = null;
            for (Coord coord: coords.values()) {
                if (!coord.infinite && coord.count > max) {
                    largest = coord;
                    max = largest.count;
                }
            }
            if (largest != null) {
                return largest.count;
            } else {
                return 0;
            }
        }
    }

    static class Coord {
        int x, y;
        char id;
        int count;
        boolean infinite;
        public Coord(int x, int y, char id) {
            this.x = x;
            this.y = y;
            this.id = id;
            this.count = 0;
            this.infinite = false;
        }
    }

}
