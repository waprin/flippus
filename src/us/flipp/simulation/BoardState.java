package us.flipp.simulation;

import us.flipp.animation.HexBoard;
import us.flipp.moding.Player;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardState {

    private static final String TAG = BoardState.class.getName();

    public void suggestIntersection(int index, Player currentPlayer) {
        Log.d(TAG, "suggesting intersection " + index + " for player " + currentPlayer);
        intersections.add(new Intersection(index, currentPlayer, true));
    }

    public void confirmVillage() {
        for (Intersection i : intersections) {
            if (i.suggested) {
                i.suggested = false;
            }
        }
    }

    public enum BoardMode {
        NONE,
        BUILDING
    }

    public BoardMode getMode() {
        return mode;
    }

    public void setMode(BoardMode mode) {
        this.mode = mode;
    }

    public BoardMode mode;

    public enum HexColor {
        BLUE,
        GREEN,
        RED,
        YELLOW,
    }

    static private class HexState {
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private int index;

        public HexColor getHexColor() {
            return hexColor;
        }

        public void setHexColor(HexColor hexColor) {
            this.hexColor = hexColor;
        }

        private HexColor hexColor;

        public HexState() {

        }
    }

    public class Intersection {
        public int index;
        public Player player;
        public Intersection(int index, Player player, boolean suggested) {
            this.index = index;
            this.player = player;
            this.suggested = suggested;
        }
        public boolean suggested;
    }

    public HexColor getColor(int index) {
        return hexStates.get(index).getHexColor();
    }

    private List<HexState> hexStates;
    private List<Intersection> intersections;

    public List<Intersection> getIntersections() {
        return intersections;
    }

   public BoardState() {
       this.mode = BoardMode.NONE;
       int totalCells = HexBoard.TOTAL_HEXES;
       hexStates = new ArrayList<HexState>();

       intersections = new ArrayList<Intersection>();

       int length = HexColor.values().length;
       Random rand = new Random();

       for (int i = 0; i < totalCells; i++) {
           HexState hexState = new HexState();
           hexState.setHexColor(HexColor.values()[rand.nextInt(length)]);
           hexState.setIndex(i);
           hexStates.add(hexState);
       }
   }
}
