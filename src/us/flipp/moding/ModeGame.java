package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.animation.GameDrawer;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.LogicalBoard;
import us.flipp.simulation.Player;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();

    private GameDrawer gameDrawer;
    private BoardState boardState;

    static public final int MAX_PLAYERS = 4;

    @Override
    public ModeAction tick(int timespan) {
        gameDrawer.tick(timespan);
        return ModeAction.NoAction;
    }

    @Override
    public void screenChanged(int width, int height) {
        Log.d(TAG, "Screen changed.");
        boardState.getLogicalBoard().print();
        gameDrawer.updateSize(width, height);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "Setup");
        gameDrawer = new GameDrawer(this);
        boardState = new BoardState();
    }

    @Override
    public String handleBottomButton() {
        Log.d(TAG, "handling bottom button");
        return "";
    }

    @Override
    public String handleTopButton() {
        Log.d(TAG, "handling button press");
        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
                LogicalBoard.LogicalPoint suggestedVillage = gameDrawer.getSuggestedVillage();
                if (suggestedVillage != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(suggestedVillage);
                }
                boardState.endTurn();
                break;
            case BUILD_FIRST_TRACK:
            case BUILD_SECOND_TRACK:
                Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> suggestedTrack = gameDrawer.getSuggestedTrack();
                if (suggestedTrack != null) {
                    boardState.buildTrack(suggestedTrack);
                }
                break;
        }
        return "";
    }

    @Override
    public void handleTap(int x, int y) {
        gameDrawer.handleTap(x, y);
        if (gameDrawer.boardContains(x, y)) {
            handleBoardTap(x, y);
        }

    }

    private void handleBoardTap(int x, int y) {
        Log.d(TAG, "handle tap");
        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
            {
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                gameDrawer.setSuggestedVillage(closestPoint);
                break;
            }
            case BUILD_FIRST_TRACK:
            case BUILD_SECOND_TRACK:
            {
                Log.d(TAG, "handleTap(): Building Track");
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                LogicalBoard.LogicalPoint connectedPoint = gameDrawer.getClosestConnectedPoint(closestPoint, x, y);
                if (closestPoint == connectedPoint) {
                    Log.e(TAG, "handleTap(): somehow a point was detected as connected to itself");
                }
                gameDrawer.setSuggestedTrack(boardState.getCurrentPlayer(), new Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>(closestPoint, connectedPoint));
                break;
            }
        }
    }

    public BoardState getBoardState() {
        return boardState;
    }

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        gameDrawer.draw(canvas, boardState);
    }
}
