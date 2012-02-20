package us.flipp.moding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import org.apache.http.cookie.Cookie;
import us.flipp.simulation.World;

import java.io.IOException;
import java.util.Map;

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

        Paint redPaint = new Paint();
        redPaint.setARGB(255, 255, 0, 0);

        Paint bluePaint = new Paint();
        bluePaint.setARGB(255, 0, 0, 255);


        Paint blackRedraw = new Paint();
        blackRedraw.setARGB(255, 0, 0, 0);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), blackRedraw);

        int widthBorder = canvas.getWidth() / 10;
        int heightBorder = canvas.getHeight() / 10;

        int boardWidth = canvas.getWidth() - (2 * widthBorder);
        int boardHeight = canvas.getHeight() - (2 * heightBorder);

        int squareWidth = boardWidth / 10;
        int squareHeight = boardHeight / 10;

        int squareWidthBorder = squareWidth / 10;
        int squareHeightBorder = squareHeight / 10;

        int squareRectWidth = squareWidth - (squareWidthBorder * 2);
        int squareRectHeight = squareHeight - (squareHeightBorder * 2);

        World.Color[][] startBoard = world.getStartBoard();

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                World.Color color = startBoard[x][y];

                Paint paint;

                if (color == World.Color.BLUE) {
                    paint = bluePaint;
                }  else {
                    paint = redPaint;
                }

                int leftOffset = widthBorder;
                leftOffset += squareWidth * x;
                leftOffset += squareWidthBorder;
                int topOffset = heightBorder;
                topOffset += squareHeight * y;
                topOffset += squareHeightBorder;
                canvas.drawRect(new Rect(leftOffset, topOffset, leftOffset + squareRectWidth, topOffset + squareRectHeight), paint);
            }
        }

        int halfX = canvas.getWidth() / 2;
        int halfY = canvas.getHeight() / 2;
        String author = world.getAuthor() != null ? world.getAuthor() : "Anonymous";
        canvas.drawText(author, 0, 300, redPaint);

    }
}
