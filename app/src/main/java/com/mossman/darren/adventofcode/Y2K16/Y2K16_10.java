package com.mossman.darren.adventofcode.Y2K16;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K16_10 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K16_10 puzzle = new Y2K16_10(true);
        int i = puzzle.run(false, 2, 5);
        System.out.printf("day 10: part 1 test = %d\n", i);
        test(i, 2);

        puzzle = new Y2K16_10(false);
        i = puzzle.run(false, 17, 61);
        System.out.printf("day 10: part 1 = %d\n", i);
        test(i, 141);

        i = puzzle.run(true, 17, 61);
        System.out.printf("day 10: part 2 = %d\n", i);
        test(i, 1209);
    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;
    HashMap<Integer, Bot> bots = new HashMap<>();
    HashMap<Integer, Integer> output = new HashMap<>();


    public Y2K16_10(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private class Bot {
        private int b, bot_low = -1, bot_high = -1, out_low = -1, out_high = -1;
        private ArrayList<Integer> chips = new ArrayList<>();
        private void init(int bot_low, int bot_high, int out_low, int out_high) {
            this.bot_low = bot_low;
            this.bot_high = bot_high;
            this.out_low = out_low;
            this.out_high = out_high;
        }
        private Bot(int b, int bot_low, int bot_high, int out_low, int out_high) {
            this.b = b;
            init(bot_low, bot_high, out_low, out_high);
        }
        private Bot(int b, int v) {
            this.b = b;
            chips.add(v);
        }
        private void receive(int v) {
            chips.add(v);
            Collections.sort(chips);
        }
        private boolean proceed(int tl, int th) {
            if (chips.size() != 2) return false;
            int l = chips.get(0);
            int h = chips.get(1);
            Bot bot;
            if (bot_low != -1) {
                bot = bots.get(bot_low);
                bot.receive(l);
            } else if (out_low != -1) {
                output.put(out_low, l);
            }
            if (bot_high != -1) {
                bot = bots.get(bot_high);
                bot.receive(h);
            } else if (out_high != -1) {
                output.put(out_high, h);
            }
            chips.clear();
            return (l == tl && h == th);
        }
    }

    public int run(boolean part2, int tl, int th) {


        final Pattern pGives = Pattern.compile("bot (\\d+) gives low to (\\S+) (\\d+) and high to (\\S+) (\\d+)");
        final Pattern pValue = Pattern.compile("value (\\d+) goes to bot (\\d+)");

        for (String s : input) {

            if (s.startsWith("value")) {
                Matcher m = pValue.matcher(s);
                if (m.matches()) {
                    int v = Integer.parseInt(m.group(1));
                    int b = Integer.parseInt(m.group(2));
                    Bot bot = bots.get(b);
                    if (bot == null) {
                        bot = new Bot(b, v);
                        bots.put(b, bot);
                    }
                    else {
                        bot.receive(v);
                    }
                }
            }
            else {
                Matcher m = pGives.matcher(s);
                if (m.matches()) {
                    int b = Integer.parseInt(m.group(1));
                    String ls = m.group(2);
                    int low = Integer.parseInt(m.group(3));
                    int bot_low = -1, out_low = -1;
                    if (ls.equals("bot")) {
                        bot_low = low;
                    }
                    else {
                        out_low = low;
                    }
                    String hs = m.group(4);
                    int high = Integer.parseInt((m.group(5)));
                    int bot_high = -1, out_high = -1;
                    if (hs.equals("bot")) {
                        bot_high = high;
                    }
                    else {
                        out_high = high;
                    }
                    Bot bot = bots.get(b);
                    if (bot == null) {
                        bot = new Bot(b, bot_low, bot_high, out_low, out_high);
                        bots.put(b, bot);
                    }
                    else {
                        bot.init(bot_low, bot_high, out_low, out_high);
                    }
                }
            }
        }

        Bot responsible = null;
        boolean proceeded = true;
        while (proceeded) {
            proceeded = false;
            for (Bot bot : bots.values()) {
                if (bot.chips.size() == 2) proceeded = true;
                if (bot.proceed(tl, th)) {
                    responsible = bot;
                }
                if (proceeded) break;
            }
        }
        if (part2) {
            return output.get(0) * output.get(1) * output.get(2);
        }
        else {
            if (responsible != null) return responsible.b;
            return -1;
        }
    }
}
