package com.mossman.darren.adventofcode;

import static com.mossman.darren.adventofcode.MovingObject.Direction.down;
import static com.mossman.darren.adventofcode.MovingObject.Direction.left;
import static com.mossman.darren.adventofcode.MovingObject.Direction.right;
import static com.mossman.darren.adventofcode.MovingObject.Direction.up;

public class MovingObject {

    public static enum Direction {up, right, down, left};

    public int x = 0, y = 0;
    public Direction dir = up;
    public char mark = ' ';

    public MovingObject(char mark) {
        this.mark = mark;
    }
    public MovingObject() {
    }

    public MovingObject(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    private static final Direction[] dirs = Direction.values();

    public void setDir(char c) {
        switch (Character.toLowerCase(c)) {
            case 'u': dir = up; break;
            case 'd': dir = down; break;
            case 'l': dir = left; break;
            case 'r': dir = right; break;
        }
    }

    public void turnLeft() {
        dir = dirs[dir.ordinal() == 0 ? dirs.length - 1 : dir.ordinal() - 1];
    }

    public void turnRight() {
        dir = dirs[(dir.ordinal() + 1) % dirs.length];
    }

    public void reverse() {
        dir = dirs[(dir.ordinal()+2) % dirs.length];
    }

    public void advance() {
        advance(1);
    }

    public void advance(int steps) {
        switch (dir) {
            case up:
                y-=steps;
                break;
            case right:
                x+=steps;
                break;
            case down:
                y+=steps;
                break;
            case left:
                x-=steps;
                break;
        }
    }

    public double distance() {
        return Math.sqrt(x*x + y*y);
    }

    public int manhattenDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
