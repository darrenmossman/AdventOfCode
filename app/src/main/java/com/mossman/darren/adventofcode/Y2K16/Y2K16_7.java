package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;

public class Y2K16_7 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_7 puzzle = new Y2K16_7(true);
        int i = puzzle.run();
        System.out.printf("day 7: part 1 test = %d\n", i);
        test(i, 2);

        puzzle = new Y2K16_7(false);
        i = puzzle.run();
        System.out.printf("day 7: part 1 = %d\n", i);
        test(i, 110);

        i = puzzle.run(true);
        System.out.printf("day 7: part 2 = %d\n", i);
        test(i, 242);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K16_7(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run() {
        return run(false);
    }

    private boolean isABBA(String seq) {
        //System.out.printf("%s\n", seq);
        if (seq.length() < 4) return false;
        for (int p = 0; p <= seq.length()-4; p++) {
            char w = seq.charAt(p);
            char x = seq.charAt(p+1);
            char y = seq.charAt(p+2);
            char z = seq.charAt(p+3);
            if (w == z && x == y && w != x) return true;
        }
        return false;
    }

    private void findABAs(ArrayList<String> supernet, String seq) {
        for (int p = 0; p <= seq.length()-3; p++) {
            char x = seq.charAt(p);
            char y = seq.charAt(p+1);
            char z = seq.charAt(p+2);
            if (x == z && x != y) {
                supernet.add(seq.substring(p,p+3));
            }
        }
    }

    public int run(boolean part2) {
        int r = 0;
        for (String s : input) {

            if (part2) {
                ArrayList<String> supernet = new ArrayList<>();
                ArrayList<String> hypernet = new ArrayList<>();

                int p, q = -1;
                p = s.indexOf('[', q + 1);
                while (p != -1) {
                    String sup = s.substring(q + 1, p);
                    findABAs(supernet, sup);

                    q = s.indexOf(']', p);
                    if (q == -1) {
                        p = -1;
                    } else {
                        String hyp = s.substring(p + 1, q);
                        hypernet.add(hyp);
                        p = s.indexOf('[', q);
                    }
                }
                String sup = s.substring(q + 1);
                findABAs(supernet, sup);

                boolean isABA = false;
                for (String seq : supernet) {
                    String sq = "" + seq.charAt(1) + seq.charAt(0) + seq.charAt(1);
                    for (String hyp : hypernet) {
                        if (hyp.contains(sq)) {
                            isABA = true;
                            break;
                        }
                    }
                    if (isABA) {
                        r++;
                        break;
                    }
                }
            }
            else {

                boolean hasABBA = false;
                boolean hasHABBA = false;
                int p, q = -1;
                p = s.indexOf('[', q + 1);
                while (p != -1) {
                    if (!hasABBA) {
                        String seq = s.substring(q + 1, p);
                        hasABBA = isABBA(seq);
                    }

                    q = s.indexOf(']', p);
                    if (q == -1) {
                        p = -1;
                    } else {
                        String seq = s.substring(p + 1, q);
                        hasHABBA = isABBA(seq);
                        p = s.indexOf('[', q);
                    }
                    if (hasHABBA) break;
                }
                if (!hasABBA) {
                    String seq = s.substring(q + 1);
                    hasABBA = isABBA(seq);
                }
                if (hasABBA && !hasHABBA) r++;
            }
        }
        return r;
    }
}
