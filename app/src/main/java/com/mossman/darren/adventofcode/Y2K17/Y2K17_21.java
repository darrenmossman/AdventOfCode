package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_21 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_21 puzzle;

        puzzle = new Y2K17_21(true);
        int i = puzzle.run(2);
        test(i, 12);

        puzzle = new Y2K17_21(false);
        i = puzzle.run(5);
        System.out.printf("day 21: part 1 = %d\n", i);
        //test(i, 136);

        i = puzzle.run(18);
        System.out.printf("day 21: part 2 = %d\n", i);
        //test(i, 1911767);
    }

    //--------------------------------------------------------------------------------------------
    

    private ArrayList<Rule> rules;
    private char[][] pattern;
    private char[][] newPattern;

    static private char[][] strArrayToCharArrays(String[] strArr) {
        char[][] charArr = new char[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            charArr[i] = strArr[i].toCharArray();
        }
        return charArr;
    }

    static private class Rule {
        String src, tgt;
        char[][] source, target;
        ArrayList<char[][]> srcs;

        public Rule(String[] arr) {
            src = arr[0];
            tgt = arr[1];
            source = strArrayToCharArrays(src.split("/"));
            initSources();
            target = strArrayToCharArrays(tgt.split("/"));
        }

        public void initSources() {
            srcs = new ArrayList<>();
            srcs.add(source);
            char[][] s = source;
            for (int i = 0; i < 3; i++) {
                s = rotate(s);
                srcs.add(s);
            }
            s = flipX(source);
            srcs.add(s);
            for (int i = 0; i < 3; i++) {
                s = rotate(s);
                srcs.add(s);
            }
        }

        public boolean matches(char[][] square) {
            if (square.length != source.length) return false;
            for (char[][] src : srcs) {
                if (match(square, src)) return true;
            }
            return false;
        }

        private boolean match(char[][] square, char[][] src) {
            if (square.length != source.length) return false;
            int l = square.length;
            for (int r = 0; r < l; r++) {
                for (int c = 0; c < l; c++) {
                    if (src[r][c] != square[r][c]) {
                        return false;
                    }
                }
            }
            return true;
        }

        private char[][] flipX(char[][] square) {
            int l = square.length;
            char[][] res = new char[l][l];
            for (int r = 0; r < l; r++) {
                for (int c = 0; c < l; c++) {
                    res[r][l - c - 1] = square[r][c];
                }
            }
            return res;
        }

        private char[][] flipY(char[][] square) {
            int l = square.length;
            char[][] res = new char[l][l];
            for (int r = 0; r < l; r++) {
                for (int c = 0; c < l; c++) {
                    res[l - r - 1][c] = square[r][c];
                }
            }
            return res;
        }

        private char[][] rotate(char[][] square) {
            int l = square.length;
            char[][] res = new char[l][l];
            for (int r = 0; r < l; r++) {
                for (int c = 0; c < l; c++) {
                    res[c][r] = square[l - r - 1][c];
                }
            }
            return res;
        }
    }

    public Y2K17_21(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, " => ");
        rules = new ArrayList<>(str.size());
        for (String[] arr: str) {
            rules.add(new Rule(arr));
        }
    }

    public int run(int iterations)  throws IllegalStateException {
        String[] image = new String[]{".#.", "..#", "###"};
        pattern = strArrayToCharArrays(image);

        for (int iter = 0; iter < iterations; iter++) {
            char[][] sq;
            int size = pattern.length;
            int ss, ns;
            if (size % 2 == 0) {
                ss = 2; ns = 3;
            }
            else if (size % 3 == 0) {
                ss = 3; ns = 4;
            }
            else throw new IllegalStateException(String.format("size = %d", size));

            int newSize = size / ss * ns;
            newPattern = new char[newSize][newSize];

            for (int r = 0; r < size/ss; r++) {
                for (int c = 0; c < size/ss; c++) {
                    sq = getSquare(r, c, ss);
                    char [][] nsq = transform(sq);
                    putSquare(r, c, nsq);
                }
            }
            pattern = newPattern;
        }

        int cnt = 0;
        for (int r = 0; r < pattern.length; r++) {
            for (int c = 0; c < pattern.length; c++) {
                if (pattern[r][c] == '#') {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private char[][] transform(char[][] sq) {
        return transform(sq, false);
    }
    private char[][] transform(char[][] sq, boolean debug) {
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            if (rule.matches(sq)) {
                if (debug) {
                    String s = "";
                    for (int j = 0; j < sq.length; j++) {
                        s += new String(sq[j]);
                        if (j != sq.length-1) s+='/';
                    }
                    System.out.printf("[%s] (%d) %s => %s\n", s, i, rule.src, rule.tgt);
                }
                return rule.target;
            }
        }
        return null;
    }

    private char[][] getSquare(int r, int c, int ss) {
        char[][] sq = new char[ss][ss];
        int y = r*ss;
        int x = c*ss;
        for (int i = 0; i < ss; i++) {
            for (int j = 0; j < ss; j++) {
                sq[i][j] = pattern[y+i][x+j];
            }
        }
        return sq;
    }

    private void putSquare(int r, int c, char[][] sq) {
        int ss = sq.length;
        int y = r*ss;
        int x = c*ss;
        for (int i = 0; i < ss; i++) {
            for (int j = 0; j < ss; j++) {
                newPattern[y+i][x+j] = sq[i][j];
            }
        }
    }

}
