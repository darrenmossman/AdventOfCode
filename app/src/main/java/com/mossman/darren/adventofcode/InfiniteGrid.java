package com.mossman.darren.adventofcode;

import java.util.HashMap;

public class InfiniteGrid<T> {

    private HashMap<Integer, HashMap<Integer, T>> data;
    private T defaultValue = null;

    public InfiniteGrid() {
        data = new HashMap<>();
    }

    public InfiniteGrid(T defaultValue) {
        data = new HashMap<>();
        this.defaultValue = defaultValue;
    }

    public T get(int x, int y) {
        HashMap<Integer, T> row = data.get(y);
        if (row == null) {
            return defaultValue;
        } else {
            T res = row.get(x);
            return res == null ? row.get(x) : defaultValue;
        }
    }

    public void put(int x, int y, T value) {
        HashMap<Integer, T> row = data.get(y);
        if (row == null) {
            row = new HashMap<>();
            data.put(y, row);
        }
        row.put(x, value);
    }

    public boolean contains(int x, int y) {
        HashMap<Integer, T> row = data.get(y);
        if (row == null) {
            return false;
        } else {
            return row.containsKey(x);
        }
    }

    public boolean contains(T value) {
        for (HashMap<Integer, T> row: data.values()) {
            if (row.containsValue(value)) {
                return true;
            }
        }
        return false;
    }
}
