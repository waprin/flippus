package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

import java.util.Map;
import java.util.TreeMap;

public class ResourceWidget implements Widget {

    private Rect rect;
    private Paint textPaint;
    private Paint backgroundPaint;
    private Paint smallPaintText;
    private Paint colorPaint;

    private Player player;

    public ResourceWidget(Rect rect, Player player) {

        this.rect = rect;
        this.player = player;

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
            int value = player.getResourceCount(BoardState.Resource.values()[i]);
            canvas.drawText(String.valueOf(value), rect.left, rect.top + 15 + (i * demoHeight), textPaint);
        }
    }
}
