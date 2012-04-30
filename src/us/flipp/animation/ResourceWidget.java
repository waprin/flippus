package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.Log;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

import java.util.Map;
import java.util.TreeMap;

public class ResourceWidget implements Widget {

    private static final String TAG = ResourceWidget.class.getName();

    private Rect rect;
    private Paint textPaint;
    private Paint backgroundPaint;
    private Paint smallPaintText;
    private Paint colorPaint;

    private ModeGame modeGame;

    public ResourceWidget(ModeGame modeGame, Rect rect) {

        this.modeGame = modeGame;
        this.rect = rect;

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setARGB(255, 60, 210, 170);

        textPaint = new Paint();
        textPaint.setTextSize(16.0f);
        textPaint.setARGB(255, 0, 0, 0);

        smallPaintText = new Paint();
        smallPaintText.setTextSize(12.0f);
        smallPaintText.setARGB(255, 0, 0, 0);

        colorPaint = new Paint();
        colorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawRect(rect, backgroundPaint);
        canvas.drawText("Resources", rect.left, rect.top, textPaint);
        int demoHeight = rect.height() / GameDrawer.HEXAGON_COLORS.length;
        for (int i = 0; i < GameDrawer.HEXAGON_COLORS.length; i++) {
            colorPaint.setColor(GameDrawer.HEXAGON_COLORS[i]);
            canvas.drawRect(new Rect(rect.left + 30, rect.top + i * demoHeight, rect.left + 60, rect.top + ((i+1)*demoHeight)), colorPaint);
            Log.d(TAG, "draw(): mode game is " + modeGame);
            Log.d(TAG, "draw(): current player is " + modeGame.getBoardState().getCurrentPlayer());
            Log.d(TAG, "draw(): index is " + i);
            Integer value = modeGame.getBoardState().getCurrentPlayer().getResourceCount(BoardState.Resource.values()[i]);
            if (value == null) {
                value = 0;
            }
            canvas.drawText(String.valueOf(value), rect.left, rect.top + 15 + (i * demoHeight), textPaint);
        }
    }

    @Override
    public void handleTap(int x, int y) {

    }
}
