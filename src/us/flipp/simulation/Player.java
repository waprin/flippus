package us.flipp.simulation;

import java.util.Map;
import java.util.TreeMap;

public class Player {

    private Map<BoardState.HexColor, Integer> cards;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Player() {
        cards = new TreeMap<BoardState.HexColor, Integer>();
    }

}
