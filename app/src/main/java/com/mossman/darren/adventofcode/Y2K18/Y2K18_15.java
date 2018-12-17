package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.mossman.darren.adventofcode.Y2K18.Y2K18_15.Type.Elf;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_15.Type.Goblin;

public class Y2K18_15 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        int i;
        Y2K18_15 puzzle;
        Date dt1, dt0 = new Date();

        puzzle = new Y2K18_15(true);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 27730);

        puzzle = new Y2K18_15(true, 1);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 36334);

        puzzle = new Y2K18_15(true, 2);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 39514);

        puzzle = new Y2K18_15(true, 3);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 27755);

        puzzle = new Y2K18_15(true, 4);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 28944);

        puzzle = new Y2K18_15(true, 5);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 18740);

        puzzle = new Y2K18_15(false, 1);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 245280);

        puzzle = new Y2K18_15(false, 0);
        i = puzzle.run();
        dt1 = new Date();
        System.out.printf("%d seconds\n", (dt1.getTime() - dt0.getTime()) / 1000); dt0 = dt1;
        test(i, 243390);

        //i = puzzle.run(true);
        //System.out.printf("day 15: part 2 = %d\n", i);
        //test(i, 0);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private char[][] map;

    public Y2K18_15(boolean test) {
        this(test, 0);
    }

    public Y2K18_15(boolean test, int t) {
        String filename = getFilename(test);
        if (t > 0) {
            filename = filename.replace(".txt", "_"+t+".txt");
        }
        input = Utils.readFile(filename);
    }

    public Integer run() {
        return run(false);
    }

    public enum Type {Elf, Goblin};

    private class Unit implements Comparable<Unit>{
        Type type;
        int x, y;
        int attackPower = 3;
        int hitPoints = 200;
        Unit attackTarget;
        Node shortestPath;
        boolean isDead;

        private Unit(char type, int x, int y) {
            this.type = type == 'E' ? Elf : Goblin;
            this.x = x;
            this.y = y;
        }

        public int compareTo(Unit other) {
            if (y == other.y) {
                return Integer.compare(x, other.x);
            } else {
                return Integer.compare(y, other.y);
            }
        }

        private char typeChar() {
            return type==Elf ? 'E' : 'G';
        }
        private String typeString() {
            return type==Elf ? "Elf" : "Goblin";
        }


        public String toString() {
            return String.format("%s(%d)", typeChar(), hitPoints);
        }

        public String toLocation() {
            return String.format("%s (x: %d, y: %d)", typeString(), x, y);
        }


        private Node getPath(int tx, int ty, int xx, int yy, Node shortestPath) {

            // Discard if Manhattan distance > shortest path
            if (shortestPath != null) {
                if (Math.abs(tx-xx) + Math.abs(ty-yy) > shortestPath.dist) {
                    return null;
                }
            }

            int height = map.length;
            int width = map[0].length;

            List<Node> unvisited = new ArrayList<>();
            Node[][] grid = new Node[height][width];


            /*
             * Dijkstra's algorithm finds shortest path, but is not amenable to selecting
             * correct node (using the required reading order) when two have same distance
             */

            Node node = new Node(xx, yy, 0, tx, ty);
            grid[yy][xx] = node;
            unvisited.add(node);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (map[y][x] == '.') {
                        Node nd = new Node(x,y, Integer.MAX_VALUE, tx, ty);
                        grid[y][x] = nd;
                        unvisited.add(nd);
                    } else {
                        grid[y][x] = null;
                    }
                }
            }

            while (!unvisited.isEmpty()) {
                node = unvisited.get(0);
                if (node.dist == Integer.MAX_VALUE) {
                    break;
                }
                if (shortestPath != null && node.dist > shortestPath.dist) {
                    break;
                }
                if (node.x == tx && node.y == ty) {
                    return node;
                }
                for (Node nd: node.adjacents(grid)) {
                    if (unvisited.contains(nd)) {
                        if (nd.dist > 1 + node.dist) {
                            nd.dist = 1 + node.dist;
                            nd.previous = node;
                        }
                    }
                }
                unvisited.remove(node);
                Collections.sort(unvisited);
            }
            return null;
        }

        private Node getShortestPath(Unit target) {
            Node shortestPath = null;
            int tx, ty, xx, yy;

            ArrayList<Node> adjacents = new ArrayList<>();
            xx = this.x; yy = this.y-1;
            if (map[yy][xx] == '.') adjacents.add(new Node(xx, yy));
            xx = this.x-1; yy = this.y;
            if (map[yy][xx] == '.') adjacents.add(new Node(xx, yy));
            xx = this.x+1; yy = this.y;
            if (map[yy][xx] == '.') adjacents.add(new Node(xx, yy));
            xx = this.x; yy = this.y+1;
            if (map[yy][xx] == '.') adjacents.add(new Node(xx, yy));

            if (adjacents.size() == 0) return null;

            tx = target.x; ty = target.y - 1;       // up
            if (map[ty][tx] == '.') {
                for (Node nd: adjacents) {
                    Node node = getPath(tx, ty, nd.x, nd.y, shortestPath);
                    if (node != null && (shortestPath == null || node.dist < shortestPath.dist)) {
                        shortestPath = node;
                        if (shortestPath.dist == 0) return shortestPath;
                    }
                }
            }
            tx = target.x-1; ty = target.y;         // left
            if (map[ty][tx] == '.') {
                for (Node nd: adjacents) {
                    Node node = getPath(tx, ty, nd.x, nd.y, shortestPath);
                    if (node != null && (shortestPath == null || node.dist < shortestPath.dist)) {
                        shortestPath = node;
                        if (shortestPath.dist == 0) return shortestPath;
                    }
                }
            }
            tx = target.x+1; ty = target.y;         // right
            if (map[ty][tx] == '.') {
                for (Node nd: adjacents) {
                    Node node = getPath(tx, ty, nd.x, nd.y, shortestPath);
                    if (node != null && (shortestPath == null || node.dist < shortestPath.dist)) {
                        shortestPath = node;
                        if (shortestPath.dist == 0) return shortestPath;
                    }
                }
            }
            tx = target.x; ty = target.y+1;         // down
            if (map[ty][tx] == '.') {
                for (Node nd: adjacents) {
                    Node node = getPath(tx, ty, nd.x, nd.y, shortestPath);
                    if (node != null && (shortestPath == null || node.dist < shortestPath.dist)) {
                        shortestPath = node;
                        if (shortestPath.dist == 0) return shortestPath;
                    }
                }
            }
            return shortestPath;
        }

        private boolean inRangeOf(Unit target) {
            if (target.x == x && (target.y == y-1 || target.y == y+1)) {
                return true;
            }
            if (target.y == y && (target.x == x-1 || target.x == x+1)) {
                return true;
            }
            return false;
        }

        private void attack(Unit target, boolean debug) {
            target.hitPoints -= this.attackPower;
            if (debug) {
                System.out.printf("%s hit by %s - HP=%d\n", target.toLocation(), this.toLocation(), target.hitPoints);
            }
            if (target.hitPoints <= 0) {
                if (debug) System.out.println(target.toLocation() + " Arrrghhhh!");
                target.isDead = true;
                map[target.y][target.x] = '.';
            }
        }

        private void moveTowards(Node node, boolean debug) {
/*
            Node first = null;
            while (node.previous != null) {
                first = node;
                node = node.previous;
            }
*/
            Node first = node;
            while (first.previous != null) {
                first = first.previous;
            }
            if (debug) {
                System.out.printf("%s moving to (x: %d, y: %d)\n", this.toLocation(), first.x, first.y);
            }
            map[y][x] = '.';
            x = first.x;
            y = first.y;
            map[y][x] = typeChar();
        }

        private List<Unit> getTargets(List<Unit> units) {
            List<Unit> targets = new ArrayList<>();
            for (Unit target: units) {
                if (target.type != type && !target.isDead) targets.add(target);
            }
            return targets;
        }

    }

    private class Node implements Comparable<Node> {
        int x, y, dist;
        int tx, ty;
        Node previous;

        private Node(int x, int y) {
            this(x, y, 0, 0, 0);
        }
        private Node(int x, int y, int dist, int tx, int ty) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.tx = tx;
            this.ty = ty;
        }

        public int compareTo(Node other) {
            return Integer.compare(dist, other.dist);
        }

        private List<Node> adjacents(Node[][] grid) {
            List<Node> res = new ArrayList<>();
            Node nd = grid[y - 1][x];
            if (nd != null) res.add(nd);
            nd = grid[y][x - 1];
            if (nd != null) res.add(nd);
            nd = grid[y][x + 1];
            if (nd != null) res.add(nd);
            nd = grid[y + 1][x];
            if (nd != null) res.add(nd);
            return res;
        }

        private Node reversePath() {
            Node res = this;
            while (res.previous != null) {
                res = res.previous;
            }
            return res;
        }

    }

    private String getMap() {
        String res = "";
        for (char[] row: map) {
            res += new String(row) + "\n";
        }
        return res;
    }

    private void outputMap(int round, List<Unit> units) {
        if (round == 0) {
            System.out.printf("Initially\n");
        } else if (round == 1) {
            System.out.printf("After %d round:\n", round);
        } else {
            System.out.printf("After %d rounds:\n", round);
        }
        for (int r = 0; r < map.length; r++) {
            String s = new String(map[r]);
            for (Unit unit: units) {
                if (unit.y == r) {
                    s = s + "  " + unit.toString();
                }
            }
            System.out.println(s);
        }
        System.out.println();
    }

    public Integer run(boolean debug) {

        map = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            map[i] = input.get(i).toCharArray();
        }
        List<Unit> units = new ArrayList<>();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (c == 'E' || c == 'G') {
                    units.add(new Unit(c, x, y));

                }
            }
        }
        int round = 0;
        String priorMap = null;
        if (debug) {
            outputMap(round, units);
            priorMap = getMap();
        }

        while (true) {
            boolean noTargets = false;
            for (Unit unit: units) {
                if (unit.isDead) {
                    continue;
                }
                unit.shortestPath = null;
                unit.attackTarget = null;
                List<Unit> targets = unit.getTargets(units);

                if (targets.size() == 0) {
                    noTargets = true;
                    break;
                }
                for (Unit target: targets) {
                    if (unit.inRangeOf(target)) {
                        if (unit.attackTarget == null || target.hitPoints < unit.attackTarget.hitPoints) {
                            unit.attackTarget = target;
                        }
                    } else if (unit.attackTarget == null) {
                        Node node = unit.getShortestPath(target);
                        if (node != null && (unit.shortestPath == null || node.dist < unit.shortestPath.dist)) {
                            unit.shortestPath = node;
                        }
                    }
                }
                if (unit.attackTarget != null) {
                    unit.attack(unit.attackTarget, debug);
                }
                else if (unit.shortestPath != null) {
                    unit.moveTowards(unit.shortestPath, debug);
                    for (Unit target: targets) {
                        if (unit.inRangeOf(target)) {
                            if (unit.attackTarget == null || target.hitPoints < unit.attackTarget.hitPoints) {
                                unit.attackTarget = target;
                            }
                        }
                    }
                    if (unit.attackTarget != null) {
                        unit.attack(unit.attackTarget, debug);
                    }
                } else {
                    if (debug) System.out.printf("%s cannot move...\n", unit.toLocation());
                }
            }
            int cnt = units.size();
            Iterator<Unit> itr = units.iterator();
            while (itr.hasNext()) {
                Unit unit = itr.next();
                if (unit.isDead) itr.remove();
            }
            if (units.size() < cnt) {
                System.out.printf("%d ", units.size());
            } else {
                System.out.printf(". ");
            }

            if (noTargets) {
                if (debug) outputMap(round, units);
                break;
            }

            round++;
            Collections.sort(units);

            if (debug) {
                String newMap = getMap();
                if (!newMap.equals(priorMap)) {
                    outputMap(round, units);
                    priorMap = newMap;
                }
            }
        }
        int res = 0;
        for (Unit unit: units) {
            res += unit.hitPoints;
        }
        res *= round;
        System.out.println();
        return res;
    }
}
