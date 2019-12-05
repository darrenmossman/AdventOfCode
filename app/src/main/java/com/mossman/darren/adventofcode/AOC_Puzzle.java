package com.mossman.darren.adventofcode;

import com.mossman.darren.adventofcode.Y2K16.Y2K16_Puzzle;
import com.mossman.darren.adventofcode.Y2K17.Y2K17_Puzzle;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;
import com.mossman.darren.adventofcode.Y2K19.Y2K19_Puzzle;

public abstract class AOC_Puzzle {

    public static void main(String[] args) {

        Y2K16_Puzzle.main(null);
        Y2K17_Puzzle.main(null);
        Y2K18_Puzzle.main(null);
        Y2K19_Puzzle.main(null);
    }

    //--------------------------------------------------------------------------------------------

    private static int t = 0;
    public static void test(String fnd, String req) {
        t++;
        if (!req.equals(fnd)) {
            if (req.contains("\n")) {
                req = "\n" + req;

            }
            throw new RuntimeException(String.format("Failed Test %d - Required: %s - Found: %s", t, req, fnd));
        }
    }
    public static void test(String fnd, long req) {
        t++;
        try {
            long l = Long.parseLong(fnd);
            if (l != req) {
                throw new RuntimeException(String.format("Failed Test %d - Required: %d - Found: %s", t, req, fnd));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Failed Test %d - Required: %d - Found: %s", t, req, fnd));
        }
    }
    public static void test(long fnd, long req) {
        t++;
        if (fnd != req) {
            throw new RuntimeException(String.format("Failed test %d - Required: %d - Found: %d", t, req, fnd));
        }
    }

    public String getFilename(boolean test) {
        //String user_dir = System.getProperty("user.dir");

        String className = getClass().getSimpleName();
        String path = className;
        if (path.contains("_")) {
            path = path.substring(0, path.indexOf("_"));
        }
        String filename = "resources/" + path + "/" + className;
        if (test) filename += "_test";
        filename += ".txt";
        return filename;
    }
}
