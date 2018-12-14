package com.mossman.darren.adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    public static boolean isAnagram(String s1, String s2) {
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        Arrays.sort(c1);
        Arrays.sort(c2);
        return c1.equals(c2);
    }

    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> res = new ArrayList<>();
        try {
            File file = new File(filename);
            FileInputStream fileStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    res.add(line);
                }
            } catch (IOException e) {
                System.out.printf(e.getMessage());
            }
        }
        catch (FileNotFoundException e) {
            System.out.printf(e.getMessage());
        }
        return res;
    }

    public static ArrayList<String[]> parseInput(ArrayList<String> input, String sep) {
        ArrayList<String[]> res = new ArrayList<>(input.size());
        for (String s: input) {
            if (sep.equals(" ")) {
                while (s.contains("  ")) {
                    s = s.replaceAll("  ", " ");
                }
                s = s.trim();
            }
            String[] arr = s.split(sep);
            res.add(arr);
        }
        return res;
    }

    public static ArrayList<Integer[]> strArraysToNum(ArrayList<String[]> str) {
        ArrayList<Integer[]> res = new ArrayList<>(str.size());
        for (String[] arr: str) {
            Integer[] num = new Integer[arr.length];
            res.add(num);
            for (int i = 0; i < arr.length; i++) {
                num[i] = Integer.parseInt(arr[i]);
            }
        }
        return res;
    }

    public static ArrayList<Integer> strArrayToNum(ArrayList<String> str) {
        ArrayList<Integer> res = new ArrayList<>(str.size());
        for (String s: str) {
            res.add(Integer.parseInt(s));
        }
        return res;
    }

}
