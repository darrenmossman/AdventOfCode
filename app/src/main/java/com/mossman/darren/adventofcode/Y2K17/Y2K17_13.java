package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Y2K17_13 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_13 puzzle = new Y2K17_13(true);
        int i = puzzle.run();
        test(i, 24);

        i = puzzle.run(true);
        test(i, 10);

        puzzle = new Y2K17_13(false);
        i = puzzle.run();
        System.out.printf("day 13: part 1 = %d\n", i);
        //test(i, 748);

        i = puzzle.run(true);
        System.out.printf("day 13: part 2 = %d\n", i);
        //test(i, 3873662);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_13(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run() {
        return run(false);
    }

    private static class Layer {
        HashMap<Integer, Layer> owner;
        int depth, range;
        private Layer(HashMap<Integer, Layer> owner, int depth, int range) {
            owner.put(depth, this);
            this.owner = owner;
            this.depth = depth;
            this.range = range;
        }
        // only calculate position when we're at that level
        private int position(int step) {
            int n = (range-1);
            int p = step % (2*n);
            if (p > n) p = (2*n)-p;
            return p;
        }
    }

    public int run(boolean doDelay) {
        HashMap<Integer, Layer> owner = new HashMap<>(input.size());
        int maxDepth = 0;
        for (String s: input) {
            s = s.replaceAll(" ", "");
            String[] arr = s.split(":");
            int depth = Integer.parseInt(arr[0]);
            int range = Integer.parseInt(arr[1]);
            new Layer(owner, depth, range);
            if (depth > maxDepth) maxDepth = depth;
        }

        if (doDelay) {
            int delay = 0;
            boolean caught;
            do {
                caught = false;
                int depth = 0;
                while (depth <= maxDepth) {
                    Layer layer = owner.get(depth);
                    if (layer != null && layer.position(depth+delay) == 0) {
                        caught = true;
                        delay++;
                        break;
                    }
                    depth++;
                }
            } while (caught);
            return delay;
        }
        else {
            int depth = 0, total = 0;
            while (depth <= maxDepth) {
                Layer layer = owner.get(depth);
                if (layer != null && layer.position(depth) == 0) {
                    int severity = layer.depth * layer.range;
                    total += severity;
                }
                depth++;
            }
            return total;
        }
    }
}
