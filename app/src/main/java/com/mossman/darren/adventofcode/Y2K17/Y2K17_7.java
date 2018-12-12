package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K17_7 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_7 puzzle;

        puzzle = new Y2K17_7(true);
        String s = puzzle.part1();
        test(s, "tknk");

        int i = puzzle.part2();
        test(i, 60);

        puzzle = new Y2K17_7(false);
        s = puzzle.part1();
        System.out.printf("day 7: part 1 = %s\n", s);
        //test(s, "aapssr");

        i = puzzle.part2();
        System.out.printf("day 7: part 2 = %d\n", i);
        //test(i, 1458);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_7(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    static private class Disc {
        String id;
        int weight, tw;
        ArrayList<String> ids;
        ArrayList<Disc> discs;
        Disc par;

        public Disc(String id, int weight, String[] ids) {
            this.id = id;
            this.weight = weight;
            if (ids != null) {
                this.ids = new ArrayList<>(Arrays.asList(ids));
            } else {
                this.ids = new ArrayList<>();
            }
            discs = new ArrayList<>();
        }

        public int calculateWeights() {
            int res = weight;
            for (Disc disc: discs) {
                res += disc.calculateWeights();
            }
            tw = res;
            return res;
        }

        public Disc checkBalance() {
            HashMap<Integer, ArrayList<Disc>> weights = new HashMap<>();
            for (Disc disc : discs) {
                ArrayList<Disc> itms = weights.get(disc.tw);
                if (itms == null) {
                    itms = new ArrayList<>();
                    weights.put(disc.tw, itms);
                }
                itms.add(disc);
            }
            Disc unbalanced = null;
            if (weights.size() > 1) {
                for (ArrayList<Disc> itm : weights.values()) {
                    if (itm.size() == 1) {
                        unbalanced = itm.get(0);
                        break;
                    }
                }
            }
            if (unbalanced != null) {
                if (unbalanced.discs.size() > 0) {
                    Disc unbl = unbalanced.checkBalance();
                    if (unbl != null) return unbl;
                    else return unbalanced;
                } else {
                    return unbalanced;
                }
            } else {
                for (Disc disc : discs) {
                    unbalanced = disc.checkBalance();
                    if (unbalanced != null) break;
                }
            }
            return unbalanced;
        }
    }

    static private Disc buildTower(ArrayList<String> input) {
        ArrayList<Disc> discs = new ArrayList<>(input.size());
        Pattern p = Pattern.compile("(\\S+)\\((\\d+)\\)");
        for (String s: input) {
            s = s.replaceAll(" ", "");
            String[] parts = s.split("->");
            Matcher m = p.matcher(parts[0]);
            if (m.matches()) {
                String id = m.group(1);
                int weight = Integer.parseInt(m.group(2));
                Disc disc;
                if (parts.length > 1) {
                    String[] ids = parts[1].split(",");
                    disc = new Disc(id, weight, ids);
                } else {
                    disc = new Disc(id, weight, null);
                }
                discs.add(disc);
            }
        }
        Disc bottom = null;
        for (Disc disc: discs) {
            for (Disc par: discs) {
                if (par.ids.contains(disc.id)) {
                    par.discs.add(disc);
                    disc.par = par;
                    break;
                }
            }
            if (disc.par == null) {
                bottom = disc;
            }
        }
        return bottom;
    }

    public String part1() {
        Disc bottom = buildTower(input);
        if (bottom != null) {
            return bottom.id;
        } else {
            return null;
        }
    }

    public int part2() {
        Disc bottom = buildTower(input);
        bottom.calculateWeights();
        Disc wrong = bottom.checkBalance();
        if (wrong != null) {
            for (Disc right: wrong.par.discs) {
                if (wrong != right) {
                    int change = right.tw - wrong.tw;
                    return wrong.weight + change;
                }
            }
        }
        return 0;
    }
}
