package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import android.view.ViewDebug;
import us.flipp.animation.GameDrawer;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

import java.util.ArrayList;
import java.util.List;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();
    private List<Player> players;
    private Player currentPlayer;

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
        gameDrawer.updateSize(width, height, boardState);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "Setup");
        gameDrawer = new GameDrawer();
        players = new ArrayList<Player>();
        for (int i = 0; i < 4; i++) {
            Player player = new Player();
            player.setId(i);
            players.add(player);
        }
        boardState = new BoardState(players);
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
                Pair<Player, Integer> pair = gameDrawer.getSuggestedVillage();
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
                int closestPoint = gameDrawer.getClosestPoint(x, y);
                gameDrawer.setSuggestedVillage(new Pair<Player, Integer>(currentPlayer, closestPoint));
                break;
            }
            case BUILDING_TRACK:
            {
                int closestIndex = gameDrawer.getClosestPoint(x, y);
                int nextIndex = gameDrawer.getClosestConnectedPoint(closestIndex, x, y);
                gameDrawer.setSuggestedTrack(currentPlayer, new Pair<Integer, Integer>(closestIndex, nextIndex));
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
