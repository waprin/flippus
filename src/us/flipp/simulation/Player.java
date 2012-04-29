package us.flipp.simulation;

import java.util.Map;
import java.util.TreeMap;

public class Player {

    private Map<BoardState.Resource, Integer> resources;

    public int getResourceCount(BoardState.Resource resource) {
        return resources.get(resource);
    }

    public void increaseResourceCount(BoardState.Resource resource, int count) {
        resources.put(resource, resources.get(resource) + count);
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
