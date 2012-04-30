package us.flipp.simulation;

import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

public class Player {

    private static final String TAG = Player.class.getName();

    private Map<BoardState.Resource, Integer> resources;

    public Integer getResourceCount(BoardState.Resource resource) {
        return resources.get(resource);
    }

    public void increaseResourceCount(BoardState.Resource resource, int count) {
        Integer previous = resources.get(resource);
        if (previous == null) {
            previous = 0;
        }
        Log.d(TAG, "increasing " + this + " resource value " + resource + " from " + previous + " by " + count);
        resources.put(resource, previous + count);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Player() {
        resources = new TreeMap<BoardState.Resource, Integer>();
    }

    public String toString() {
        return "Player " + Integer.toString(id+1);
    }

}
