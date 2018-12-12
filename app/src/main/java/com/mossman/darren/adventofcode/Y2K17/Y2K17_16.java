package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_16 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_16 puzzle = new Y2K17_16(new String[]{"s1", "x3/4", "pe/b"});
        String s = puzzle.run(5, 1);
        test(s, "baedc");

        s = puzzle.run(5, 2);
        test(s, "ceadb");

        puzzle = new Y2K17_16(false);
        s = puzzle.run(16, 1);
        System.out.printf("day 16: part 1 = %s\n", s);
        //test(s, "kpfonjglcibaedhm");

        s = puzzle.run(16, 1000000000L);
        System.out.printf("day 16: part 2 = %s\n", s);
        //test(s, "odiabmplhfgjcekn");
    }

    //--------------------------------------------------------------------------------------------

    private String[] strs;

    public Y2K17_16(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, ",");
        strs = str.get(0);
    }
    public Y2K17_16(String[] strs) {
        this.strs = strs;
    }

    // region day 16
    private static class Move {
        char m;
        int n;
        int p0, p1;
        char c0, c1;

        private Move(char m, int n) {
            this.m = m;
            this.n = n;
        }
        private Move(char m, int p0, int p1) {
            this.m = m;
            this.p0 = p0;
            this.p1 = p1;
        }
        private Move(char m, char c0, char c1) {
            this.m = m;
            this.n = n;
            this.c0 = c0;
            this.c1 = c1;
        }

    }

    public String run(int count, long repeat) {
        ArrayList<Character> progs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            progs.add((char)('a'+i));
        }

        ArrayList<Move> moves = new ArrayList<>(strs.length);
        for (String str : strs) {
            char m = str.charAt(0);
            String s = str.substring(1);
            String[] arr;
            switch (m) {
                case 's':
                    int n = Integer.parseInt(s);
                    moves.add(new Move(m, n));
                    break;
                case 'x':
                    arr = s.split("/");
                    int p0 = Integer.parseInt(arr[0]);
                    int p1 = Integer.parseInt(arr[1]);
                    moves.add(new Move(m, p0, p1));
                    break;
                case 'p':
                    arr = s.split("/");
                    char c0 = arr[0].charAt(0);
                    char c1 = arr[1].charAt(0);
                    moves.add(new Move(m, c0, c1));
                    break;
            }
        }

        String res = null;
        ArrayList<String> positions = new ArrayList<>();
        for (long r = 0; r < repeat; r++) {
            for (Move move: moves) {
                switch (move.m) {
                    case 's':
                        for (int i = 0; i < move.n; i++) {
                            progs.add(0, progs.remove(count - 1));
                        }
                        break;
                    case 'x':
                        char c = progs.get(move.p0);
                        progs.set(move.p0, progs.get(move.p1));
                        progs.set(move.p1, c);
                        break;
                    case 'p':
                        int p0 = progs.indexOf(move.c0);
                        int p1 = progs.indexOf(move.c1);
                        progs.set(p0, move.c1);
                        progs.set(p1, move.c0);
                        break;
                }
            }
            StringBuffer builder = new StringBuffer(count);
            for (Character c: progs) builder.append(c);
            res = builder.toString();
            if (positions.contains(res)) {
                int rr = (int)((repeat-1) % positions.size());
                res = positions.get(rr);
                break;
            } else {
                positions.add(res);
            }
        }
        return res;
    }

}
