package com.mossman.darren.adventofcode.Y2K20;

import com.mossman.darren.adventofcode.Utils;
import com.mossman.darren.adventofcode.Y2K18.Y2K18_Puzzle;

import java.util.*;
import java.util.stream.Collectors;

public class Y2K20_21 extends Y2K18_Puzzle {

    public static void main(String[] args) {


        Y2K20_21 puzzle = new Y2K20_21(true);
        long i = puzzle.part1();
        System.out.printf("day 21: part 1a = %d\n", i);
        test(i, 5L);

        puzzle = new Y2K20_21(false);
        i = puzzle.part1();
        System.out.printf("day 21: part 1b = %d\n", i);
        test(i, 2428);

        String s = puzzle.part2();
        System.out.printf("day 21: part 2 = %s\n", s);
        test(s, "bjq,jznhvh,klplr,dtvhzt,sbzd,tlgjzx,ctmbr,kqms");

    }

    //--------------------------------------------------------------------------------------------

    private ArrayList<String> input;

    private Set<String> allIngredients;
    private HashMap<String, Integer> ingredientCount;
    private TreeMap<String, Set<String>> allergenHash;

    public Y2K20_21(boolean test) {
        input = Utils.readFile(getFilename(test));
    }

    private void init() {

        allIngredients = new HashSet<>();
        ingredientCount = new HashMap<>();
        allergenHash = new TreeMap<>();

        input.forEach(food -> {
            String[] arr = food.split(" \\(contains ");
            String[] ingredientsArray = arr[0].split(" ");
            Set<String> ingredients = new HashSet<>(Arrays.asList(ingredientsArray));

            allIngredients.addAll(ingredients);
            ingredients.forEach(ingredient -> {
                ingredientCount.put(ingredient, ingredientCount.getOrDefault(ingredient, 0)+1);
            });

            String[] allergensArray;
            if (arr.length > 1) {
                String s = arr[1].substring(0, arr[1].length()-1);
                allergensArray = s.split(", ");
            } else {
                allergensArray = new String[0];
            }
            ArrayList<String> allergens = new ArrayList<>(Arrays.asList(allergensArray));

            allergens.forEach(allergen -> {
                Set<String> newIngredients = new HashSet<>(ingredients);
                if (allergenHash.containsKey(allergen)) {
                    Set<String> priorIngredients = allergenHash.get(allergen);
                    newIngredients.retainAll(priorIngredients);
                }
                allergenHash.put(allergen, newIngredients);
            });
        });
    }

    public long part1() {
        init();

        Set<String> noAllergenIngredients = new HashSet<>(allIngredients);
        allergenHash.values().forEach(noAllergenIngredients::removeAll);

        return noAllergenIngredients.stream()
                .map(ingredient -> ingredientCount.get(ingredient))
                .reduce(0, Integer::sum);
    }

    private void removeKnownIngredients(String allergen, Set<String> ingredients) {
        if (ingredients.size() == 1) {
            allergenHash.forEach((a, i) -> {
                if (!allergen.equals(a) && i.containsAll(ingredients)) {
                    i.removeAll(ingredients);
                    removeKnownIngredients(a, i);
                }
            });
        }
    }

    private void removeKnownIngredients() {
        allergenHash.forEach(this::removeKnownIngredients);
    }

    public String part2() {
        init();

        removeKnownIngredients();

        return allergenHash.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.joining(","));
    }
}
