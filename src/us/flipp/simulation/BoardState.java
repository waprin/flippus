package us.flipp.simulation;

import android.util.Pair;
import org.apache.commons.logging.Log;
import us.flipp.animation.HexBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardState {

    private static final String TAG = BoardState.class.getName();

    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;
    public static int [] subRowCounts =  {3, 4, 4, 5, 5, 6, 6, 5, 5, 4, 4, 3};
    public static int totalIntersections;
    public static int[][] intersectionCoords;
    public static Pair<Integer, Integer>[] reverseCoords;

    private LogicalBoard logicalBoard;

    public LogicalBoard getLogicalBoard() {
        return logicalBoard;
    }
/*
    static {
        int intersectionIndex = 0;
        intersectionCoords = new int[subRowCounts.length][];
        for (int i = 0; i < subRowCounts.length; i++) {
            intersectionCoords[i] = new int[subRowCounts[i]];
            for (int j = 0; j < subRowCounts[i]; j++) {
                intersectionCoords[i][j] = intersectionIndex;
                reverseCoords[intersectionIndex] = new Pair<Integer, Integer>(i, j);
                intersectionIndex++;
            }
        }
        totalIntersections = intersectionIndex;
    }
  */
    public BoardState(List<Player> players) {
       logicalBoard = new LogicalBoard();
       this.players = players;

       int totalCells = TOTAL_HEXES;
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
       this.gamePhase = GamePhase.INITIAL_VILLAGE;
    }

    private List<Player> players;

    public enum HexColor {
        BLUE,
        GREEN,
        RED,
        YELLOW,
    }

    public enum GamePhase {
        INITIAL_VILLAGE,
        SECOND_VILLAGE
    }

    private GamePhase gamePhase;

    static private class HexState {
        public int index;
        public HexColor hexColor;
        public int value;
    }

    public class Intersection {
        public LogicalBoard.LogicalPoint point;
        public Player player;
        public Intersection(LogicalBoard.LogicalPoint point, Player player) {
            this.point = point;
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

   public void buildVillage(LogicalBoard.LogicalPoint logicalPoint, Player player) {
       intersections.add(new Intersection(logicalPoint, player));
   }

}
