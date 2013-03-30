package us.flipp.animation;

import android.graphics.Canvas;

public class Animation {
    protected boolean mStarted = false;
    protected boolean mFinished = false;
    protected int mTickCount = 0;
    protected int mMaxTicks = Integer.MAX_VALUE;

    public void tick() {
        mStarted = true;
        mTickCount++;
        if (mTickCount > mMaxTicks) {
            mFinished = true;
            return;
        }
    }

    public void draw(Canvas c) {
        if (!mStarted || mFinished) {
            return;
        }
    }

    public boolean finished() {
        return mFinished;
    }

    public int getTickCount() {
        return mTickCount;
    }

    public int getMaxTicks() {
        return mMaxTicks;
    }
}
