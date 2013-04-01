package us.flipp.simulation;

import android.util.Log;
import android.util.Pair;
import us.flipp.utility.CircularLinkedList;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class BoardState {

    private static final String TAG = BoardState.class.getName();

    public enum BoardEvent {
        RESOURCE_UPDATE,
        DICE_ROLL,
    }

    static public interface EventHandler {
        public void notify(BoardEvent be, Object[] args);
    }

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

    private GameState mGameState;
    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;
    private LogicalBoard logicalBoard;

    private CircularLinkedList<Player> players;
    private Player mCurrentPlayer;
    private Player firstPlayer;
    private List<Intersection> mIntersections;
    private List<Track> tracks;

    private LogicalBoard.LogicalPoint mLastVillage;

    private EventHandler mEventHandler;

    public BoardState() {
       logicalBoard = new LogicalBoard();
       this.players = new CircularLinkedList<Player>();

        for (Player.PlayerID id : Player.PlayerID.values()) {
            Player player = new Player(id);
            players.add(player);
        }
        mCurrentPlayer = players.getNext();
        firstPlayer = mCurrentPlayer;

       mIntersections = new ArrayList<Intersection>();

        this.mGameState = GameState.BUILD_FIRST_VILLAGE;
        this.tracks = new ArrayList<Track>();
    }

    public void setEventHandler(EventHandler eventHandler) {
        mEventHandler = eventHandler;
    }

    public int rollDice() {
        Random random = new Random();
        int firstRoll = random.nextInt(6) + 1;
        int secondRoll = random.nextInt(6) + 1;
        int totalRoll = firstRoll + secondRoll;

        for (Intersection intersection : mIntersections) {
            for (LogicalBoard.LogicalHex hex : intersection.point.getHexes()) {
                if (hex.getHexState().getDiceValue() == totalRoll) {
                    intersection.player.increaseResourceCount(hex.getHexState().getResource(), 1);
                    mEventHandler.notify(BoardEvent.RESOURCE_UPDATE, new Object[] { hex.getHexState().getResource(), 1});
                }
            }
        }
        return totalRoll;
    }

    public void startTurn() {
    }

    public LogicalBoard getLogicalBoard() {
        return logicalBoard;
    }

    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }

    public GameState getGameState() {
        return this.mGameState;
    }

    public List<Intersection> getIntersections() {
        return mIntersections;
    }

    public List<Track> getTracks() {
        return tracks;
    }


    public boolean villageAtPoint(LogicalBoard.LogicalPoint logicalPoint) {
        for (Intersection intersection : mIntersections) {
            if (intersection.point.equals(logicalPoint))
                return true;
        }
        return false;
    }

    public boolean isVillageLegal(LogicalBoard.LogicalPoint logicalPoint) {
        Log.d(TAG, "isVillageLegal(): logical point " + logicalPoint.getIndex());
        if (villageAtPoint(logicalPoint)) {
            Log.d(TAG, "isVillageLegal(): village already exists");
            return false;
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
       mIntersections.add(new Intersection(logicalPoint, mCurrentPlayer));
       mLastVillage = logicalPoint;
       if (mGameState == GameState.BUILD_SECOND_VILLAGE) {
           mCurrentPlayer.increaseResourceCount(logicalPoint.getStartingResources());
           for (Resource resource : logicalPoint.getStartingResources().keySet()) {
               Log.d(TAG, "buildVillage(): notifying of increase of for resource " + resource.toString());
               mEventHandler.notify(BoardEvent.RESOURCE_UPDATE, new Object[] { resource, 1});
           }
       }
   }

    public boolean isLegalTrack(LogicalBoard.LogicalPoint closestPoint, LogicalBoard.LogicalPoint connectedPoint) {
        return closestPoint.equals(mLastVillage) || connectedPoint.equals(mLastVillage);
    }


    public void buildTrack(Pair<LogicalBoard.LogicalPoint , LogicalBoard.LogicalPoint> suggestedTrack) {
        tracks.add(new Track(suggestedTrack.first, suggestedTrack.second, mCurrentPlayer));
   }

   public void endTurn() {
       Log.d(TAG, "endTurn(): begin: current player is "  + mCurrentPlayer.getPlayerID() + " mode is " + mGameState.toString());
       if (mGameState == BoardState.GameState.BUILD_FIRST_VILLAGE) {
           mGameState = GameState.BUILD_FIRST_TRACK;
       } else if (mGameState == GameState.BUILD_SECOND_VILLAGE) {
           mGameState = GameState.BUILD_SECOND_TRACK;
       } else if (mGameState == GameState.BUILD_FIRST_TRACK) {
           mCurrentPlayer = players.getNext();
           mGameState = mCurrentPlayer.equals(firstPlayer) ? GameState.BUILD_SECOND_VILLAGE : GameState.BUILD_FIRST_VILLAGE;
       }  else if (mGameState == GameState.BUILD_SECOND_TRACK) {
           mCurrentPlayer = players.getNext();
           mGameState = mCurrentPlayer.equals(firstPlayer) ? GameState.NORMAL : GameState.BUILD_SECOND_VILLAGE;
       }
       Log.d(TAG, "endTurn(): end: current player is "  + mCurrentPlayer.getPlayerID() + " mode is " + mGameState.toString());
   }
}
