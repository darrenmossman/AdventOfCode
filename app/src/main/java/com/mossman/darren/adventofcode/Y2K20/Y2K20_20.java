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
        System.out.printf("day 20: part 1a = %d\n", i);
        test(i, 20899048083289L);

        puzzle = new Y2K20_20(false);
        i = puzzle.part1();
        System.out.printf("day 20: part 1b = %d\n", i);
        test(i, 21599955909991L);

        i = puzzle.part2();
        System.out.printf("day 20: part 2 = %d\n", i);
        test(i, 2495);

    }

    //--------------------------------------------------------------------------------------------

    private class Match {
        int edge;
        Tile targetTile;
        int targetEdge;
        public Match(int edge, Tile targetTile, int targetEdge) {
            this.edge = edge;
            this.targetTile = targetTile;
            this.targetEdge = targetEdge;
        }
    }

    private class Tile {
        long id;
        List<Tile> tiles;
        List<String> rows = new ArrayList<>();
        List<Match> matches = new ArrayList<>();
        boolean processed;

        Tile(List<Tile> tiles) {
            this.tiles = tiles;
            if (tiles != null) {
                tiles.add(this);
            }
        }

        boolean isCorner() {
            return matches.size() == 2;
        }

        boolean noneMatch(int edge) {
            return (matches.stream().noneMatch(m -> m.edge == edge));
        }

        String columns(int c) {
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

        void rotate(int rot) {
            //System.out.printf("Rotate id: %d - %d\n", id, r);
            switch (rot) {
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

        String pattern(int edge) {
            switch (edge) {
                case 0:
                  return rows.get(0);
                case 1:
                    return columns(width()-1);
                case 2:
                    return rows.get(height()-1);
                case 3:
                    return columns(0);
                default:
                    return "";
            }
        }

        boolean match(Tile targetTile) {
            if (this == targetTile) {
                return false;
            }
            for (int edge = 0; edge < 4; edge++) {
                String pattern = pattern(edge);
                for (int targetEdge = 0; targetEdge < 4; targetEdge++) {
                    String targetPattern = targetTile.pattern(targetEdge);
                    if (pattern.equals(targetPattern) || Utils.reverse(pattern).equals(targetPattern)) {
                        matches.add(new Match(edge, targetTile, targetEdge));
                        return true;
                    }
                }
            }
            return false;
        }

        void match() {
            matches.clear();
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

            matches.stream()
                    .filter(m -> !m.targetTile.processed)
                    .forEach(m -> {
                int requiredEdge = (m.edge + 2) % 4;
                int targetEdge = m.targetEdge;

                int rot = 0;
                while (targetEdge != requiredEdge) {
                    rot++;
                    targetEdge = (targetEdge + 1) % 4;
                }
                m.targetTile.rotate(rot);

                if (!pattern(m.edge).equals(m.targetTile.pattern(targetEdge))) {
                    if (targetEdge % 2 == 1) {
                        m.targetTile.flipRows();
                    } else {
                        m.targetTile.flipColumns();
                    }
                }

                int xx = x; int yy = y;
                switch (m.edge) {
                    case 0:
                        yy--; break;
                    case 1:
                        xx++; break;
                    case 2:
                        yy++; break;
                    case 3:
                        xx--; break;
                }
                m.targetTile.process(grid, xx, yy);
            });
        }


        @Override
        public String toString() {
            String res = String.format("id = %d, matchCount = %d\n", id, matches.size());
            res = res + rows.stream().collect(Collectors.joining("\n"));
            for (int e = 0; e < 4; e++) {
                res = res + String.format("\n%d - %s", e, pattern(e));
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

        if (corner.noneMatch(1)) {
            corner.rotate(2);
        } else if (corner.noneMatch( 2)) {
            corner.rotate(1);
        }

        corner.process(grid,0,0);
        //grid.display();

        Tile monster = initMonster();

        boolean found = false;
        for (int rot = 0; rot < 4; rot++) {
            found = findMonster(grid, monster);
            if (found) {
                break;
            }
            monster.rotate(1);
        }
        if (!found) {
            monster.flipRows();
            for (int rot = 0; rot < 4; rot++) {
                found = findMonster(grid, monster);
                if (found) {
                    break;
                }
                monster.rotate(1);
            }
        }
        if (found) {
            blockMonster(grid, monster);
            //grid.display();
            return countWaves(grid);
        }
        return 0;
    }
}
