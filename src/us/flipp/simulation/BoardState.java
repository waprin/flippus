package us.flipp.simulation;

import us.flipp.animation.HexBoard;
import us.flipp.moding.Player;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BoardState {

    private static final String TAG = BoardState.class.getName();

    private Map<Player, Map<HexColor, Integer>> cards;

    public enum HexColor {
        BLUE,
        GREEN,
        RED,
        YELLOW,
    }

    static private class HexState {
        public int index;
        public HexColor hexColor;
        public int value;
    }

    public class Intersection {
        public int index;
        public Player player;
        public Intersection(int index, Player player) {
            this.index = index;
            this.player = player;
        }
    }

    public HexColor getColor(int index) {
        return hexStates.get(index).hexColor;
    }

    public int getValue(int index) {
        return hexStates.get(index).value;
    }

    private List<HexState> hexStates;
    private List<Intersection> intersections;

    public List<Intersection> getIntersections() {
        return intersections;
    }

   public void buildVillage(int index, Player player) {
       intersections.add(new Intersection(index, player));
   }

   public BoardState() {
       int totalCells = HexBoard.TOTAL_HEXES;
       hexStates = new ArrayList<HexState>();

       intersections = new ArrayList<Intersection>();

       int length = HexColor.values().length;
       Random rand = new Random();

       for (int i = 0; i < totalCells; i++) {
           HexState hexState = new HexState();
           hexState.hexColor = HexColor.values()[rand.nextInt(length)];
           hexState.index = i;
           hexState.value = rand.nextInt(13);
           hexStates.add(hexState);
       }
   }
}
