package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K16_8 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_8 puzzle = new Y2K16_8(true);
        int i = puzzle.run();
        System.out.printf("day 8: part 1 test = %d\n", i);
        test(i, 6);

        puzzle = new Y2K16_8(false);
        i = puzzle.run();
        System.out.printf("day 8: part 1 = %d\n", i);
        test(i, 123);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    ArrayList<ArrayList<Integer>> screen;


    public Y2K16_8(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run() {
        return run(false);
    }

    private void init(int x, int y) {
        screen = new ArrayList<>(y);
        for (int r = 0; r < y; r++) {
            ArrayList<Integer> row = new ArrayList<>(x);
            screen.add(row);
            for (int c = 0; c < x; c++) {
                row.add(0);
            }
        }
    }

    private void rect(int x, int y) {
        for (int r = 0; r < y; r++) {
            ArrayList<Integer> row = screen.get(r);
            for (int c = 0; c < x; c++) {
                row.set(c, 1);
            }
        }
    }

    private void rotateRow(int r, int n) {
        ArrayList<Integer> row = screen.get(r);
        for (int c = 0; c < n; c++) {
            int i = row.remove(row.size()-1);
            row.add(0, i);
        }
    }

    private void rotateCol(int c, int n) {
        ArrayList<Integer> col = new ArrayList<>(screen.size());
        for (int r = 0; r < screen.size(); r++) {
            ArrayList<Integer> row = screen.get(r);
            int i = row.get(c);
            col.add(i);
        }
        for (int r = 0; r < screen.size(); r++) {
            int rr = (r+n) % screen.size();
            ArrayList<Integer> row = screen.get(rr);
            int i = col.get(r);
            row.set(c, i);
        }
    }

    private int count() {
        int res = 0;

        for (int r = 0; r < screen.size(); r++) {
            ArrayList<Integer> row = screen.get(r);
            for (int c = 0; c < row.size(); c++) {
                int i = row.get(c);
                if (i == 1) res++;
            }
        }
        return res;
    }

    private void display() {
        for (int r = 0; r < screen.size(); r++) {
            ArrayList<Integer> row = screen.get(r);
            String s = "";
            for (int c = 0; c < row.size(); c++) {
                int i = row.get(c);
                if (i == 1) s += "#";
                else s += " ";
            }
            System.out.printf("%s\n", s);
        }
    }

    public int run(boolean part2) {
        int res = 0;

        init(50, 6);
        for (String s : input) {

            if (part2) {
            }
            else {
                if (s.startsWith("rect ")) {
                    s = s.substring("rect ".length());
                    String[] arr = s.split("x");
                    int x = Integer.parseInt(arr[0]);
                    int y = Integer.parseInt(arr[1]);
                    rect(x, y);
                }
                else if (s.startsWith("rotate row y=")) {
                    s = s.substring("rotate row y=".length());
                    String[] arr = s.split("by");
                    int r = Integer.parseInt(arr[0].trim());
                    int n = Integer.parseInt(arr[1].trim());
                    rotateRow(r, n);
                }
                else if (s.startsWith("rotate column x=")) {
                    s = s.substring("rotate column x=".length());
                    String[] arr = s.split("by");
                    int c = Integer.parseInt(arr[0].trim());
                    int n = Integer.parseInt(arr[1].trim());
                    rotateCol(c, n);
                }
            }
        }
        display();
        res = count();
        return res;
    }
}
