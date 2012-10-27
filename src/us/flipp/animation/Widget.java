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
    protected float mFontSize = 16;

    private String mText = "";
    private float mBorderSize = 0.0f;
    private boolean mHighlighted = false;

    private boolean mVisible;
    private OnClickListener mOnClickListener;

    public Widget(Bitmap bitmap, Rect size) {
        mBounds = size;
        mBackbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
        mFrontbuffer = Bitmap.createBitmap(mBounds.width(),  mBounds.height(), Bitmap.Config.ARGB_8888);
        drawBitmap(mBackbuffer, bitmap);
        mVisible = true;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
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
        if (mVisible) {
            if (mInvalidated) {
                redraw();
            }
            canvas.drawBitmap(mFrontbuffer, mBounds.left + offset.x, mBounds.top + offset.y, null);
        }
    }

    private void redraw() {
        Canvas canvas = new Canvas(mFrontbuffer);
/*        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        clearPaint.setARGB(0,0,0,0);
        canvas.drawPaint(clearPaint);
*/
        canvas.drawBitmap(mBackbuffer, 0, 0, null);

        Paint textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setTextSize(mFontSize);
       // textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        textPaint.setTextAlign(Paint.Align.CENTER);

        int lineHeight = (int)(textPaint.descent() - textPaint.ascent());
        int verticalPosition = mBounds.height() / 2 + (int)textPaint.descent();


        canvas.drawText(mText, 3 * mBounds.width() / 4, verticalPosition, textPaint);

        if (mBorderSize > 0) {
            Paint borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(mBorderSize);
            if (mHighlighted) {
                borderPaint.setARGB(255, 255, 255, 255);
            } else {
                borderPaint.setARGB(255, 0, 0, 0);
            }
            Rect borderRect = new Rect(0, 0, mFrontbuffer.getWidth(), mFrontbuffer.getHeight());
//            borderRect.bottom -= 5;
//            borderRect.right -= 5;
            canvas.drawRect(borderRect, borderPaint);
        }
    }

    public void handleTap(int x, int y) {
        Log.d(TAG, "handleTap ");
        if (mBounds.contains(x, y)) {
            mOnClickListener.onClickListener();
        }
    }


    public void tick(int timespan) {

    }

    public void setBorderSize(float borderSize) {
        mBorderSize = borderSize;
    }

    public float getBorderSize() {
        return mBorderSize;
    }

    public void setHighlighted(boolean highlighted) {
        mHighlighted = highlighted;
    }

    public boolean getHighlighted() {
        return mHighlighted;
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

    public void setFontSize(int fontSize) {
        mFontSize = fontSize;
    }
}
