package com.mossman.darren.adventofcode.Y2K20;

import com.mossman.darren.adventofcode.InfiniteGrid;
import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Y2K20_20 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K20_20 puzzle = new Y2K20_20(true);
        long i = puzzle.part1();
        System.out.printf("day 1: part 1a = %d\n", i);
        test(i, 20899048083289L);

        puzzle = new Y2K20_20(false);
        i = puzzle.part1();
        System.out.printf("day 1: part 1b = %d\n", i);
        test(i, 21599955909991L);

        i = puzzle.part2();
        System.out.printf("day 1: part 2 = %d\n", i);
        test(i, 2495);

    }

    //--------------------------------------------------------------------------------------------
    private class Match {
        Tile tile;
        int edge;
        public Match(Tile tile, int edge) {
            this.tile = tile;
            this.edge = edge;
        }
    }

    private class Tile {
        List<Tile> tiles;
        long id;
        List<String> rows = new ArrayList<>();
        Match[] matches = new Match[4];
        int matchCount = 0;
        boolean processed;

        Tile(List<Tile> tiles) {
            this.tiles = tiles;
            if (tiles != null) {
                tiles.add(this);
            }
        }

        boolean isCorner() {
            return matchCount == 2;
        }

        String cols(int c) {
            StringBuilder builder = new StringBuilder();
            for (String r: rows) {
                builder.append(r.charAt(c));
            }
            return builder.toString();
        }

        void flipRows() {
            //System.out.printf("Flip Rows id: %d\n", id);
            for (int i = 0; i < (height()+1) / 2; i++) {
                int j = height()-i-1;
                String s1 = rows.get(i);
                String s2 = rows.get(j);
                rows.set(i, s2);
                rows.set(j, s1);
            }
        }

        void flipColumns() {
            //System.out.printf("Flip Columns id: %d\n", id);
            for (int r = 0; r < height(); r++) {
                StringBuilder builder = new StringBuilder();
                String s = rows.get(r);
                for (int c = 0; c < width(); c++) {
                    builder.insert(0, s.charAt(c));
                }
                rows.set(r, builder.toString());
            }
        }

        void rotate(int r) {
            //System.out.printf("Rotate id: %d - %d\n", id, r);
            switch (r) {
                case 1:
                    List<String> newRows = new ArrayList<>();
                    for (int i = 0; i < width(); i++) {
                        StringBuilder builder = new StringBuilder();
                        for (int j = 0; j < height(); j++) {
                            String s = rows.get(height()-j-1);
                            builder.append(s.charAt(i));
                        }
                        newRows.add(builder.toString());
                    }
                    rows = newRows;
                    break;
                case 2:
                    for (int i = 0; i <= (height()-1) / 2; i++) {
                        int j = height()-i-1;
                        String s1 = Utils.reverse(rows.get(i));
                        String s2 = Utils.reverse(rows.get(j));
                        rows.set(i, s2);
                        rows.set(j, s1);
                    }
                    break;
                case 3:
                    rotate(2);
                    rotate(1);
                    break;
            }
        }

        int height() {
            return rows.size();
        }

        int width() {
            return rows.get(0).length();
        }

        String edge(int e) {
            switch (e) {
                case 0:
                  return rows.get(0);
                case 1:
                    return cols(width()-1);
                case 2:
                    return rows.get(height()-1);
                case 3:
                    return cols(0);
                default:
                    return "";
            }
        }

        boolean matches(Tile tile) {
            if (this == tile) {
                return false;
            }
            for (int e = 0; e < 4; e++) {
                String s1 = edge(e);
                for (int f = 0; f < 4; f++) {
                    String s2 = tile.edge(f);
                    if (s1.equals(s2) || Utils.reverse(s1).equals(s2)) {
                        matches[e] = new Match(tile, f);
                        return true;
                    }
                }
            }
            return false;
        }

        void match(Tile tile) {
            if (matches(tile)) {
                matchCount++;
            }
        }

        void match() {
            matchCount = 0;
            matches = new Match[4];
            tiles.forEach(this::match);
        }

        void place(InfiniteGrid<Character> grid, int x, int y) {
            int xx = x * (width()-2);
            int yy = y * (height()-2);

            for (int r = 1; r < height()-1; r++) {
                String s = rows.get(r);
                for (int c = 1; c < width()-1; c++) {
                    grid.put(xx+c-1, yy+r-1, s.charAt(c));
                }
            }
        }

        void process(InfiniteGrid<Character> grid, int x, int y) {
            processed = true;
            place(grid, x,y);

            match();
            for (int p = 0; p < 4; p++) {
                int q = (p + 2) % 4;
                Match m = matches[p];
                if (m == null || m.tile.processed) {
                    continue;
                }
                int e = m.edge;

                int r = 0;
                while (e != q) {
                    r++;
                    e = (e + 1) % 4;
                }
                m.tile.rotate(r);

                if (!edge(p).equals(m.tile.edge(q))) {
                    if (q % 2 == 1) {
                        m.tile.flipRows();
                    } else {
                        m.tile.flipColumns();
                    }
                }

                int xx = x; int yy = y;
                switch (p) {
                    case 0:
                        yy--; break;
                    case 1:
                        xx++; break;
                    case 2:
                        yy++; break;
                    case 3:
                        xx--; break;
                }
                //System.out.printf("%d,%d", xx, yy);
                m.tile.process(grid, xx, yy);
            }
        }


        @Override
        public String toString() {
            String res = String.format("id = %d, matchCount = %d\n", id, matchCount);
            res = res + rows.stream().collect(Collectors.joining("\n"));
            for (int e = 0; e < 4; e++) {
                res = res + String.format("\n%d - %s", e, edge(e));
            }
            return res;
        }

        int read(ArrayList<String> input, int r) {
            Pattern pId = Pattern.compile("Tile (\\d+):");
            String row = input.get(r);
            Matcher matcher = pId.matcher(row);
            if (matcher.matches()) {
                id = Integer.parseInt(matcher.group(1));
            } else {
                r--;
            }
            while (r < input.size()-1) {
                r++;
                row = input.get(r);
                if (!row.isEmpty()) {
                    rows.add(row);
                    //System.out.println(row);
                } else {
                    r++;
                    break;
                }
            }
            return r;
        }
    }

    private ArrayList<String> input;

    public Y2K20_20(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private List<Tile> initTiles() {
        List<Tile> tiles = new ArrayList<>();
        int r = 0;
        while (r < input.size()-1) {
            Tile tile = new Tile(tiles);
            r = tile.read(input, r);
        }

        tiles.forEach(Tile::match);

        return tiles;
    }

    public long part1() {
        List<Tile> tiles = initTiles();

        return tiles.stream()
                .filter(Tile::isCorner)
                .map(t -> t.id)
                .reduce(1L, (a,b) -> a*b);
    }

    private Tile findCorner(List<Tile> tiles) {
        return tiles.stream()
                .filter(Tile::isCorner)
                .findFirst().orElse(null);
    }

    private void testTransformations(List<Tile> tiles) {

        tiles.forEach(tile -> {
            String s = tile.toString();
            for (int i = 0; i < 4; i++) {
                tile.rotate(1);
            }
            assert s.equals(tile.toString());

            for (int i = 0; i < 2; i++) {
                tile.rotate(2);
            }
            assert s.equals(tile.toString());

            for (int i = 0; i < 2; i++) {
                tile.flipRows();
            }
            assert s.equals(tile.toString());

            for (int i = 0; i < 2; i++) {
                tile.flipColumns();
            }
            assert s.equals(tile.toString());
        });
    }

    private void display(InfiniteGrid<Character> grid) {
        for (int r = 0; r < grid.size(); r++) {
            HashMap<Integer, Character> row = grid.getRow(r);
            String s = "";
            for (int c = 0; c < row.size(); c++) {
                s += row.get(c);
            }
            System.out.printf("%s\n", s);
        }
    }

    private Tile initMonster() {
        String filename = getFilename(true);
        filename = filename.replace("_test", "_monster");
        ArrayList<String> monsterInput = Utils.readFile(filename);

        Tile monster = new Tile(null);
        monster.read(monsterInput, 0);
        return monster;
    }

    private boolean findMonsterAt(InfiniteGrid<Character> grid, Tile monster, int x, int y) {
        for (int r = 0; r < monster.height(); r++) {
            String s = monster.rows.get(r);
            int yy = y + r;
            for (int c = 0; c < s.length(); c++) {
                if (s.charAt(c) == '#') {
                    int xx = x + c;
                    Character ch = grid.get(xx, yy);
                    if (ch != '#') return false;
                }
            }
        }
        return true;
    }

    private boolean findMonster(InfiniteGrid<Character> grid, Tile monster) {
        for (int r = 0; r < grid.size()-monster.height()+1; r++) {
            HashMap<Integer, Character> row = grid.getRow(r);
            for (int c = 0; c < row.size()-monster.width()+1; c++) {
                if (findMonsterAt(grid, monster, c, r)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void blockMonsterAt(InfiniteGrid<Character> grid, Tile monster, int x, int y) {
        for (int r = 0; r < monster.height(); r++) {
            String s = monster.rows.get(r);
            int yy = y + r;
            for (int c = 0; c < s.length(); c++) {
                if (s.charAt(c) == '#') {
                    grid.put(x+c, y+r, 'O');
                }
            }
        }
    }

    private void blockMonster(InfiniteGrid<Character> grid, Tile monster) {
        for (int r = 0; r < grid.size()-monster.height()+1; r++) {
            HashMap<Integer, Character> row = grid.getRow(r);
            for (int c = 0; c < row.size()-monster.width()+1; c++) {
                if (findMonsterAt(grid, monster, c, r)) {
                    blockMonsterAt(grid, monster, c, r);
                }
            }
        }
    }

    private int countWaves(InfiniteGrid<Character> grid) {
        int res = 0;
        for (int r = 0; r < grid.size(); r++) {
            HashMap<Integer, Character> row = grid.getRow(r);
            for (int c = 0; c < row.size(); c++) {
                if (row.get(c) == '#') {
                    res++;
                }
            }
        }
        return res;
    }

    public long part2() {
        List<Tile> tiles = initTiles();

        InfiniteGrid<Character> grid = new InfiniteGrid<>();

        //testTransformations(tiles);

        Tile corner = findCorner(tiles);

        if (corner.matches[1] == null) {
            corner.rotate(2);
        } else if (corner.matches[2] == null) {
            corner.rotate(1);
        }

        corner.process(grid,0,0);
        //display(grid);

        Tile monster = initMonster();
        boolean found = false;
        for (int r = 0; r < 4; r++) {
            found = findMonster(grid, monster);
            if (found) {
                break;
            }
            monster.rotate(1);
        }
        if (!found) {
            monster.flipRows();
            for (int r = 0; r < 4; r++) {
                found = findMonster(grid, monster);
                if (found) {
                    break;
                }
                monster.rotate(1);
            }
        }
        if (found) {
            blockMonster(grid, monster);
            //display(grid);
            return countWaves(grid);
        }
        return 0;
    }
}
