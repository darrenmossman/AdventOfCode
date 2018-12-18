package com.mossman.darren.adventofcode;

import java.util.Collection;
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
            T val = row.get(x);
            return val != null ? val : defaultValue;
        }
    }

    public HashMap<Integer, T> getRow(int y) {
        HashMap<Integer, T> row = data.get(y);
        if (row == null) {
            row = new HashMap<>();
            data.put(y, row);
        }
        return row;
    }

    // optimize puts to same row
    private int py;
    private HashMap<Integer, T> putRow = null;

    public Integer minx = null, maxx = null, miny = null, maxy = null;

    public void put(int x, int y, T value) {
        if (minx == null || x < minx) minx = x;
        if (maxx == null || x > maxx) maxx = x;
        if (miny == null || y < miny) miny = y;
        if (maxy == null || y > maxy) maxy = y;

        HashMap<Integer, T> row;
        if (putRow != null && y == py) {
            row = putRow;
        }
        else {
            row = data.get(y);
            if (row == null) {
                row = new HashMap<>();
                data.put(y, row);
            }
            putRow = row; py = y;
        }
        row.put(x, value);
    }

    public T inc(int x, int y, T value) {
        if (!(value instanceof Integer || value instanceof Long)) {
            throw new RuntimeException("Only Integer types can incremented");
        }
        HashMap<Integer, T> row;
        if (putRow != null && y == py) {
            row = putRow;
        }
        else {
            row = data.get(y);
            if (row == null) {
                row = new HashMap<>();
                data.put(y, row);
            }
            putRow = row; py = py;
        }
        T val = row.get(x);
        val = val != null ? val : defaultValue;
        val = val != null ? val : (T)(Integer)0;

        if (value instanceof Integer) {
            Integer i = (Integer)val;
            i += (Integer)value;
            val = (T)i;
        } else {
            Long l = (Long)val;
            l += (Long)value;
            val = (T)l;
        }
        row.put(x, val);
        return val;
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

    public Collection<HashMap<Integer, T>> values() {
        return data.values();

    }

    public int size() {
        return data.size();
    }

}
