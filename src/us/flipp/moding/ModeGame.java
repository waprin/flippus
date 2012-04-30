package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import android.view.ViewDebug;
import us.flipp.animation.GameDrawer;
import us.flipp.animation.HexBoard;
import us.flipp.animation.Hexagon;
import us.flipp.animation.Widget;
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
    private boolean isBuilding;

    public static final int MAX_PLAYERS = 4;

    private enum GameState {
        SETUP_1,
        SETUP_2,
        IN_GAME,
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
        gameDrawer.updateSize(width, height);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "Setup");
        gameDrawer = new GameDrawer(this);
        boardState = new BoardState();
        gameState = GameState.SETUP_1;
        isBuilding = true;
    }

    @Override
    public String handleBottomButton() {
        Log.d(TAG, "handling bottom button");
        switch (gameState) {
            case BUILDING_TRACK:
                Pair<Player, Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>>  pair = gameDrawer.getSuggestedTrack();
        }
        return "";
    }

    @Override
    public String handleTopButton() {
        Log.d(TAG, "handling button press");
        switch (gameState) {
            case SETUP_1:
            case SETUP_2:
                Pair<Player, LogicalBoard.LogicalPoint> pair = gameDrawer.getSuggestedVillage();
                if (pair != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(pair.second, pair.first);
                    if (gameState == GameState.SETUP_2) {
                        List<LogicalBoard.LogicalHex> adjacentHexes = pair.second.getHexes();
                        for (LogicalBoard.LogicalHex hex : adjacentHexes) {
                            BoardState.Resource resource = hex.getHexState().getResource();
                            int diceValue = hex.getHexState().getDiceValue();
                            boardState.getCurrentPlayer().increaseResourceCount(resource, diceValue);
                        }
                    }
                }
                boardState.endTurn();
                if (boardState.isFirstPlayerCurrent()) {
                    if (gameState == GameState.SETUP_1) {
                        gameState = GameState.SETUP_2;
                    } else if (gameState == GameState.SETUP_2) {
                        gameState = GameState.IN_GAME;
                    }
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
        switch (gameState) {
            case SETUP_1:
            case SETUP_2:
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
        gameDrawer.drawBoard(canvas, boardState);
        gameDrawer.drawWidgets(canvas, boardState);
    }
}
