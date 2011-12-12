package us.flipp.moding;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameMode extends Mode {

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(new Rect(0, 0, 100, 100), paint);
    }

}
