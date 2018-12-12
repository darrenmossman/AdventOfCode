package com.mossman.darren.adventofcode.Y2K17;

import java.util.ArrayList;

public class Y2K17_14 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_14 puzzle = new Y2K17_14();
        int i = puzzle.run("flqrgnkx");
        test(i, 8108);

        i = puzzle.run("flqrgnkx", true);
        test(i, 1242);

        i = puzzle.run("xlqgujun");
        System.out.printf("day 14: part 1 = %d\n", i);
        //test(i, 8204);

        i = puzzle.run("xlqgujun", true);
        System.out.printf("day 14: part 2 = %d\n", i);
        //test(i, 1089);
    }

    //--------------------------------------------------------------------------------------------

    public int run(String input) {
        return run(input, false);
    }

    private static boolean makeRegion(ArrayList<char[]> squares, int c, int r, int nxt) {
        if (c < 0 || r < 0 || c >= 128 || r >= 128) return false;
        char v = squares.get(r)[c];
        if (v != '#') return false;
        squares.get(r)[c] = (char)('0' + nxt);
        makeRegion(squares, c-1, r, nxt);
        makeRegion(squares, c+1, r, nxt);
        makeRegion(squares, c, r-1, nxt);
        makeRegion(squares, c, r+1, nxt);
        return true;
    }

    public int run(String input, boolean doRegions) {

        ArrayList<char[]> squares = new ArrayList<>();
        int total = 0;

        Y2K17_10 puzzle = new Y2K17_10();
        for (int r = 0; r < 128; r++) {
            String s = input + "-" + r;
            String hash = puzzle.part2(s);
            String binary = "";
            for (int i = 0; i < hash.length(); i++) {
                String h = hash.substring(i,i+1);
                String bin = Integer.toBinaryString(Integer.parseInt(h, 16));
                while (bin.length()<4) bin = '0' + bin;
                binary += bin;
            }
            if (doRegions) {
                binary = binary.replaceAll("0",".");
                binary = binary.replaceAll("1","#");
                squares.add(binary.toCharArray());
            } else {
                String used = binary.replaceAll("0", "");
                int cnt = used.length();
                total += cnt;
            }
        }
        if (doRegions) {
            int nxt = 1;
            boolean updated;
            do {
                updated = false;
                for (int r = 0; r < 128; r++) {
                    for (int c = 0; c < 128; c++) {
                        if (makeRegion(squares, c, r, nxt)) {
                            updated = true;
                            nxt++;
                        }
                    }
                }
            } while (updated);
            return nxt-1;
        } else {
            return total;
        }
    }
}
