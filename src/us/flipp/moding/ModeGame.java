package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import android.view.ViewDebug;
import us.flipp.animation.GameDrawer;
import us.flipp.animation.HexBoard;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.LogicalBoard;
import us.flipp.simulation.Player;
import us.flipp.utility.CircularLinkedList;

import java.util.ArrayList;
import java.util.List;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();

    private GameDrawer gameDrawer;
    private BoardState boardState;

    private enum GameState {
        NONE,
        BUILDING,
        BUILDING_TRACK,
    }

    private GameState gameState;

    @Override
    public ModeAction tick(int timespan) {
        gameDrawer.tick();
        return ModeAction.NoAction;
    }

    @Override
    public void screenChanged(int width, int height) {
        Log.d(TAG, "Screen changed.");
        boardState.getLogicalBoard().print();
        gameDrawer.updateSize(width, height, boardState);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "Setup");
        gameDrawer = new GameDrawer();
        boardState = new BoardState();
        gameState = GameState.NONE;
    }

    @Override
    public String handleBottomButton() {
        Log.d(TAG, "handling bottom button");
        switch (gameState) {
            case NONE :
                Log.d(TAG, "switching to road building state");
                gameState = GameState.BUILDING_TRACK;
                break;
            case BUILDING_TRACK:
                Pair<Player, Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>>  pair = gameDrawer.getSuggestedTrack();
        }
        return "";
    }

    @Override
    public String handleTopButton() {
        Log.d(TAG, "handling button press");
        switch (gameState) {
            case NONE:
                Log.d(TAG, "switching to board state button");
                gameState = GameState.BUILDING;
                break;
            case BUILDING:
                Pair<Player, LogicalBoard.LogicalPoint> pair = gameDrawer.getSuggestedVillage();
                if (pair != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(pair.second, pair.first);
                }
                gameState = GameState.NONE;
                break;
        }
        return "";
    }

    @Override
    public void handleTap(int x, int y) {
        Log.d(TAG, "handle tap");
        switch (gameState) {
            case NONE:
                gameDrawer.selectHexagon(x, y);
                break;
            case BUILDING:
            {
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                gameDrawer.setSuggestedVillage(new Pair<Player, LogicalBoard.LogicalPoint>(boardState.getCurrentPlayer(), closestPoint));
                break;
            }
            case BUILDING_TRACK:
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

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        gameDrawer.drawBoard(canvas, boardState);
    }
}
