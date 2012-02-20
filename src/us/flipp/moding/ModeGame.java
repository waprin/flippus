package us.flipp.moding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import org.apache.http.cookie.Cookie;
import us.flipp.simulation.World;

import java.io.IOException;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();
    private Progress progress;
    private World world;

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
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        Paint blackRedraw = new Paint();
        blackRedraw.setARGB(255, 0, 0, 0);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), blackRedraw);

        int widthBorder = canvas.getWidth() / 10;
        int heightBorder = canvas.getHeight() / 10;


        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        int halfX = canvas.getWidth() / 2;
        int halfY = canvas.getHeight() / 2;
        canvas.drawRect(new Rect(0, 0, halfX,halfY), paint);
        String author = world.getAuthor() != null ? world.getAuthor() : "Anonymous";
        canvas.drawText(author, 0, 300, paint);
    }
}
