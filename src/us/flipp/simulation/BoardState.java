package us.flipp.simulation;

import android.util.Log;
import android.util.Pair;
import us.flipp.utility.CircularLinkedList;

import java.util.ArrayList;
import java.util.EnumMap;
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

    private List<ResourceChangeEvent> mResourceChangeEvents;

    private LogicalBoard.LogicalPoint mLastVillage;

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

        mResourceChangeEvents = new ArrayList<ResourceChangeEvent>();
    }

    public void addResourceChangeEvent(ResourceChangeEvent resourceChangeEvent) {
        mResourceChangeEvents.add(resourceChangeEvent);
    }

    private void notifyOfResourceChange(Resource resource, int amount) {
        for (ResourceChangeEvent rce : mResourceChangeEvents) {
            rce.notifyOfResourceChange(resource, amount);
        }
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
               notifyOfResourceChange(resource, 1);
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
