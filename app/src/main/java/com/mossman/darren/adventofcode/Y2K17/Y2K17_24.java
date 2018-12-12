package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_24 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_24 puzzle = new Y2K17_24(true);
        int i = puzzle.run();
        test(i, 31);
        i = puzzle.run(true);
        test(i, 19);

        puzzle = new Y2K17_24(false);
        i = puzzle.run();
        System.out.printf("day 24: part 1 = %d\n", i);
        //test(i, 1656);

        i = puzzle.run(true);
        System.out.printf("day 24: part 2 = %d\n", i);
        //test(i, 1642);
    }
    //--------------------------------------------------------------------------------------------

    private ArrayList<Integer[]> vals;

    public Y2K17_24(boolean test) {
        ArrayList<String> input = Utils.readFile(getFilename(test));
        ArrayList<String[]> str = Utils.parseInput(input, "/");
        vals = Utils.strArraysToNum(str);
    }

    public int run() {
        return run(false);
    }

    private class Component {
        int port1, port2;
        private Component(int port1, int port2) {
            this.port1 = port1;
            this.port2 = port2;
        }
        private Integer match(int req) {
            if (port1 == req) return port2;
            else if (port2 == req) return port1;
            else return null;
        }
        private int strength() {
            return port1 + port2;
        }
    }

    private class BridgeNode {
        Component component;
        ArrayList<BridgeNode> items = new ArrayList<>();;
        int position = 0; int maxPosition = 0;
        int strength = 0;

        private BridgeNode() {
            // dummy component to start bridge
            component = new Component(0,0);
        }
        private BridgeNode(Component component, int position) {
            // components are used multiple times so need a wrapper
            this.component = component;
            this.position = position;
            this.maxPosition = position;
        }

        private void build(ArrayList<Component> components, int port, boolean part2) {
            int maxStrength = 0;
            for (Component comp: components) {
                Integer next = comp.match(port);
                if (next != null) {
                    ArrayList<Component> comps = new ArrayList<>(components);
                    comps.remove(comp);
                    BridgeNode node = new BridgeNode(comp, position+1);
                    items.add(node);
                    node.build(comps, next, part2);
                    if (part2) {
                        // only update max Strength if >= maxPosition
                        if (node.maxPosition > maxPosition) {
                            maxPosition = node.maxPosition;
                            maxStrength = node.strength;
                        } else if (node.maxPosition == maxPosition) {
                            if (node.strength > maxStrength) {
                                maxStrength = node.strength;
                            }
                        }
                    } else {
                        // update max Strength
                        if (node.strength > maxStrength) {
                            maxStrength = node.strength;
                        }
                    }
                }
            }
            strength = component.strength() + maxStrength;
        }
    }

    public int run(boolean part2) {
        ArrayList<Component> components = new ArrayList<>(vals.size());
        for (Integer[] val: vals) {
            components.add(new Component(val[0], val[1]));
        }
        BridgeNode bridge = new BridgeNode();
        bridge.build(components, 0, part2);
        return bridge.strength;
    }
}
