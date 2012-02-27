package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
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
    }

    @Override
    public void handleButton() {
        Log.d(TAG, "handling button press");
        switch (boardState.getMode()) {
            case NONE:
                Log.d(TAG, "switching to board state button");
                boardState.setMode(BoardState.BoardMode.BUILDING);
                break;
            case BUILDING:
                boardState.confirmVillage();
                boardState.setMode(BoardState.BoardMode.NONE);
                break;
        }
    }

    @Override
    public void handleTap(int x, int y) {
        Log.d(TAG, "handle tap");
        switch (boardState.getMode()) {
            case NONE:
                gameDrawer.selectHexagon(x, y);
                break;
            case BUILDING:
                int index = gameDrawer.getPoint(x, y);
                boardState.suggestIntersection(index, currentPlayer);
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
