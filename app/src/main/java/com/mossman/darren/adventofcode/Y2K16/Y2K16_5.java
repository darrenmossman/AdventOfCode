package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Y2K16_5 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_5 puzzle = new Y2K16_5();
        String s;
        /* skip slow tests
        s = puzzle.run("abc");
        test(s, "18f47a30");
        */

        s = puzzle.run("reyedfim");
        System.out.printf("day 5: part 1 = %s\n", s);
        //test(s, "f97c354d");

        /* skip slow tests
        s = puzzle.run("abc", true);
        test(s, "05ace8e3" );
        */

        s = puzzle.run("reyedfim", true);
        System.out.printf("day 5: part 2 = %s\n", s);
        //test(s, "863dde27");
    }

    //--------------------------------------------------------------------------------------------

    private String hashText(MessageDigest md, String input) {
        byte[] messageDigest = md.digest(input.getBytes());
        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);
        // Convert message digest into hex value
        String hashtext = no.toString(16);
        // Add preceding 0s to make it 32 bit
        StringBuilder builder = new StringBuilder(hashtext);
        while (builder.length() < 32) {
            builder.insert(0, '0');
        }
        return builder.toString();
    }

    public String run(String input) {
        return run(input, false);
    }

    public String run(String input, boolean part2) {

        if (part2) {
            System.out.printf("Computing \"%s\" part 2 ", input);
        } else {
            System.out.printf("Computing \"%s\" part 1 ", input);
        }

        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

        char[] arr = new char[8];
        int cnt = 0;
        String res = "";
        int i = 0;
        while (true) {
            String hash = hashText(md, input + i);
            if (hash.startsWith("00000")) {
                char c = hash.charAt(5);
                if (part2) {
                    if (c >= '0' && c <= '7') {
                        int p = (int)c - '0';
                        if (arr[p] == 0) {
                            c = hash.charAt(6);
                            arr[p] = c;
                            cnt++;
                            System.out.printf(".");
                            if (cnt == 8) {
                                res = new String(arr);
                                break;
                            }
                        }
                    }

                } else {
                    res += c;
                    System.out.printf(".");
                    if (res.length() == 8) break;
                }
            }
            i++;
        }
        System.out.printf("\n");
        return res;
    }
}
