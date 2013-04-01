package us.flipp.simulation;

import android.util.Log;
import android.util.Pair;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Player {

    private static final String TAG = Player.class.getName();

    public PlayerID getPlayerID() {
        return mPlayerID;
    }

    public enum PlayerID {
        PLAYER_1,
        PLAYER_2,
        PLAYER_3,
        PLAYER_4
    }

    private PlayerID mPlayerID;
    private EnumMap<Resource, Integer> mResources;

    public Player(PlayerID playerID) {
        mPlayerID = playerID;
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

    public void increaseResourceCount(Resource resource, int amount) {
        mResources.put(resource, mResources.get(resource) + amount);
    }

}
