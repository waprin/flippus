package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.simulation.BoardState;

public class MenuPopoutWidget implements Widget {

    private Rect rect;
    private Paint paint;

    public MenuPopoutWidget(Rect rect) {
        this.rect = rect;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255, 255, 0, 0);
    }

    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawRect(rect, paint);
    }

    @Override
    public void handleTap(int x, int y) {

    }
}
