package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_25 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_25 puzzle;
        int i;

        puzzle = new Y2K18_25(true);
        i = puzzle.run();
        test(i, 2);

        puzzle = new Y2K18_25(true, 1);
        i = puzzle.run();
        test(i, 4);

        puzzle = new Y2K18_25(true, 2);
        i = puzzle.run();
        test(i, 3);

        puzzle = new Y2K18_25(true, 3);
        i = puzzle.run();
        test(i, 8);

        puzzle = new Y2K18_25(false);
        i = puzzle.run();
        System.out.printf("day 25: part 1 = %d\n", i);
        //test(i, 310);

    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<Point> points;


    public Y2K18_25(boolean test) {
        this(test, 0);
    }

    public Y2K18_25(boolean test, int t) {
        String filename = getFilename(test);
        if (t > 0) {
            filename = filename.replace(".txt", "_"+t+".txt");
        }
        ArrayList<String> input = Utils.readFile(filename);
        points = new ArrayList<>(input.size());
        for (String s: input) {
            points.add(new Point(s));
        }
    }

    private class Point {
        private int x,y,z,t;
        private ArrayList<Point> points = new ArrayList<>();
        private ArrayList<Point> constellation = null;

        private Point(String s) {
            String[] arr = s.split(",");
            x = Integer.parseInt(arr[0].trim());
            y = Integer.parseInt(arr[1].trim());
            z = Integer.parseInt(arr[2].trim());
            t = Integer.parseInt(arr[3].trim());
        }

        private int distance(int x, int y, int z, int t) {
            return Math.abs(x-this.x) + Math.abs(y-this.y) + Math.abs(z-this.z) + Math.abs(t-this.t);
        }

        private int distance(Point p) {
            return distance(p.x, p.y, p.z, p.t);
        }
    }

    private void addPoints(Point point, ArrayList<Point> constellation) {
        if (constellation.contains(point)) {
            return;
        }
        constellation.add(point);
        point.constellation = constellation;
        for (Point p: point.points) {
            addPoints(p, constellation);
        }
    }

    public int run() {

        for (Point point: points) {
            for (Point p : points) {
                if (p == point) continue;
                if (point.points.contains(p)) continue;
                int d = p.distance(point);
                if (d <= 3) {
                    point.points.add(p);
                    p.points.add(point);
                }
            }
        }

        ArrayList<ArrayList<Point>> constellations = new ArrayList<>();

        for (Point point: points) {
            if (point.constellation == null) {
                ArrayList<Point> constellation = new ArrayList<>();
                constellations.add(constellation);
                addPoints(point, constellation);
            }
        }

        return constellations.size();
    }
}
