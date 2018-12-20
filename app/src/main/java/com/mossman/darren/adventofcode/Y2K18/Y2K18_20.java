package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_20 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_20 puzzle;
        int i;

        puzzle = new Y2K18_20("^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$");
        i = puzzle.part1();
        test(i, 23);

        puzzle = new Y2K18_20("^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$");
        i = puzzle.part1();
        test(i, 31);

        puzzle = new Y2K18_20(false);
        i = puzzle.part1();
        System.out.printf("day 20: part 1 = %d\n", i);
        //test(i, 4108);

        i = puzzle.part2();
        System.out.printf("day 20: part 2 = %d\n", i);
        //test(i, 8366);
    }

    //--------------------------------------------------------------------------------------------

    private String input;
    private InfiniteGrid<Character> grid;
    private InfiniteGrid<Integer> count;
    private int x, y;
    private int doorCount, maxCount;


    public Y2K18_20(boolean test) {
        ArrayList<String> strings = Utils.readFile(getFilename(test));
        input = strings.get(0);
    }
    public Y2K18_20(String s) {
        input = s;
    }

    private void outputGrid() {

        for (int y = grid.miny; y <= grid.maxy; y++) {
            String s = "";
            for (int x = grid.minx; x <= grid.maxx; x++) {
                s += grid.get(x, y);
            }
            System.out.println(s);
        }
        System.out.println();
    }

    private void fillWalls() {
        for (int y = grid.miny; y <= grid.maxy; y++) {
            for (int x = grid.minx; x <= grid.maxx; x++) {
                char c = grid.get(x, y);
                if (c == '?') grid.put(x,y, '#');
            }
        }
    }

    private void makeRoom(int x, int y) {
        makeRoom(x, y, '.');
    }
    private void makeRoom(int x, int y, char c) {
        grid.put(x, y, c);
        grid.put(x-1, y-1, '#');
        grid.put(x+1, y-1, '#');
        grid.put(x-1, y+1, '#');
        grid.put(x+1, y+1, '#');
        if (grid.get(x-1, y) == ' ') grid.put(x-1, y, '?');
        if (grid.get(x+1, y) == ' ') grid.put(x+1, y, '?');
        if (grid.get(x, y-1) == ' ') grid.put(x, y-1, '?');
        if (grid.get(x, y+1) == ' ') grid.put(x, y+1, '?');

        Integer cnt = count.get(x, y);
        if (cnt == null || doorCount < cnt) {
            count.put(x, y, doorCount);
            if (doorCount > maxCount) {
                maxCount = doorCount;
            }
        }
        doorCount++;
    }


    private int branch(int pos) {

        char c = input.charAt(pos);
        int xx = x, yy = y, dc = doorCount;
        while (c != '$') {

            switch (c) {
                case 'N':
                    grid.put(x, y-1, '-');
                    y -=2;
                    makeRoom(x, y);
                    break;
                case 'S':
                    grid.put(x, y+1, '-');
                    y +=2;
                    makeRoom(x, y);
                    break;
                case 'E':
                    grid.put(x+1, y, '|');
                    x +=2;
                    makeRoom(x, y);
                    break;
                case 'W':
                    grid.put(x-1, y, '|');
                    x -=2;
                    makeRoom(x, y);
                    break;
                case '(':
                    pos = branch(pos+1);
                    break;
                case ')':
                    return pos;
                case '|':
                    x = xx; y = yy; doorCount = dc;
                default:
                    break;
            }
            pos++;
            c = input.charAt(pos);
        }
        return pos;
    }

    public int part1() {
        return part1(false);
    }
    public int part1(boolean part2) {

        grid = new InfiniteGrid<>(' ');
        count = new InfiniteGrid<>();
        x = 0; y = 0; doorCount = 0; maxCount = 0;
        makeRoom(x, y, 'X');

        int pos = 1;
        branch(pos);

        if (!part2) {
            fillWalls();
            outputGrid();
        }

        return maxCount;
    }

    public int part2() {
        part1(true);

        int res = 0;
        for (int y = count.miny; y <= count.maxy; y++) {
            for (int x = count.minx; x <= count.maxx; x++) {
                Integer i = count.get(x, y);
                if (i != null && i >= 1000) {
                    res++;
                }
            }
        }
        return res;
    }

}
