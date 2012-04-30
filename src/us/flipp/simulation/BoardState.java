package us.flipp.simulation;

import android.util.Pair;
import us.flipp.utility.CircularLinkedList;

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
    public BoardState() {
       logicalBoard = new LogicalBoard();
       this.players = new CircularLinkedList<Player>();

        for (int i = 0; i < 4; i++) {
            Player player = new Player();
            player.setId(i);
            players.add(player);
        }
        currentPlayer = players.getNext();
        firstPlayer = currentPlayer;

       intersections = new ArrayList<Intersection>();
    }

    private CircularLinkedList<Player> players;
    private Player currentPlayer;
    private Player firstPlayer;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isFirstPlayerCurrent() {
        return firstPlayer == currentPlayer;
    }

    public enum Resource {
        BLUE,
        GREEN,
        RED,
        YELLOW,
    }
    public class Intersection {
        public LogicalBoard.LogicalPoint point;
        public Player player;
        public Intersection(LogicalBoard.LogicalPoint point, Player player) {
            this.point = point;
            this.player = player;
        }
    }
    private List<Intersection> intersections;

    public List<Intersection> getIntersections() {
        return intersections;
    }

   public void buildVillage(LogicalBoard.LogicalPoint logicalPoint, Player player) {
       intersections.add(new Intersection(logicalPoint, player));
   }

   public void endTurn() {
       currentPlayer = players.getNext();
   }

}
