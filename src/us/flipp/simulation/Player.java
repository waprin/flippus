package us.flipp.simulation;

import android.util.Log;
import android.util.Pair;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Player {

    private static final String TAG = Player.class.getName();

    private int id;
    private EnumMap<Resource, Integer> mResources;

    public Player() {
        mResources = new EnumMap<Resource, Integer>(Resource.class);
        for (Resource resource : Resource.values()) {
            mResources.put(resource, 0);
        }
    }

    public EnumMap<Resource, Integer> getResources() {
        return mResources;
    }

    public void increaseResourceCount(EnumMap<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            mResources.put(resource, mResources.get(resource) + resources.get(resource));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Player " + Integer.toString(id+1);
    }

}
