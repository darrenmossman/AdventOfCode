package com.mossman.darren.adventofcode.Y2K18;

import android.media.audiofx.DynamicsProcessing;

import com.mossman.darren.adventofcode.InfiniteGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.Equipment.climbing;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.Equipment.none;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.Equipment.torch;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.RegionType.narrow;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.RegionType.rocky;
import static com.mossman.darren.adventofcode.Y2K18.Y2K18_22.RegionType.wet;

public class Y2K18_22 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_22 puzzle;
        int i;

        puzzle = new Y2K18_22(510, 10, 10);
        i = puzzle.part1();
        test(i, 114);
        i = puzzle.part2();
        test(i, 45);

        puzzle = new Y2K18_22(8787, 10, 725);
        i = puzzle.part1();
        System.out.printf("day 22: part 1 = %d\n", i);
        test(i, 8090);

        i = puzzle.part2();
        System.out.printf("day 22: part 2 = %d\n", i);
        test(i, 0);
    }

    //--------------------------------------------------------------------------------------------

    private int depth;
    private int tx, ty;
    private InfiniteGrid<Region> map;
    private int totalRisk;
    //private InfiniteGrid<Node> gridC;
    //private InfiniteGrid<Node> gridT;
    //private InfiniteGrid<Node> gridN;

    enum RegionType {rocky, wet, narrow}
    enum Equipment {climbing, torch, none};

    private HashMap<Equipment, InfiniteGrid<Node>> grid;

    private class Region {
        int x, y;
        int geologicIndex;
        int erosionLevel;
        int risk;
        RegionType type;
        char c;

        private Region(int x, int y) {
            this.x = x;
            this.y = y;
            map.put(x, y, this);

            if (x == 0 && y == 0 || x == tx && y == ty) {
                geologicIndex = 0;
            } else if (y == 0) {
                geologicIndex = x * 16807;
            } else if (x == 0) {
                geologicIndex = y * 48271;
            } else {
                Region r1 = map.get(x-1, y);
                Region r2 = map.get(x, y-1);
                if (r1 == null) {
                    r1 = new Region(x-1, y);
                }
                if (r2 == null) {
                    r2 = new Region(x, y-1);
                }
                geologicIndex = r1.erosionLevel * r2.erosionLevel;
            }
            erosionLevel = (geologicIndex + depth) % 20183;
            risk = erosionLevel % 3;
            if (risk == 0) {
                type = rocky;
                c = '.';
            }
            else if (risk == 1) {
                type = wet;
                c = '=';
            }
            else {
                type = narrow;
                c = '|';
            }
        }
    }

    private void printMap() {
        for (int y = map.miny; y <= map.maxy; y++) {
            String s = "";
            for (int x = map.minx; x <= map.maxx; x++) {
                Region region = map.get(x, y);
                if (region == null) {
                    s += ' ';
                } else {
                    s += region.c;
                }
            }
            System.out.println(s);
        }
        System.out.flush();
    }

    public Y2K18_22(int depth, int tx, int ty) {
        map = new InfiniteGrid<>();
        this.depth = depth;
        this.tx = tx;
        this.ty = ty;

        totalRisk = 0;
        for (int y = 0; y <= ty; y++) {
            for (int x = 0; x <= tx; x++) {
                Region region = map.get(x, y);
                if (region == null) {
                    region = new Region(x, y);
                }
                totalRisk += region.risk;
            }
        }
        //printMap();
    }


    public int part1() {
        return totalRisk;
    }

    private String regionTypeString(RegionType type) {
        if (type == rocky) return "rocky";
        else if (type == wet) return "wet";
        else return "narrow";
    }

    private String equipmentString(Equipment equipment) {
        if (equipment == climbing) return "climbing";
        else if (equipment == torch) return "torch";
        else return "none";
    }

    private class Node implements Comparable<Node> {
        int x, y;
        Region region;
        Node previous;
        Equipment equipment;
        int dist;

        public String toString() {
            return String.format("%3d - (%d,%d) - %s - %s", dist, x, y,
                    regionTypeString(region.type), equipmentString(equipment));
        }

        private Node(int x, int y, InfiniteGrid<Node> grid) {
            Region region = map.get(x, y);
            if (region == null) {
                region = new Region(x, y);
            }
            this.x = region.x;
            this.y = region.y;
            this.region = region;
            if (grid == gridC) {
                this.equipment = climbing;
            } else if (grid == gridT) {
                this.equipment = torch;
            } else {
                this.equipment = none;
            }
            this.dist = Integer.MAX_VALUE;
            grid.put(x, y, this);
        }

        public int compareTo(Node other) {
            return Integer.compare(dist, other.dist);
        }

        private void printPath() {
            Node node = this;
            Equipment equipment = torch;
            int cnt = 0;
            int i = 0;
            while (node.previous != null) {
                if (node.equipment != equipment) {
                    equipment = node.equipment;
                    cnt++;
                }
                i++;
                node = node.previous;
            }

            equipment = torch;
            node = this;
            System.out.printf("%3d, %d = %s\n", i, cnt, node.toString());
            while (node.previous != null) {
                if (node.equipment != equipment) {
                    equipment = node.equipment;
                    cnt--;
                }
                i--;
                System.out.printf("%3d, %2d : %s\n", i, cnt, node.toString());
                node = node.previous;
            }
            System.out.flush();
        }

        private ArrayList<Node> getAdjacents() {
            ArrayList<Node> adjacents = new ArrayList<>();
            getAdjacents(adjacents, gridC);
            getAdjacents(adjacents, gridT);
            getAdjacents(adjacents, gridN);
            return adjacents;
        }

        private void getAdjacents(ArrayList<Node> adjacents, InfiniteGrid<Node> grid) {
            Node node;
            final int sz = 20;
            if (y > 0) {
                node = grid.get(x, y-1);               // above
                if (node == null) node = new Node(x, y-1, grid);
                adjacents.add(node);
            }
            if (y < ty + sz) {
                node = grid.get(x, y+1);                   // below
                if (node == null) node = new Node(x, y+1, grid);
                adjacents.add(node);
            }

            if (x > 0) {
                node = grid.get(x-1, y);               // left
                if (node == null) node = new Node(x-1, y, grid);
                adjacents.add(node);
            }
            if (x < tx + sz) {
                node = grid.get(x+1, y);                   // right
                if (node == null) node = new Node(x+1, y, grid);
                adjacents.add(node);
            }
        }
    }
    private Node getPath() {

        gridC = new InfiniteGrid<Node>();
        gridT = new InfiniteGrid<Node>();
        gridN = new InfiniteGrid<Node>();

        List<Node> unvisited = new ArrayList<>();
        List<Node> visited = new ArrayList<>();

        Node node = new Node(0, 0, gridT);
        node.dist = 0;
        unvisited.add(node);

        while (!unvisited.isEmpty()) {
            node = unvisited.get(0);
            if (node.dist == Integer.MAX_VALUE) {
                break;
            }
            if (node.x == tx && node.y == ty) {
                return node;
            }

            ArrayList<Node> adjacents = node.getAdjacents();
            for (Node nd: adjacents) {
                if (!visited.contains(nd) && !unvisited.contains(nd)) {
                    visited.add(nd);
                    unvisited.add(nd);
                }
                if (unvisited.contains(nd)) {
                    if (nd.x == tx && nd.y == ty) {
                        if (nd.equipment != torch) {
                            unvisited.remove(nd);
                            continue;
                        }
                    }
                    else if (nd.region.type == node.region.type && nd.equipment != node.equipment) {
                        //unvisited.remove(nd);
                        continue;
                    }
                    if ((nd.region.type == rocky && nd.equipment == none) |
                            (nd.region.type == narrow && nd.equipment == climbing) |
                            (nd.region.type == wet && nd.equipment == torch))
                    {
                        unvisited.remove(nd);
                        continue;
                    }

                    if ((node.region.type == rocky && nd.equipment == none) |
                            (node.region.type == narrow && nd.equipment == climbing) |
                            (node.region.type == wet && nd.equipment == torch))
                    {
                        continue;
                    }



                    int time = 1;
                    if (nd.equipment != node.equipment) {
                        time += 7;
                    }
                    if (nd.dist > time + node.dist) {
                        nd.dist = time + node.dist;
                        nd.previous = node;
                    }
                }
            }
            unvisited.remove(node);
            visited.add(node);
            Collections.sort(unvisited);
        }
        return null;
    }

    public int part2() {
        Node node = getPath();
        node.printPath();
        return node.dist;
    }
}
