package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K18_4 extends Y2K18_Puzzle {

    public static void main(String[] args) {
       
        Y2K18_4 puzzle = new Y2K18_4(true);
        int i = puzzle.run();
        test(i, 240);
        i = puzzle.run(true);
        test(i, 4455);

        puzzle = new Y2K18_4(false);
        i = puzzle.run();
        System.out.printf("day 4: part 1 = %d\n", i);
        //test(i, 125444);

        i = puzzle.run(true);
        System.out.printf("day 4: part 2 = %d\n", i);
        //test(i, 18325);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    
    public Y2K18_4(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private static class Guard {
        int id;
        Date sleeps;
        int total;
        int[] minutes;
        private int mostFreq;

        private Guard(int id) {
            this.id = id;
            sleeps = null;
            total = 0;
            minutes = new int[60];
            mostFreq = -1;
        }

        private int getMostFrequentMinute() {
            if (mostFreq == -1) {
                int max = 0;
                for (int m = 0; m < 60; m++) {
                    if (minutes[m] > max) {
                        max = minutes[m];
                        mostFreq = m;
                    }
                }
            }
            return mostFreq;
        }

        private int getMostFrequentCount() {
            int mf = getMostFrequentMinute();
            if (mf == -1) return 0;
            else return minutes[mf];
        }
    }

    public int run() {
        return run(false);

    }
    public int run(boolean mostFreq) {

        Pattern p = Pattern.compile("\\[(.*)\\] (.*)");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        TreeMap<Date, String> shift = new TreeMap<>();

        for (String s: input) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                String dtStr = m.group(1);
                String action = m.group(2);
                try {
                    Date dt = sdf.parse(dtStr);
                    shift.put(dt, action);

                } catch (ParseException e) {
                }
            }
        }
        p = Pattern.compile("Guard #(\\d+) begins shift");
        HashMap<Integer, Guard> guards = new HashMap<>();
        Guard guard = null;
        for (Map.Entry<Date, String> entry: shift.entrySet()) {
            Date dt = entry.getKey();
            String action = entry.getValue();
            if (action.startsWith("Guard #") && action.endsWith("begins shift")) {
                Matcher m = p.matcher(action);
                if (m.matches()) {
                    int id = Integer.parseInt(m.group(1));
                    guard = guards.get(id);
                    if (guard == null) {
                        guard = new Guard(id);
                        guards.put(id, guard);
                    }
                }
            }  else if (action.equals("falls asleep")) {
                if (guard != null) {
                    guard.sleeps = dt;
                }
            } else if (action.equals("wakes up")) {
                if (guard != null) {
                    long tm = dt.getTime() - guard.sleeps.getTime();
                    int mins = (int)(tm/1000L/60);
                    guard.total += mins;

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(guard.sleeps);
                    int min = cal.get(Calendar.MINUTE);
                    for (int m = 0; m < mins; m++) {
                        int cnt = guard.minutes[(min+m) % 60];
                        guard.minutes[(min+m) % 60] = cnt+1;
                    }
                }
            }
        }
        int res;
        if (mostFreq) {
            guard = null;
            for (Guard gd : guards.values()) {
                if (guard == null || gd.getMostFrequentCount() > guard.getMostFrequentCount()) {
                    guard = gd;
                }
            }
            res = guard.getMostFrequentMinute() * guard.id;

        } else {
            guard = null;
            for (Guard gd : guards.values()) {
                if (guard == null || gd.total > guard.total) {
                    guard = gd;
                }
            }
            res = guard.getMostFrequentMinute() * guard.id;
        }
        return res;
    }
    
}
