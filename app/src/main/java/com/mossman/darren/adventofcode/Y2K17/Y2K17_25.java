package com.mossman.darren.adventofcode.Y2K17;

import java.util.HashSet;

public class Y2K17_25 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_25 puzzle = new Y2K17_25();
        long l = puzzle.run(12302209);
        System.out.printf("day 25: part 1 = %d\n", l);
        //test(l, 633);

        l = puzzle.run(12302209, true);
        System.out.printf("day 25: part 2 = %d\n", l);
        //test(l, 0);
    }

    //--------------------------------------------------------------------------------------------

    private static class TuringMachine {

        private enum State {A, B, C, D, E, F};

        private State state = State.A;
        private int pos = 0;

        private HashSet<Integer> tape = new HashSet<>();

        public TuringMachine() {
        }
        private int readVal() {
            return tape.contains(pos) ? 1 : 0;
        }
        private void writeVal(int val) {
            if (val == 1) {
                tape.add(pos);
            } else {
                tape.remove(pos);
            }
        }
        private void moveRight() {
            pos++;
        }
        private void moveLeft() {
            pos--;
        }
        private void setState(State state) {
            this.state = state;
        }

        private int countOnes() {
            return tape.size();
        }

        public int run(int steps) {

            for (int i = 0; i < steps; i++) {
                int val = readVal();
                switch (state) {
                    case A:
                        if (val == 0) {
                            writeVal(1);
                            moveRight();
                            setState(State.B);
                        } else {
                            writeVal(0);
                            moveLeft();
                            setState(State.D);
                        }
                        break;
                    case B:
                        if (val == 0) {
                            writeVal(1);
                            moveRight();
                            setState(State.C);
                        } else {
                            writeVal(0);
                            moveRight();
                            setState(State.F);
                        }
                        break;
                    case C:
                        if (val == 0) {
                            writeVal(1);
                            moveLeft();
                            setState(State.C);
                        } else {
                            writeVal(1);
                            moveLeft();
                            setState(State.A);
                        }
                        break;
                    case D:
                        if (val == 0) {
                            writeVal(0);
                            moveLeft();
                            setState(State.E);
                        } else {
                            writeVal(1);
                            moveRight();
                            setState(State.A);
                        }
                        break;
                    case E:
                        if (val == 0) {
                            writeVal(1);
                            moveLeft();
                            setState(State.A);
                        } else {
                            writeVal(0);
                            moveRight();
                            setState(State.B);
                        }
                        break;
                    case F:
                        if (val == 0) {
                            writeVal(0);
                            moveRight();
                            setState(State.C);
                        } else {
                            writeVal(0);
                            moveRight();
                            setState(State.E);
                        }
                        break;
                }
            }
            return countOnes();
        }
    }

    private TuringMachine tm;

    public Y2K17_25() {
        tm = new TuringMachine();
    }

    public long run(int steps) {
        return run(steps, false);
    }

    public long run(int steps, boolean part2) {
        if (part2) {
            return 0;
        }
        else {
            return tm.run(steps);
        }
    }
}
