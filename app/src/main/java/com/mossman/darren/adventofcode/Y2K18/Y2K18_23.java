package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K18_23 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_23 puzzle;
        int i;

        puzzle = new Y2K18_23(true);
        i = puzzle.part1();
        test(i, 7);

        puzzle = new Y2K18_23(true, 1);
        i = puzzle.part2();
        test(i, 36);

        puzzle = new Y2K18_23(false);
        i = puzzle.part1();
        System.out.printf("day 23: part 1 = %d\n", i);
        test(i, 674);

        i = puzzle.part2();
        System.out.printf("day 23: part 2 = %d\n", i);
        test(i, 129444177);

    }

    //--------------------------------------------------------------------------------------------

    private class Nanobot {
        int x, y, z, r;

        private Nanobot(Matcher m) {
            x = Integer.parseInt(m.group(1));
            y = Integer.parseInt(m.group(2));
            z = Integer.parseInt(m.group(3));
            r = Integer.parseInt(m.group(4));
        }

        private int distance(int x, int y, int z) {
            return Math.abs(x-this.x) + Math.abs(y-this.y) + Math.abs(z-this.z);
        }

        private boolean inRange(int x, int y, int z) {
            return distance(x, y, z) <= r;
        }

        private boolean inRange(Volume vol) {

            if (vol.minx == vol.maxx && vol.miny == vol.maxy && vol.minz == vol.maxz) {
                return inRange(vol.minx, vol.miny, vol.minz);
            }

            int d = 0;
            if (x < vol.minx || x > vol.maxx) {
                d += Math.min(Math.abs(x - vol.minx), Math.abs(x - vol.maxx));
            }
            if (y < vol.miny || y > vol.maxy) {
                d += Math.min(Math.abs(y - vol.miny), Math.abs(y - vol.maxy));
            }
            if (z < vol.minz || z > vol.maxz) {
                d += Math.min(Math.abs(z - vol.minz), Math.abs(z - vol.maxz));
            }
            return (d <= r);
        }

        private int distance(Nanobot nanobot) {
            return Math.abs(x-nanobot.x) + Math.abs(y-nanobot.y) + Math.abs(z-nanobot.z);
        }

        private boolean inRange(Nanobot nanobot) {
            return distance(nanobot) <= r;
        }

    }

    private ArrayList<Nanobot> nanobots;

    public Y2K18_23(boolean test) {
        this(test, 0);
    }

    public Y2K18_23(boolean test, int t) {
        String filename = getFilename(test);
        if (t > 0) {
            filename = filename.replace(".txt", "_"+t+".txt");
        }
        ArrayList<String> input = Utils.readFile(filename);
        final Pattern p = Pattern.compile("pos=<(-*\\d+),(-*\\d+),(-*\\d+)>, r=(\\d+)");

        nanobots = new ArrayList<>(input.size());
        for (String s: input) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                nanobots.add(new Nanobot(m));
            }
        }
    }

    public int part1() {
        Nanobot max = null;
        for (Nanobot nanobot: nanobots) {
            if (max == null || nanobot.r > max.r) {
                max = nanobot;
            }
        }
        int cnt = 0;
        for (Nanobot nanobot: nanobots) {
            if (max.inRange(nanobot)) {
                cnt++;
            }
        }
        return cnt;
    }

    private class Volume {
        private int minx, miny, minz, maxx, maxy, maxz;
        private int count = 0;

        private Volume(int minx, int miny, int minz, int maxx, int maxy, int maxz) {
            this.minx = minx;
            this.miny = miny;
            this.minz = minz;
            this.maxx = maxx;
            this.maxy = maxy;
            this.maxz = maxz;
        }

        private Volume(int minx, int miny, int minz) {
            this(minx, miny, minz, minx, miny, minz);
        }
    }
    
    public int part2() {

        Nanobot nanobot = nanobots.get(0);
        int minx = nanobot.x-nanobot.r, miny = nanobot.y-nanobot.r, minz = nanobot.z-nanobot.r;
        int maxx = nanobot.x+nanobot.r, maxy = nanobot.y+nanobot.r, maxz = nanobot.z+nanobot.r;

        for (Nanobot n: nanobots) {
            if (minx > n.x-n.r) minx = n.x-n.r;
            if (miny > n.y-n.r) miny = n.y-n.r;
            if (minz > n.z-n.r) minz = n.z-n.r;
            if (maxx < n.x+n.r) maxx = n.x+n.r;
            if (maxy < n.y+n.r) maxy = n.y+n.r;
            if (maxz < n.z+n.r) maxz = n.z+n.r;
        }

        Volume vol;
        while (true) {
            Volume[] volume = new Volume[8];

            boolean found = (maxx - minx <= 1 && maxy - miny <= 1 && maxz - minz <= 1);
            if (found) {
                volume[0] = new Volume(minx, miny, minz);
                volume[1] = new Volume(maxx, miny, minz);
                volume[2] = new Volume(minx, maxy, minz);
                volume[3] = new Volume(maxx, maxy, minz);
                volume[4] = new Volume(minx, miny, maxz);
                volume[5] = new Volume(maxx, miny, maxz);
                volume[6] = new Volume(minx, maxy, maxz);
                volume[7] = new Volume(maxx, maxy, maxz);
            }
            else {
                volume[0] = new Volume(minx, miny, minz, (minx + maxx) / 2, (miny + maxy) / 2, (minz + maxz) / 2);
                volume[1] = new Volume((minx + maxx) / 2, miny, minz, maxx, (miny + maxy) / 2, (minz + maxz) / 2);
                volume[2] = new Volume(minx, (miny + maxy) / 2, minz, (minx + maxx) / 2, maxy, (minz + maxz) / 2);
                volume[3] = new Volume((minx + maxx) / 2, (miny + maxy) / 2, minz, maxx, maxy, (minz + maxz) / 2);
                volume[4] = new Volume(minx, miny, (minz + maxz) / 2, (minx + maxx) / 2, (miny + maxy) / 2, maxz);
                volume[5] = new Volume((minx + maxx) / 2, miny, (minz + maxz) / 2, maxx, (miny + maxy) / 2, maxz);
                volume[6] = new Volume(minx, (miny + maxy) / 2, (minz + maxz) / 2, (minx + maxx) / 2, maxy, maxz);
                volume[7] = new Volume((minx + maxx) / 2, (miny + maxy) / 2, (minz + maxz) / 2, maxx, maxy, maxz);
            }

            int vmax = 0;
            for (int v = 0; v < volume.length; v++) {
                vol = volume[v];
                for (Nanobot n : nanobots) {
                    if (n.inRange(vol)) {
                        vol.count++;
                        if (vol.count > volume[vmax].count) {
                            vmax = v;
                        }
                    }
                }
            }

            vol = volume[vmax];
            if (found) {
                break;
            }

            minx = vol.minx; miny = vol.miny; minz = vol.minz;
            maxx = vol.maxx; maxy = vol.maxy; maxz = vol.maxz;
        }

        return vol.minx + vol.miny + vol.minz;
    }
}
