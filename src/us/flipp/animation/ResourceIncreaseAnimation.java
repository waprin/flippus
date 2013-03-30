package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import us.flipp.utility.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: bill
 * Date: 3/29/13
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceIncreaseAnimation extends Animation {

    private Vector2i mOffset;
    private Paint textPaint;

    public ResourceIncreaseAnimation(Vector2i offset, int seconds) {
        mOffset = offset;

        mMaxTicks = seconds * Constants.TICKS_PER_SECOND;
        textPaint = new TextPaint();
        textPaint.setARGB(255, 255, 255, 255);
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!mStarted || mFinished) {
            return;
        }
        canvas.drawText("+1", mOffset.x, mOffset.y, textPaint);
    }
}
