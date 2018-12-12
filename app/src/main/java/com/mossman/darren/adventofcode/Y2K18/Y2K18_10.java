package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_10 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_10 puzzle = new Y2K18_10(true);
        String s = puzzle.run();
        test(s,
                "#   #  ###\n" +
                "#   #   # \n" +
                "#   #   # \n" +
                "#####   # \n" +
                "#   #   # \n" +
                "#   #   # \n" +
                "#   #   # \n" +
                "#   #  ###\n");
        s = puzzle.run(true);
        test(s, 3);

        puzzle = new Y2K18_10(false);
        s = puzzle.run();
        System.out.printf("day 10: part 1 = \n%s\n", s);
        /*test(s,
                "#    #     ###  #####      ###  #    #  #####     ##    ######\n" +
                "#    #      #   #    #      #   #    #  #    #   #  #        #\n" +
                "#    #      #   #    #      #    #  #   #    #  #    #       #\n" +
                "#    #      #   #    #      #    #  #   #    #  #    #      # \n" +
                "######      #   #####       #     ##    #####   #    #     #  \n" +
                "#    #      #   #    #      #     ##    #  #    ######    #   \n" +
                "#    #      #   #    #      #    #  #   #   #   #    #   #    \n" +
                "#    #  #   #   #    #  #   #    #  #   #   #   #    #  #     \n" +
                "#    #  #   #   #    #  #   #   #    #  #    #  #    #  #     \n" +
                "#    #   ###    #####    ###    #    #  #    #  #    #  ######\n");*/
        s = puzzle.run(true);
        System.out.printf("day 10: part 2 = %s\n", s);
        //test(s, 10641);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private ArrayList<Point> points;

    private static class Point {
        int px, py, vx, vy;

        private Point(String s) {
            s = s.replaceAll("position=<", "");
            s = s.replaceAll("> velocity=<", ",");
            s = s.replaceAll(">", "");
            s = s.replaceAll(" ", "");
            String[] arr = s.split(",");
            px = Integer.parseInt(arr[0]);
            py = Integer.parseInt(arr[1]);
            vx = Integer.parseInt(arr[2]);
            vy = Integer.parseInt(arr[3]);
        }

        private void move(boolean forward) {
            if (forward) {
                px += vx;
                py += vy;
            } else {
                px -= vx;
                py -= vy;
            }
        }

    }

    private class Range {
        int minx, miny, maxx, maxy;
        int wid, hgt;
        long area;
        private Range(int minx, int miny, int maxx, int maxy) {
            this.minx = minx;
            this.miny = miny;
            this.maxx = maxx;
            this.maxy = maxy;
        }
    }

    private Range getRange() {
        Range rng = null;
        for (Point p: points) {
            if (rng == null) {
                rng = new Range(p.px, p.py, p.px, p.py);
            } else {
                if (p.px < rng.minx) rng.minx = p.px;
                if (p.py < rng.miny) rng.miny = p.py;
                if (p.px > rng.maxx) rng.maxx = p.px;
                if (p.py > rng.maxy) rng.maxy = p.py;
            }
        }
        rng.wid = Math.abs(rng.maxx-rng.minx+1);
        rng.hgt = Math.abs(rng.maxy-rng.miny+1);
        rng.area = (long)rng.wid * rng.hgt;

        return rng;
    }

    private void movePoints(boolean forward) {
        for (Point point: points) point.move(forward);
    }

    private String getString() {
        Range rng = getRange();

        char[][] grid = new char[rng.hgt][rng.wid];
        for (Point p : points) {
            grid[p.py - rng.miny][p.px - rng.minx] = '#';
        }
        String res = "";
        for (int h = 0; h < rng.hgt; h++) {
            String s = "";
            for (int w = 0; w < rng.wid; w++) {
                char ch = grid[h][w];
                s += (ch == 0) ? " " : ch;
            }
            res = res + s + "\n";
        }
        return res;
    }

    public Y2K18_10(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private void init() {
        points = new ArrayList<>(input.size());
        for (String s: input) points.add(new Point(s));
    }

    public String run() {
        return run(false);
    }

    public String run(boolean part2) {
        init();

        int cnt = 0;
        Range prior = getRange();
        while (true) {
            movePoints(true);
            Range rng = getRange();
            // If points are no longer closing we too far.
            // Possible but unlikely for points to collapse smaller than readable message
            if (rng.area > prior.area) {
                if (part2) {
                    return cnt+"";
                } else {
                    movePoints(false);
                    return getString();
                }
            }
            prior = rng;
            cnt++;
        }
    }
}













