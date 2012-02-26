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

    private GameDrawer gameDrawer;

    private int screenWidth;
    private int screenHeight;

    @Override
    public void screenChanged(int width, int height) {
        Log.e(TAG, "NICE! Screen changed.");
        screenHeight = height;
        screenWidth = width;
        gameDrawer = new GameDrawer(width, height);
    }

    @Override
    public void setup(Context context) {
        Log.d(TAG, "setting up game world with context " + context);
        this.progress = new Progress(context);
        try {
            this.world = this.progress.getWorld();
        } catch (IOException e) {
            Log.e(TAG, "Caught exception while loading world: " + e.getMessage());
        }
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
        gameDrawer.touchSelected(x, y);
    }

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        gameDrawer.drawBoard(canvas);
    }
}
