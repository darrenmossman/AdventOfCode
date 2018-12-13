package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.MovingObject;
import com.mossman.darren.adventofcode.MovingObject.Direction;
import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Y2K18_13 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_13 puzzle;
        String s;

        puzzle = new Y2K18_13(true);
        s = puzzle.run();
        test(s, "7,3");

        puzzle = new Y2K18_13(false);
        s = puzzle.run();
        System.out.printf("day 13: part 1 = %s\n", s);
        //test(s, "71,121");

        s = puzzle.run(true);
        System.out.printf("day 13: part 2 = %s\n", s);
        //test(s, "71,76");
    }

    //--------------------------------------------------------------------------------------------

    private char[][] grid;
    private HashMap<Character, Direction> dirMap;
    private ArrayList<Cart> carts;

    public Y2K18_13(boolean test) {
        dirMap = new HashMap<>();
        dirMap.put('^', Direction.up);
        dirMap.put('>', Direction.right);
        dirMap.put('v', Direction.down);
        dirMap.put('<', Direction.left);

        ArrayList<String> input = Utils.readFile(getFilename(test));
        grid = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            grid[i] = s.toCharArray();
        }
    }

    public String run() {
        return run(false);
    }

    private static class Cart extends MovingObject {
        private Direction nextDir = Direction.left;;
        private boolean removePending = false;

        private Cart(int x, int y, Direction dir) {
            super(x,y,dir);
        }

        @Override
        public String toString() {
            return String.format("%d,%d", x, y);
        }

        private void react(char c) {
            switch(c) {
                case '/':
                    if (dir == Direction.up || dir == Direction.down) {
                        turnRight();
                    } else {
                        turnLeft();
                    }
                    break;
                case '\\':
                    if (dir == Direction.up || dir == Direction.down) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                    break;
                case '+':
                    if (nextDir == Direction.left) {
                        turnLeft();
                        nextDir = null;
                    } else if (nextDir == null) {
                        nextDir = Direction.right;
                    } else if (nextDir == Direction.right) {
                        turnRight();
                        nextDir = Direction.left;
                    }
                    break;
            }
        }
    }

    private void initCarts() {
        carts = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            char[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                char c = row[x];
                if (dirMap.containsKey(c)) {
                    Cart cart = new Cart(x, y, dirMap.get(c));
                    carts.add(cart);
                }
            }
        }
    }

    private void sortCarts() {
        Collections.sort(carts, new Comparator<Cart>() {
            @Override
            public int compare(Cart cart1, Cart cart2) {
                if (cart1.y == cart2.y) return (cart1.x - cart2.x);
                else return (cart1.y - cart2.y);
            }
        });

    }

    public String run(boolean part2) {
        initCarts();
        boolean collision = false;
        while (true) {
            sortCarts();
            for (Cart cart: carts) {
                if (cart.removePending) continue;
                cart.advance();
                for (Cart other: carts) {
                    if (other == cart || other.removePending) continue;
                    collision = cart.y == other.y && cart.x == other.x;
                    if (collision) {
                        if (part2) {
                            cart.removePending = true;
                            other.removePending = true;
                            break;
                        } else {
                            return cart.toString();
                        }
                    }
                }
                if (!collision) {
                    char c = grid[cart.y][cart.x];
                    cart.react(c);
                }
            }
            if (part2) {
                Iterator itr = carts.iterator();
                while (itr.hasNext()) {
                    Cart cart = (Cart)itr.next();
                    if (cart.removePending) itr.remove();
                }
                if (carts.size() == 1) {
                    return carts.get(0).toString();
                }
                else if (carts.size() == 0) {
                    throw new IllegalStateException("Zero carts remain!");
                }
            }
        }
    }
}













