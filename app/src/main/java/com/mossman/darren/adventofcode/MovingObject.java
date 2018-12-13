package com.mossman.darren.adventofcode;

public class MovingObject {

    public enum Direction {up, right, down, left};

    public int x, y;
    public Direction dir;

    public MovingObject(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    private static final Direction[] dirs = Direction.values();

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
