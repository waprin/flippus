package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import us.flipp.simulation.BoardState;


public class Widget {

    private static final String TAG = Widget.class.getName();

    protected Bitmap mBackbuffer;
    protected Bitmap mFrontbuffer;
    protected boolean mInvalidated = true;
    protected Rect mBounds;
    protected Paint mBackgroundPaint;
    protected float mFontSize = 16;

    private String mText = "";

    public Widget(Bitmap bitmap, Rect size) {
        mBounds = size;
        mBackbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
        mFrontbuffer = Bitmap.createBitmap(mBounds.width(),  mBounds.height(), Bitmap.Config.ARGB_8888);

        drawBitmap(mBackbuffer, bitmap);
    }

    public Widget(Bitmap backbuffer, Point position) {
        Log.d(TAG, "Widget(): creating widget at point " + position.toString() + " of dimensions " + backbuffer.getWidth() + " , " + backbuffer.getHeight());
        mBackbuffer = backbuffer;
        mBounds = new Rect(position.x, position.y, position.x + backbuffer.getWidth(), position.y + backbuffer.getHeight());
        mBackbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
        mFrontbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(mBackbuffer);
        c.drawBitmap(backbuffer, 0, 0, null);
    }

    protected void drawBitmap(Bitmap dest, Bitmap src) {
        Canvas canvas = new Canvas(mBackbuffer);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(src, 0, 0, null);
    }

    public void draw(Canvas canvas, Vector2i offset) {
        if (mInvalidated) {
            redraw();
        }
        canvas.drawBitmap(mFrontbuffer, mBounds.left + offset.x, mBounds.top + offset.y, null);
    }

    private void redraw() {
        Canvas canvas = new Canvas(mFrontbuffer);
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        clearPaint.setARGB(0,0,0,0);
        canvas.drawPaint(clearPaint);

        canvas.drawBitmap(mBackbuffer, 0, 0, null);

        Paint textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setTextSize(mFontSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(mText, mBounds.centerX(), mBounds.centerY(), textPaint);
    }

    public void handleTap(int x, int y) {

    }

    public void tick(int timespan) {

    }

    public void setText(String text) {
        mText = text;
        mInvalidated = true;
    }

    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
    }

    public Rect getBounds() {
        return mBounds;
    }
}
