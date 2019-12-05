package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Y2K16_4 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_4 puzzle = new Y2K16_4(false);
        int i = puzzle.run();
        System.out.printf("day 4: part 1 = %d\n", i);
        test(i, 245102);

        i = puzzle.run(true);
        System.out.printf("day 4: part 2 = %d\n", i);
        test(i, 324);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    public Y2K16_4(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    public int run() {
        return run(false);
    }

    private boolean isReal(String name, String checksum) {

        HashMap<Character, Integer> hash = new HashMap<>();
        for (char c: name.replaceAll("-", "").toCharArray()) {
            Integer i = hash.get(c);
            if (i == null) hash.put(c, 1);
            else hash.put(c, i+1);
        }
        List<Map.Entry<Character, Integer>> list = new LinkedList<>(hash.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> entry1, Map.Entry<Character, Integer> entry2) {
                if (entry1.getValue() == entry2.getValue()) {
                    return entry1.getKey() - entry2.getKey();
                } else {
                    return entry2.getValue() - entry1.getValue();
                }
            }
        });
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Character, Integer> entry: list) {
            builder.append(entry.getKey());
        }
        String test = builder.toString();
        return (test.length() >= 5 && test.substring(0, 5).equals(checksum));
    }

    public int run(boolean part2) {

        int res = 0;
        for (String s : input) {
            int p = s.lastIndexOf("-");
            int b = s.indexOf("[");
            String name = s.substring(0, p);
            String id = s.substring(p+1, b);
            int sectorid = Integer.parseInt(id);
            String checksum = s.substring(b+1, s.length()-1);

            if (isReal(name, checksum)) {
                if (part2) {
                    StringBuilder builder = new StringBuilder();
                    for (Character c: name.toCharArray()) {
                        if (c == '-') {
                            c = ' ';
                        } else {
                            int i = (int)c;
                            i -= 'a';
                            i = (i + sectorid) % 26;
                            i += 'a';
                            c = (char)i;
                        }
                        builder.append(c);
                    }
                    String test = builder.toString();
                    if ("northpole object storage".equals(test)) {
                        res = sectorid;
                        break;
                    }
                }
                else {
                    res += sectorid;
                }
            }
        }
        return res;
    }
}
