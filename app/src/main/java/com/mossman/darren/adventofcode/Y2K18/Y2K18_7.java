package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Y2K18_7 extends Y2K18_Puzzle {

    public static void main(String[] args) {
        Y2K18_7 puzzle;
        String s;

        puzzle = new Y2K18_7(true);
        s = puzzle.run();
        test(s, "CABDFE");

        s = puzzle.run(true, 2, 0);
        test(s, 15);

        puzzle = new Y2K18_7(false);
        s = puzzle.run();
        System.out.printf("day 7: part 1 = %s\n", s);
        //test(s, "CQSWKZFJONPBEUMXADLYIGVRHT");

        s = puzzle.run(true, 5, 60);
        System.out.printf("day 7: part 2 = %s\n", s);
        //test(s, 914);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    private LinkedHashMap<Character, ArrayList<Character>> map;
    private ArrayList<Character> letters;

    public Y2K18_7(boolean test) {
        input = Utils.readFile(getFilename(test));
        map = new LinkedHashMap<>();
        letters = new ArrayList<>();
    }

    private void init() {
        map.clear();
        letters.clear();
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            char depend = s.charAt(s.indexOf(" ")+1);
            char letter = s.charAt(s.indexOf("can")-2);

            ArrayList<Character> dependsOn = map.get(letter);
            if (dependsOn == null) {
                dependsOn = new ArrayList<>();
                map.put(letter, dependsOn);
            }
            dependsOn.add(depend);
            if (!letters.contains(letter)) {
                letters.add(letter);
            }
            if (!letters.contains(depend)) {
                letters.add(depend);
            }
        }
    }

    public String run() {
        return run(false, 0, 0);
    }

    public String run(boolean part2, int workerCount, int base) {
        init();

        ArrayList<Character> available = new ArrayList<>();
        String steps = "";

        if (part2) {
            LinkedHashMap<Character, Integer> tasks = new LinkedHashMap<>();
            ArrayList<Character> workers = new ArrayList<>(workerCount);
            for (int w = 0; w < workerCount; w++) {
                workers.add('.');
            }

            long seconds = 0;
            while (!letters.isEmpty()) {
                // look for available letters
                for (Character letter : letters) {
                    if (available.contains(letter)) continue;
                    if (tasks.containsKey(letter)) continue;

                    ArrayList<Character> dependsOn = map.get(letter);
                    if (dependsOn == null || dependsOn.isEmpty()) {
                        available.add(letter);
                    }
                }
                if (!available.isEmpty()) {
                    Collections.sort(available);

                    // start tasks if workers available
                    Iterator itr = available.iterator();
                    while (itr.hasNext()) {
                        Character letter = (Character)itr.next();
                        for (int w = 0; w < workerCount; w++) {
                            if (workers.get(w) == '.') {
                                workers.set(w, letter);
                                int duration = letter - 'A' + base + 1;
                                tasks.put(letter, duration);
                                steps += letter;
                                itr.remove();
                                break;
                            }
                        }
                    }
                }

                // workers process tasks
                for (int w = 0; w < workerCount; w++) {
                    Character letter = workers.get(w);
                    if (letter != '.') {
                        Integer t = tasks.get(letter);
                        if (t != null && t > 0) {
                            t--;
                            if (t == 0) {
                                workers.set(w, '.');
                                tasks.remove(letter);
                                for (ArrayList<Character> dep : map.values()) {
                                    dep.remove(letter);
                                }
                                letters.remove(letter);
                            } else {
                                tasks.put(letter, t);
                            }
                        }
                    }
                }
                seconds++;
            }
            return seconds+"";

        } else{
            while (!letters.isEmpty()) {
                for (Character letter : letters) {
                    if (available.contains(letter)) continue;
                    ArrayList<Character> dependsOn = map.get(letter);
                    if (dependsOn == null || dependsOn.isEmpty()) {
                        available.add(letter);
                    }
                }
                if (!available.isEmpty()) {
                    Collections.sort(available);
                    Character letter = available.get(0);
                    available.remove(0);
                    steps += letter;
                    for (ArrayList<Character> dep : map.values()) {
                        dep.remove(letter);
                    }
                    letters.remove(letter);
                }
            }
            return steps;
        }
    }
}

