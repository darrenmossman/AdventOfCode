package com.mossman.darren.adventofcode.Y2K17;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;

public class Y2K17_20 extends Y2K17_Puzzle {

    public static void main(String[] args) {

        Y2K17_20 puzzle = new Y2K17_20(true);
        int i = puzzle.run();
        test(i, 0);

        puzzle = new Y2K17_20(false);
        i = puzzle.run();
        System.out.printf("day 20: part 1 = %d\n", i);
        //test(i, 157);

        i = puzzle.run(true);
        System.out.printf("day 20: part 2 = %d\n", i);
        //test(i, 499);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K17_20(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run() {
        return run(false);
    }

    private static class Particle {
        int pX, pY, pZ;
        int vX, vY, vZ;
        int aX, aY, aZ;
        private Particle(String[] arr) {
            pX = Integer.parseInt(arr[0]);
            pY = Integer.parseInt(arr[1]);
            pZ = Integer.parseInt(arr[2]);
            vX = Integer.parseInt(arr[3]);
            vY = Integer.parseInt(arr[4]);
            vZ = Integer.parseInt(arr[5]);
            aX = Integer.parseInt(arr[6]);
            aY = Integer.parseInt(arr[7]);
            aZ = Integer.parseInt(arr[8]);
        }
        private void update() {
            vX += aX;
            vY += aY;
            vZ += aZ;
            pX += vX;
            pY += vY;
            pZ += vZ;
        }
        private int distance() {
            return Math.abs(pX)+Math.abs(pY)+Math.abs(pZ);
        }
        private int acceleration() {
            return Math.abs(aX)+Math.abs(aY)+Math.abs(aZ);
        }
        private boolean collides(Particle p) {
            return (pX == p.pX) && (pY == p.pY) && (pZ == p.pZ);
        }
    }

    public int run(boolean part2) {
        ArrayList<Particle> particles = new ArrayList<>(input.size());
        for (String s: input) {
            s = s.replaceAll(" ", "");
            s = s.replaceAll("<", "");
            s = s.replaceAll(">", "");
            s = s.replaceAll("p", "");
            s = s.replaceAll("v", "");
            s = s.replaceAll("a", "");
            s = s.replaceAll("=", "");
            String[] arr = s.split(",");
            Particle particle = new Particle(arr);
            particles.add(particle);
        }

        if (part2) {
            int count = 9999;
            for (int tick = 0; tick < count; tick++) {
                for (int i = particles.size()-1; i >= 0; i--) {
                    Particle p = particles.get(i);
                    boolean collides = false;
                    for (int j = i-1; j >= 0; j--) {
                        Particle p1 = particles.get(j);
                        if (p1.collides(p)) {
                            collides = true;
                            particles.remove(j);
                            i--;
                        }
                    }
                    if (collides) particles.remove(i);
                }
                for (Particle p: particles) {
                    p.update();
                }
            }
            // this is crappy brute force solution - better to look to see if particles are diverging
            return particles.size();
        }
        /*
        int min = 0;
        Particle nearest = null;
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            int dist = particle.distance();
            if (nearest == null || dist < min) {
                nearest = particle;
                min = dist;
            }
        }
        return particles.indexOf(nearest);
        */
        else {
            int min = 0;
            Particle lowest = null;
            for (int i = 0; i < particles.size(); i++) {
                Particle particle = particles.get(i);
                int acc = particle.acceleration();
                if (lowest == null || acc < min) {
                    lowest = particle;
                    min = acc;
                }
            }
            return particles.indexOf(lowest);
        }
    }
}
