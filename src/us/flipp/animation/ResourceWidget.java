package us.flipp.animation;

import android.app.PendingIntent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.simulation.BoardState;

public class ResourceWidget {

    private int left;
    private int top;
    private int width;
    private int height;

    private Paint textPaint;
    private Paint backgroundPaint;

    public ResourceWidget(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width  = width;
        this.height = height;

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setARGB(255, 230, 210, 170);

        textPaint = new Paint();
        textPaint.setARGB(255, 255, 255, 255);
    }

    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawRect(new Rect(left, top, left + width, top + height), backgroundPaint);
        canvas.drawText("STUFF GOES HERE", top, left, textPaint);
    }
}
