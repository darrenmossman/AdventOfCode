package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.InfiniteGrid;

public class Y2K17_3 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_3 puzzle = new Y2K17_3();
        int i = puzzle.part1(277678);
        System.out.printf("day 3: part 1 = %d\n", i);
        //test(i, 475);

        i = puzzle.part2(277678);
        System.out.printf("day 3: part 2 = %d\n", i);
        //test(i, 279138);
    }

    //--------------------------------------------------------------------------------------------

    public Y2K17_3() {
    }
    enum Direction {right, up, left, down};

    public int part1(int val) {
        Direction dir = Direction.right;
        int x = 0, y = 0;
        int maxx = 0, minx = 0, maxy = 0, miny = 0;
        int v = 1;
        while (v < val) {
            switch (dir) {
                case right:
                    x++;
                    if (x > maxx) {
                        maxx++;
                        dir = Direction.up;
                    }
                    break;
                case up:
                    y--;
                    if (y < miny) {
                        miny--;
                        dir = Direction.left;
                    }
                    break;
                case left:
                    x--;
                    if (x < minx) {
                        minx--;
                        dir = Direction.down;
                    }
                    break;
                case down:
                    y++;
                    if (y > maxy) {
                        maxy++;
                        dir = Direction.right;
                    }
                    break;
            }
            v++;
        }
        int steps = x + y;
        return steps;
    }

    private static int getSum(InfiniteGrid<Integer> spiral, int x, int y) {
        int sum = 0;
        for (int xx = x-1; xx <= x+1; xx++) {
            for (int yy = y-1; yy <= y+1; yy++) {
                if (xx == x && yy == y) continue;
                sum += spiral.get(xx, yy);
            }
        }
        return sum;
    }

    public int part2(int val) {
        InfiniteGrid<Integer> grid = new InfiniteGrid<>(0);
        grid.put(0, 0, 1);

        Direction dir = Direction.right;
        int x = 0, y = 0;
        int maxx = 0, minx = 0, maxy = 0, miny = 0;
        int v = 1;

        int sum = 1;
        while (sum < val) {
            switch (dir) {
                case right:
                    x++;
                    if (x > maxx) {
                        maxx++;
                        dir = Direction.up;
                    }
                    break;
                case up:
                    y--;
                    if (y < miny) {
                        miny--;
                        dir = Direction.left;
                    }
                    break;
                case left:
                    x--;
                    if (x < minx) {
                        minx--;
                        dir = Direction.down;
                    }
                    break;
                case down:
                    y++;
                    if (y > maxy) {
                        maxy++;
                        dir = Direction.right;
                    }
                    break;
            }
            sum = getSum(grid, x, y);
            grid.put(x, y, sum);

            v++;
        }
        return sum;
    }

}
