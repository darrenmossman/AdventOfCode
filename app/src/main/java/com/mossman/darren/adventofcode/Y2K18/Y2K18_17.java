package com.mossman.darren.adventofcode.Y2K18;

import android.util.Pair;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.MovingObject;
import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mossman.darren.adventofcode.MovingObject.Direction.down;
import static com.mossman.darren.adventofcode.MovingObject.Direction.left;
import static com.mossman.darren.adventofcode.MovingObject.Direction.right;

public class Y2K18_17 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_17 puzzle;
        int i;

        puzzle = new Y2K18_17(true);
        puzzle.run();
        test(puzzle.total, 57);
        test(puzzle.totalRetained, 29);

        puzzle = new Y2K18_17(false);
        puzzle.run();

        System.out.printf("day 17: part 1 = %d\n", puzzle.total);
        test(puzzle.total, 35707);

        System.out.printf("day 17: part 2 = %d\n", puzzle.totalRetained);
        test(puzzle.totalRetained, 29293);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<Clay> clays;
    private InfiniteGrid<Character> grid;
    private int total, totalRetained;
    private InfiniteGrid<Boolean> leftNotRight;

    private class Clay {
        char c0;
        int v;
        char c1;
        int r0, r1;

        private Clay(char c0, int v, char c1, int r0, int r1) {
            this.c0 = c0;
            this.v = v;
            this.c1 = c1;
            this.r0 = r0;
            this.r1 = r1;
            if (r0 > r1) {
                throw new IllegalStateException(toString() + " is not valid");
            }
        }

        private void applyToGrid() {
            if (c0 == 'x' && c1 == 'y') {
                for (int y = r0; y <= r1; y++) {
                    grid.put(v, y, '#');
                }
            }
            else if (c0 == 'y' && c1 == 'x') {
                for (int x = r0; x <= r1; x++) {
                    grid.put(x, v, '#');
                }
            }
            else {
                throw new IllegalStateException(toString() + " is not valid");
            }
        }
    }
    final static char empty = ' ';

    private class Water extends MovingObject {
        private int blocked = 0;
        private boolean changed = false;

        private Water(int x, int y) {
            super(x, y, down);
        }

        private char below() {
            return grid.get(x,y+1);
        }
        private char toLeft() {
            return grid.get(x-1,y);
        }
        private char toRight() {
            return grid.get(x+1,y);
        }

        private void set(char c) {
            if (y >= grid.miny && y <= grid.maxy) {
                char prior = grid.get(x,y);
                if (prior == empty) {
                    total++;
                }
                if (prior != c) {
                    grid.put(x, y, c);
                    changed = true;
                    if (c == '~') {
                        totalRetained++;
                    }
                }
            }
        }

        public void advance() {
            set('|');
            super.advance();
        }

        private void flow(int i) {
            boolean flowing = true;
            while (flowing) {
                switch (dir) {
                    case down:
                        blocked = 0;
                        if (below() == empty || below() == '|') {
                            advance();
                        } else if (below() == '#' || below() == '~') {
                            if (leftNotRight.get(x, y)) {
                                leftNotRight.put(x, y, false);
                                if (toLeft() == empty || toLeft() == '|') {
                                    dir = left;
                                } else {
                                    dir = right;
                                }
                            } else {
                                leftNotRight.put(x, y, true);
                                if (toRight() == empty || toRight() == '|') {
                                    dir = right;
                                } else {
                                    dir = left;
                                }
                            }
                        }
                        break;
                    case left:
                        if (below() == empty || below() == '|') {
                            dir = down;
                        } else if (toLeft() == empty || toLeft() == '|') {
                            advance();
                        } else if (toLeft() == '#' || toLeft() == '~') {
                            if (blocked == 2) {
                                set('~');
                                flowing = false;
                            } else {
                                dir = right;
                                blocked++;
                            }
                        }
                        break;
                    case right:
                        if (below() == empty || below() == '|') {
                            dir = down;
                        } else if (toRight() == empty || toRight() == '|') {
                            advance();
                        } else if (toRight() == '#' || toRight() == '~') {
                            if (blocked == 2) {
                                set('~');
                                flowing = false;
                            } else {
                                dir = left;
                                blocked++;
                            }
                        }
                        break;
                }
                if (y > grid.maxy) {
                    flowing = false;
                }
                if (x < grid.minx-1 || x > grid.maxx+1) {
                    throw new IllegalStateException(String.format("%d - %d, %d", i, x, y));
                }
            }
        }
    }

    public Y2K18_17(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, ", ");
        clays = new ArrayList<>(str.size());

        for (String[] arr: str) {
            String s = arr[0];
            char c0 = s.charAt(0);
            int v = Integer.parseInt(s.substring(2));

            s = arr[1];
            char c1 = s.charAt(0);
            String[] rng = s.substring(2).split("\\.\\.");
            int r0 = Integer.parseInt(rng[0]);
            int r1 = Integer.parseInt(rng[1]);

            Clay clay = new Clay(c0, v, c1, r0, r1);
            clays.add(clay);
        }
    }

    private void outputGrid(boolean includeHeader) {

        if (includeHeader) {
            int mod = 1000;
            while (mod >= 10) {
                String header = "     ";
                for (int x = grid.minx - 1; x <= grid.maxx + 1; x++) {
                    header += (x % mod) / (mod / 10);
                }
                System.out.println(header);
                mod /= 10;
            }
        }

        for (int y = grid.miny; y <= grid.maxy; y++) {
            String s = String.format("%4d ", y);
            for (int x = grid.minx-1; x <= grid.maxx+1; x++) {
                s += grid.get(x, y);
            }
            System.out.println(s);
        }
        System.out.println();
    }

    public void run() {
        grid = new InfiniteGrid<>(empty);
        grid.put(500, 0, '+');
        grid.miny = null;       // reset so doesn't '+' doesnt contribute to miny

        for (Clay clay: clays) {
            clay.applyToGrid();
        }
        // probably overkill, but easy way to flip left then right at each location
        leftNotRight = new InfiniteGrid<>(true);

        total = 0; totalRetained = 0;
        int i = 0;
        int cnt = 0;
        while (true) {
            i++;
            Water water = new Water(500, 1);
            water.flow(i);
            if (water.changed){
                cnt = 0;
            } else {
                // if no change occurs on more than 2 iterations, we're done
                cnt++;
                if (cnt > 2) {
                    break;
                }
            };
        }
        //outputGrid(true);
        //System.out.printf("%d = %d, %d (%d)\n", i, total, totalRetained, cnt);
    }
}
