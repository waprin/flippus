package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import org.apache.http.cookie.Cookie;
import us.flipp.animation.GameDrawer;
import us.flipp.animation.HexBoard;
import us.flipp.simulation.World;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();
    private Progress progress;
    private World world;

    private HexBoard hexBoard;
    private GameDrawer gameDrawer;

    @Override
    public void setup(Context context) {
        Log.d(TAG, "setting up game world with context " + context);
        this.progress = new Progress(context);
        try {
            this.world = this.progress.getWorld();
        } catch (IOException e) {
            Log.e(TAG, "Caught exception while loading world: " + e.getMessage());
        }
        this.gameDrawer = new GameDrawer();
        this.hexBoard = new HexBoard();
    }

    @Override
    public void handleButton() {
        Log.d(TAG, "handling button press, loading next world");
        try {
            this.world = this.progress.getWorld();
        } catch (IOException e) {
            Log.e(TAG, "Caught exception while loading world " + e.getMessage());
        }
        Log.d(TAG, "author of next world is " + world.getAuthor());
    }

    @Override
    public void handleTap(int x, int y) {
        hexBoard.touchSelected(x, y);

    }

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);

        hexBoard.updateCanvas(canvas);
        gameDrawer.drawBoard(canvas, hexBoard);
    }
}
