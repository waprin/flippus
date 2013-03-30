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

    private Paint mTextPaint;
    private Align mAlign;

    public boolean getVisible() {
        return mVisible;
    }

    public enum Align {
        Center,
        Right
    }

    public Widget(Bitmap bitmap, Rect size) {
        mBounds = size;
        mBackbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
        mFrontbuffer = Bitmap.createBitmap(mBounds.width(),  mBounds.height(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mBackbuffer);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(bitmap, 0, 0, null);
        mVisible = true;

        initializeAssets();
    }

    public void initializeAssets() {
        mTextPaint = new Paint();
        mTextPaint.setARGB(255, 0, 0, 0);
        mTextPaint.setTextSize(mFontSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mAlign = Align.Right;
    }

    public void setTextPaint(Paint paint) {
        mTextPaint = paint;
        mInvalidated = true;
    }

    public void setAlign(Align align) {
        mAlign = align;
        mInvalidated = true;
    }

    public Widget(Rect size) {
        mBounds = size;
        Log.d(TAG, "creating bitmap of size " + mBounds.width() + " and ehight " + mBounds.height());
        mBackbuffer = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
        mFrontbuffer = Bitmap.createBitmap(mBounds.width(),  mBounds.height(), Bitmap.Config.ARGB_8888);
        mVisible = true;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void draw(Canvas canvas, Vector2i offset) {
        if (mVisible) {
            if (mInvalidated) {
                redraw();
                mInvalidated = false;
            }
            canvas.drawBitmap(mFrontbuffer, mBounds.left + offset.x, mBounds.top + offset.y, null);
        }
    }

    private void redraw() {
        Canvas canvas = new Canvas(mFrontbuffer);
        canvas.drawColor(Color.argb(0, 0, 0, 0), PorterDuff.Mode.SRC);
        canvas.drawBitmap(mBackbuffer, 0, 0, null);

        int lineHeight = (int)(mTextPaint.descent() - mTextPaint.ascent());

        int verticalPosition = mBounds.height() / 2 + (int)mTextPaint.descent();
        int horizontalOffset = 3 * mBounds.width() / 4;

        if (mAlign == Align.Right) {
        } else if (mAlign == Align.Center) {
            horizontalOffset = mBounds.width() / 2;
        }
        Log.d(TAG, "drawing text " + mText);
        canvas.drawText(mText, horizontalOffset, verticalPosition, mTextPaint);

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
            canvas.drawRect(borderRect, borderPaint);
        }
       // canvas.drawARGB(0, 0, 0, 0);

    }

    public void handleTap(int x, int y) {
        Log.d(TAG, "handleTap ");
        if (mVisible) {
            if (mBounds.contains(x, y)) {
                mOnClickListener.onClickListener();
            }
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
        mInvalidated = true;
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
        mInvalidated = true;
    }

    public Rect getBounds() {
        return mBounds;
    }

    public void setFontSize(int fontSize) {
        mFontSize = fontSize;
        mInvalidated = true;
    }
}
