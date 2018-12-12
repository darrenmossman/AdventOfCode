package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K18_8 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_8 puzzle;
        int i;

        puzzle = new Y2K18_8(true);
        i = puzzle.run();
        test(i, 138);

        i = puzzle.run(true);
        test(i, 66);

        puzzle = new Y2K18_8(false);
        i = puzzle.run();
        System.out.printf("day 8: part 1 = %d\n", i);
        //test(i, 41555);

        i = puzzle.run(true);
        System.out.printf("day 8: part 2 = %d\n", i);
        //test(i, 16653);
    }

    //--------------------------------------------------------------------------------------------

    private Node root;

    public Y2K18_8(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, " ");
        ArrayList<Integer[]> arr = Utils.strArraysToNum(str);
        Integer[] vals = arr.get(0);

        root = new Node(null, vals, 0);
        root.buildNodes(vals, 0);
    }

    public int run() {
        return run(false);
    }

    static private class Node {
        int childCount;
        int metaCount;
        ArrayList<Node> childNodes;
        int[] metaData;

        private Node(Node parent, Integer[] vals, int p) {
            if (parent != null) {
                parent.childNodes.add(this);
            }
            childCount = vals[p];
            metaCount = vals[p+1];
            if (childCount > 0) {
                childNodes = new ArrayList<>(childCount);
            }
            if (metaCount > 0) {
                metaData = new int[metaCount];
            }
        }

        private int value(boolean part2) {
            int sum = 0;

            if (part2) {
                if (childCount == 0) {
                    for (int m : metaData) sum += m;
                } else if (metaCount > 0) {
                    for (int m : metaData) {
                        if (m < 1 || m > childCount) continue;
                        Node child = childNodes.get(m - 1);
                        if (child != null) {
                            sum += child.value(part2);
                        }
                    }
                }
            }
            else {
                for (int m : metaData) sum += m;
                if (childCount > 0) {
                    for (Node child: childNodes) {
                        sum += child.value(part2);
                    }
                }
            }
            return sum;
        }

        private int buildNodes(Integer[] vals, int p) {
            p += 2;
            for (int i = 0; i < childCount; i++) {
                Node child = new Node(this, vals, p);
                p = child.buildNodes(vals, p);
            }
            for (int i = 0; i < metaCount; i++) {
                int m = vals[p++];
                metaData[i] = m;
            }
            return p;
        }
    }

    public int run(boolean part2) {
        return root.value(part2);
    }
}













