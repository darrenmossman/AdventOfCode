package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Y2K18_18 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_18 puzzle;
        int i;

        puzzle = new Y2K18_18(true);
        i = puzzle.run(10);
        test(i, 1147);

        puzzle = new Y2K18_18(false);
        i = puzzle.run(10);
        System.out.printf("day 18: part 1 = %d\n", i);
        test(i, 605154);

        i = puzzle.run(1000000000, true);
        System.out.printf("day 18: part 2 = %d\n", i);
        test(i, 200364);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private char[][]grid;

    public Y2K18_18(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run(int minutes) {
        return run(minutes, false);
    }

    private String getAdjacent(int y, int x) {
        String res = "";
        for (int yy = y-1; yy <= y+1; yy++) {
            if (yy < 0 || yy >= grid.length) continue;
            for (int xx = x-1; xx <= x+1; xx++) {
                if (yy == y && xx == x) continue;
                if (xx < 0 || xx >= grid[yy].length) continue;
                res += grid[yy][xx];
            }
        }
        return res;
    }

    private String gridToString() {
        StringBuilder builder = new StringBuilder();
        for (char[] arr: grid) {
            builder.append(arr);
        }
        return builder.toString();
    }

    private int getResult(String s) {
        String trees = s.replaceAll("[^|]", "");
        String lumber = s.replaceAll("[^#]", "");
        return trees.length() * lumber.length();
    }

    public int run(int minutes, boolean part2) {
        grid = new char[input.size()][];
        for (int i = 0 ; i < input.size(); i++) {
            grid[i] = input.get(i).toCharArray();
        }
        ArrayList<String> history = new ArrayList<>();
        for (int m = 0; m < minutes; m++) {
            char[][] newGrid = new char[grid.length][grid[0].length];

            boolean changed = false;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    char c = grid[y][x];
                    String adjacant = getAdjacent(y, x);
                    String trees = adjacant.replaceAll("[^|]", "");
                    String lumber = adjacant.replaceAll("[^#]", "");
                    switch (c) {
                        case '.':
                            if (trees.length() >= 3) {
                                changed = true;
                                c = '|';
                            }
                            break;
                        case '|':
                            if (lumber.length() >= 3) {
                                changed = true;
                                c = '#';
                            }
                            break;
                        case '#':
                            if (lumber.length() == 0 || trees.length() == 0) {
                                changed = true;
                                c = '.';
                            }
                            break;
                    }
                    newGrid[y][x] = c;
                }
            }
            if (part2 && !changed) break;
            grid = newGrid;

            if (part2) {
                String s = gridToString();
                if (history.contains(s)) {
                    int i = history.indexOf(s);
                    int index = (minutes-1-m) % (m-i) + i;
                    s = history.get(index);
                    int res = getResult(s);
                    return res;
                } else {
                    history.add(s);
                }
            }
        }
        String s = gridToString();
        int res = getResult(s);
        return res;
    }
}













