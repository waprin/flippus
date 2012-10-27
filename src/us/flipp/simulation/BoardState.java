package us.flipp.simulation;

import android.util.Log;
import android.util.Pair;
import us.flipp.utility.CircularLinkedList;

import java.util.ArrayList;
import java.util.List;

public class BoardState {

    private static final String TAG = BoardState.class.getName();

    public enum GameState {
        BUILD_FIRST_VILLAGE,
        BUILD_FIRST_TRACK,
        BUILD_SECOND_VILLAGE,
        BUILD_SECOND_TRACK,
        NORMAL
    }

    public class Intersection {
        public LogicalBoard.LogicalPoint point;
        public Player player;
        public Intersection(LogicalBoard.LogicalPoint point, Player player) {
            this.point = point;
            this.player = player;
        }
    }

    public class Track {
        public LogicalBoard.LogicalPoint first;
        public LogicalBoard.LogicalPoint second;
        public Player player;

        public Track(LogicalBoard.LogicalPoint first, LogicalBoard.LogicalPoint second, Player player) {
            this.first = first;
            this.second = second;
            this.player = player;
        }
    }

    private GameState gameState;
    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;
    private LogicalBoard logicalBoard;

    private CircularLinkedList<Player> players;
    private Player currentPlayer;
    private Player firstPlayer;
    private List<Intersection> intersections;
    private List<Track> tracks;

    public BoardState() {
       logicalBoard = new LogicalBoard();
       this.players = new CircularLinkedList<Player>();

        for (Player.PlayerID id : Player.PlayerID.values()) {
            Player player = new Player(id);
            players.add(player);
        }
        currentPlayer = players.getNext();
        firstPlayer = currentPlayer;

       intersections = new ArrayList<Intersection>();

        this.gameState = GameState.BUILD_FIRST_VILLAGE;
        this.tracks = new ArrayList<Track>();
    }

    public LogicalBoard getLogicalBoard() {
        return logicalBoard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public boolean villageAtPoint(LogicalBoard.LogicalPoint logicalPoint) {
        for (Intersection intersection : intersections) {
            if (intersection.point.equals(logicalPoint))
                return true;
        }
        return false;
    }

    public boolean isVillageLegal(LogicalBoard.LogicalPoint logicalPoint) {
        Log.d(TAG, "isVillageLegal(): logical point " + logicalPoint.getIndex());
        if (villageAtPoint(logicalPoint)) {
            Log.d(TAG, "isVillageLegal(): village already exists");
        }
        for (LogicalBoard.LogicalPoint connected : logicalPoint.getConnected()) {
            Log.d(TAG, "isVillageLegal(): comparing index " + connected.getIndex());
            if (villageAtPoint(connected)) {
                Log.d(TAG, "isVillageLegal(): false index " + connected.getIndex());
                return false;
            }
        }
        Log.d(TAG, "isVillageLegal(): true");
        return true;
    }

   public void buildVillage(LogicalBoard.LogicalPoint logicalPoint) {
       Log.d(TAG, "buildVillage(): index " + logicalPoint.getIndex());
       if (!isVillageLegal(logicalPoint)) {
           throw new RuntimeException("Trying to build village at illegal point " + logicalPoint.getIndex());
       }
       intersections.add(new Intersection(logicalPoint, currentPlayer));
       if (gameState == GameState.BUILD_SECOND_TRACK) {
           currentPlayer.increaseResourceCount(logicalPoint.getStartingResources());
       }
   }

   public void buildTrack(Pair<LogicalBoard.LogicalPoint , LogicalBoard.LogicalPoint> suggestedTrack) {
        tracks.add(new Track(suggestedTrack.first, suggestedTrack.second, currentPlayer));
   }

   public void endTurn() {
       Log.d(TAG, "endTurn(): begin: current player is "  + currentPlayer.getPlayerID() + " mode is " + gameState.toString());
       if (gameState == BoardState.GameState.BUILD_FIRST_VILLAGE) {
           gameState = GameState.BUILD_FIRST_TRACK;
       } else if (gameState == GameState.BUILD_SECOND_VILLAGE) {
           gameState = GameState.BUILD_SECOND_TRACK;
       } else if (gameState == GameState.BUILD_FIRST_TRACK) {
           currentPlayer = players.getNext();
           gameState = currentPlayer.equals(firstPlayer) ? GameState.BUILD_SECOND_VILLAGE : GameState.BUILD_FIRST_VILLAGE;
       }  else if (gameState == GameState.BUILD_SECOND_TRACK) {
           currentPlayer = players.getNext();
           gameState = currentPlayer.equals(firstPlayer) ? GameState.NORMAL : GameState.BUILD_SECOND_VILLAGE;
       }
       Log.d(TAG, "endTurn(): end: current player is "  + currentPlayer.getPlayerID() + " mode is " + gameState.toString());
   }
}
