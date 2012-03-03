package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.animation.GameDrawer;
import us.flipp.simulation.BoardState;

import java.util.List;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();
    private Progress progress;
    private List<Point> buildings;
    private Player currentPlayer;

    private GameDrawer gameDrawer;
    private BoardState boardState;

    private enum GameState {
        NONE,
        BUILDING
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
        gameDrawer.updateSize(width, height);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "Setup");
        boardState = new BoardState();
        gameDrawer = new GameDrawer();
        currentPlayer = new Player();
        currentPlayer.setId(0);
        gameState = GameState.NONE;
    }

    @Override
    public void handleButton() {
        Log.d(TAG, "handling button press");
        switch (gameState) {
            case NONE:
                Log.d(TAG, "switching to board state button");
                gameState = GameState.BUILDING;
                break;
            case BUILDING:
                Pair<Player, Integer> pair = gameDrawer.getSuggestedVillage();
                if (pair != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(pair.second, pair.first);
                }
                gameState = GameState.NONE;
                break;
        }
    }

    @Override
    public void handleTap(int x, int y) {
        Log.d(TAG, "handle tap");
        switch (gameState) {
            case NONE:
                gameDrawer.selectHexagon(x, y);
                break;
            case BUILDING:
                int index = gameDrawer.getPoint(x, y);
                gameDrawer.setSuggestedVillage(new Pair<Player, Integer>(currentPlayer, index));
                break;
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
