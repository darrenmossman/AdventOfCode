package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_11 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        /* Helper function tests */
        int power;
        power = powerLevel(3, 5, 8);
        test(power, 4);
        power = powerLevel(122, 79, 57);
        test(power, -5);
        power = powerLevel(217, 196, 39);
        test(power, 0);
        power = powerLevel(101, 153, 71);
        test(power, 4);

        Y2K18_11 puzzle = new Y2K18_11();
        String s;

        s = puzzle.run(18);
        test(s, "33,45");
        s = puzzle.run(42);
        test(s, "21,61");

        s = puzzle.run(8561);
        System.out.printf("day 11: part 1 = %s\n", s);
        //test(s, "21,37");

        /* Part2 tests are fairly slow
        s = puzzle.run(18, true);
        test(s, "90,269,16");
        s = puzzle.run(42, true);
        test(s, "232,251,12");
        */

        s = puzzle.run(8561, true);
        System.out.printf("day 11: part 2 = %s\n", s);
        //test(s, "236,146,12");
    }

    //--------------------------------------------------------------------------------------------

    private static int powerLevel(int x, int y, int serialNumber) {
        int rackId = x + 10;
        int result = (rackId * y + serialNumber) * rackId;
        result %= 1000;
        result /= 100;
        result -= 5;
        return result;
    }

    public String run(int serialNumber) {
        return run(serialNumber, false);
    }

    public String run(int serialNumber, boolean part2) {

        final int gridSize = 300;

        int[][] grid = new int[gridSize][gridSize];
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                int power = powerLevel(x+1, y+1, serialNumber);
                grid[x][y] = power;
            }
        }
        int largest = 0;
        String result = "";

        int minSize, maxSize;
        if (part2) {
            minSize = 1; maxSize = 300;
        } else {
            minSize = maxSize = 3;
        }

        int xx = 0, yy = 0, ss = 0;
        for (int squareSize = minSize; squareSize <= maxSize; squareSize++) {
            for (int y = 0; y < gridSize - squareSize; y++) {
                int prior = 0;

                for (int x = 0; x < gridSize - squareSize; x++) {
                    int totalPower = 0;
                    if (x > 0) {
                        // use prior total and just add next row and sub prior row
                        // not fastest method, but fast enough without making code too complicated
                        totalPower = prior;
                        for (int r = 0; r < squareSize; r++) {
                            int power = grid[x + squareSize-1][y + r];
                            totalPower += power;
                            power = grid[x - 1][y + r];
                            totalPower -= power;
                        }
                    } else {
                        // calculate full square once per row
                        for (int c = 0; c < squareSize; c++) {
                            for (int r = 0; r < squareSize; r++) {
                                int power = grid[x + c][y + r];
                                totalPower += power;
                            }
                        }
                    }
                    prior = totalPower;

                    if (totalPower > largest) {
                        largest = totalPower;
                        xx = x; yy = y; ss = squareSize;
                    }
                }
            }
        }
        if (part2) {
            result = String.format("%d,%d,%d", xx + 1, yy + 1, ss);
        } else {
            result = String.format("%d,%d", xx + 1, yy + 1);
        }

        return result;
    }
}













