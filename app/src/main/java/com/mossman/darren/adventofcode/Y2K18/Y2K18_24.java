package com.mossman.darren.adventofcode.Y2K18;

import com.mossman.darren.adventofcode.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Y2K18_24 extends Y2K18_Puzzle {

    public static void main(String[] args) {

        Y2K18_24 puzzle;
        int i;

        puzzle = new Y2K18_24(true);
        i = puzzle.part1();
        test(i, 5216);

        i = puzzle.part2();
        test(i, 51);

        puzzle = new Y2K18_24(false);
        i = puzzle.part1();
        System.out.printf("day 24: part 1 = %d\n", i);
        //test(i, 23385);

        i = puzzle.part2();
        System.out.printf("day 24: part 2 = %d\n", i);
        //test(i, 2344);

    }

    //--------------------------------------------------------------------------------------------

    private class Group implements Comparable<Group> {
        private int unitCount, hitPoints, attackDamage, initiative;
        private String damageType;
        private ArrayList<String> weakness = null;
        private ArrayList<String> immunity = null;
        private Group target;

        public int compareTo(Group other) {
            if (effectivePower() == other.effectivePower()) {
                return -Integer.compare(initiative, other.initiative);
            } else {
                return -Integer.compare(effectivePower(), other.effectivePower());
            }
        }

        private String[] responseTo(String s, String match) {
            if (s.contains(match)) {
                return s.substring(match.length() + 2).replaceAll(" ", "").split(",");
            } else {
                return null;
            }
        }

        private Group(Matcher m) {
            unitCount = Integer.parseInt(m.group(1));
            hitPoints = Integer.parseInt(m.group(2));

            String s = m.group(3);
            if (!s.isEmpty()) {
                s = s.replaceAll("\\) ", "");
                String[] arr = s.split(";");
                String[] r;
                if (arr.length > 0) {
                    r = responseTo(arr[0], "weak to");
                    if (r != null) weakness = new ArrayList<>(Arrays.asList(r));
                    r = responseTo(arr[0], "immune to");
                    if (r != null) immunity = new ArrayList<>(Arrays.asList(r));
                }
                if (arr.length > 1) {
                    r = responseTo(arr[1], "weak to");
                    if (r != null) weakness = new ArrayList<>(Arrays.asList(r));
                    r = responseTo(arr[1], "immune to");
                    if (r != null) immunity = new ArrayList<>(Arrays.asList(r));
                }
            }
            if (weakness == null) {
                weakness = new ArrayList<>();
            }
            if (immunity == null) {
                immunity = new ArrayList<>();
            }

            attackDamage = Integer.parseInt(m.group(4));
            damageType = m.group(5);
            initiative = Integer.parseInt(m.group(6));
        }

        private int effectivePower() {
            return unitCount * attackDamage;
        }

        private int getAttackDamage(Group target) {
            if (target.immunity.contains(damageType)) {
                return 0;
            }
            else if (target.weakness.contains(damageType)) {
                return effectivePower() * 2;
            }
            else {
                return effectivePower();
            }
        }

        private boolean attack(Group target) {
            int d = getAttackDamage(target);
            int killCount = (int)(d / target.hitPoints);
            if (killCount > target.unitCount) {
                killCount = target.unitCount;
            }
            target.unitCount -= killCount;
            return killCount > 0;
        }

    }

    private ArrayList<Group> immuneSystem;
    private ArrayList<Group> infection;
    private ArrayList<String> input;

    public Y2K18_24(boolean test) {
        this(test, 0);
    }

    private void init() {
        final Pattern p = Pattern.compile("(\\d+) units each with (\\d+) hit points (.*)with an attack that does (\\d+) (.+) damage at initiative (\\d+)");

        immuneSystem = new ArrayList<>();
        infection = new ArrayList<>();
        ArrayList<Group> units = null;

        for (String s: input) {
            if (s.equals("Immune System:")) {
                units = immuneSystem;
                continue;
            }
            else if (s.equals("Infection:")) {
                units = infection;
                continue;
            }
            else if (s.isEmpty()) {
                continue;
            }

            Matcher m = p.matcher(s);
            if (m.matches()) {
                units.add(new Group(m));
            }
        }
    }

    public Y2K18_24(boolean test, int t) {
        String filename = getFilename(test);
        if (t > 0) {
            filename = filename.replace(".txt", "_"+t+".txt");
        }
        input = Utils.readFile(filename);
    }

    public int part1() {
        return part1(0);
    }

    public int part1(int boost) {
        init();

        ArrayList<Group> attackers = new ArrayList<>();
        attackers.addAll(immuneSystem);
        attackers.addAll(infection);

        if (boost > 0) {
            for (Group group: immuneSystem) {
                group.attackDamage += boost;
            }
        }

        boolean killing = true;
        while (killing) {
            killing = false;
            Collections.sort(immuneSystem);
            Collections.sort(infection);

            // target selection - immuneSystem
            ArrayList<Group> targets;
            targets = new ArrayList<>(infection);
            for (Group group: immuneSystem) {
                int maxDamage = 0;
                Group target = null;
                for (Group t: targets) {
                    int d = group.getAttackDamage(t);
                    if (d > maxDamage) {
                        maxDamage = d;
                        target = t;
                    }
                }
                if (maxDamage == 0) {
                    group.target = null;
                } else {
                    group.target = target;
                    targets.remove(target);
                }
            }

            // target selection - infection
            targets = new ArrayList<>(immuneSystem);
            for (Group group: infection) {
                int maxDamage = 0;
                Group target = null;
                for (Group t: targets) {
                    int d = group.getAttackDamage(t);
                    if (d > maxDamage) {
                        maxDamage = d;
                        target = t;
                    }
                }
                if (maxDamage == 0) {
                    group.target = null;
                } else {
                    group.target = target;
                    targets.remove(target);
                }
            }

            attackers.sort(new Comparator<Group>() {
                @Override
                public int compare(Group group, Group t1) {
                    return -Integer.compare(group.initiative, t1.initiative);
                }
            });

            // attacking
            for (Group group: attackers) {
                if (group.target != null && group.unitCount > 0) {
                    if (group.attack(group.target)) {
                        killing = true;
                        if (group.target.unitCount <= 0) {
                            immuneSystem.remove(group.target);
                            infection.remove(group.target);
                        }
                    }
                }
            }
            int immuneSystemCount = 0;
            for (Group group: immuneSystem) immuneSystemCount += group.unitCount;
            int infectionCount = 0;
            for (Group group: infection) infectionCount += group.unitCount;

            if (immuneSystemCount == 0) {
                if (boost > 0) {
                    return 0;
                } else {
                    return infectionCount;
                }
            }
            else if (infectionCount == 0) {
                return immuneSystemCount;
            }
        }
        // Stalemate
        return -1;
    }


    public int part2() {
        int boost = 1;
        while (true) {
            int immuneSystemCount = part1(boost);
            if (immuneSystemCount > 0) {
                return immuneSystemCount;
            }
            boost++;
        }
    }
}
