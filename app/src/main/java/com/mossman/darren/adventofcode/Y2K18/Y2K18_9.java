package com.mossman.darren.adventofcode.Y2K18;

public class Y2K18_9 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_9 puzzle = new Y2K18_9();
        long i;

        i = puzzle.run(9, 25);
        test(i, 32);
        i = puzzle.run(10, 1618);
        test(i, 8317);
        i = puzzle.run(13, 7999);
        test(i, 146373);
        i = puzzle.run(17, 1104);
        test(i, 2764);
        i = puzzle.run(21, 6111);
        test(i, 54718);
        i = puzzle.run(30, 5807);
        test(i, 37305);

        i = puzzle.run(416, 71617);
        System.out.printf("day 9: part 1 = %d\n", i);
        //test(i, 436720);

        i = puzzle.run(416, 71617*100);
        System.out.printf("day 9: part 2 = %d\n", i);
        //test(i, 3527845091L);
    }

    //--------------------------------------------------------------------------------------------


    public Y2K18_9() {
    }

    private static class Marble {
        int score;
        Marble prior, next;

        private Marble rotate(int r) {
            Marble res = this;
            if (r < 0) {
                while (r<0) {
                    res = res.prior; r++;
                }
            } else {
                while (r>0) {
                    res = res.next; r--;
                }
            }
            return res;
        }
        private Marble remove() {
            Marble res = next;
            prior.next = next;
            next.prior = prior;
            return res;
        }
        private Marble add(int score) {
            Marble res = new Marble(score, this, this.next);
            return res;
        }
        private Marble() {
            this.score = 0;
            this.prior = this;
            this.next = this;
        }
        private Marble(int score, Marble prior, Marble next) {
            this.score = score;
            this.prior = prior;
            this.next = next;
            prior.next = this;
            next.prior = this;
        }
    }

    public long run(int playerCount, int marbleCount) {
        long[] players = new long[playerCount];
        long highScore = 0;
        Marble circle = new Marble();
        for (int marble = 1; marble <= marbleCount; marble++) {
            if (marble % 23 == 0) {
                circle = circle.rotate(-7);
                int p = (marble-1) % playerCount;
                players[p] += marble + circle.score;
                if (players[p] > highScore) {
                    highScore = players[p];                     // see alternative below
                }
                circle = circle.remove();
            }
            else {
                circle = circle.rotate(1);
                circle = circle.add(marble);
            }
        }
        //return Arrays.stream(players).max().getAsLong();      // alternative that doesn't require we keep track
        return highScore;
    }
}













