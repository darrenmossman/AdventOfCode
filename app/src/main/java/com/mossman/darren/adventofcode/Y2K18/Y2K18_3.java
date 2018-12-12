package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K18_3 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_3 puzzle = new Y2K18_3(false);
        int i = puzzle.part1();
        System.out.printf("day 3: part 1 = %d\n", i);
        //test(i, 107663);

        i = puzzle.part2();
        System.out.printf("day 3: part 2 = %d\n", i);
        //test(i, 1166);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K18_3(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private static class Claim {
        private int id, l, t, w, h;
        private static final Pattern p = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

        public Claim(String s) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                id = Integer.parseInt(m.group(1));
                l = Integer.parseInt(m.group(2));
                t = Integer.parseInt(m.group(3));
                w = Integer.parseInt(m.group(4));
                h = Integer.parseInt(m.group(5));
            }
        }
    }

    public int part1() {
        HashMap<Integer, HashMap<Integer, Integer>> fabric = new HashMap<>();

        fabric = new HashMap<>();
        for (String s: input) {
            Claim claim = new Claim(s);
            for (int y = claim.t; y < claim.t+claim.h; y++) {
                HashMap<Integer, Integer> row = fabric.get(y);
                if (row == null) {
                    row = new HashMap<>();
                    fabric.put(y, row);
                }
                for (int x = claim.l; x < claim.l+claim.w; x++) {
                    Integer val = row.get(x);
                    row.put(x, val == null ? 1 : val+1);
                }
            }
        }
        int cnt = 0;
        for (HashMap<Integer, Integer> row: fabric.values()) {
            for (Integer val: row.values()) {
                if (val > 1) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public int part2() {
        HashMap<Integer, HashMap<Integer, ArrayList<Claim>>> fabric = new HashMap<>();
        ArrayList<Claim> allClaims = new ArrayList<>(input.size());

        for (String s: input) {
            Claim claim = new Claim(s);
            allClaims.add(claim);
            for (int y = claim.t; y < claim.t+claim.h; y++) {
                HashMap<Integer, ArrayList<Claim>> row = fabric.get(y);
                if (row == null) {
                    row = new HashMap<>();
                    fabric.put(y, row);
                }
                for (int x = claim.l; x < claim.l+claim.w; x++) {
                    ArrayList<Claim> claims = row.get(x);
                    if (claims == null) {
                        claims = new ArrayList<>();
                        row.put(x, claims);
                    }
                    claims.add(claim);
                }
            }
        }
        for (HashMap<Integer, ArrayList<Claim>> row: fabric.values()) {
            for (ArrayList<Claim> claims: row.values()) {
                if (claims.size() > 1) {
                    for (Claim claim: claims) {
                        allClaims.remove(claim);
                    }
                }
            }
        }
        if (allClaims.size() == 1) {
            return allClaims.get(0).id;
        }
        else {
            return 0;
        }
    }

}
