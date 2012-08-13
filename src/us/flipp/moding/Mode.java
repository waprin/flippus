package us.flipp.moding;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import us.flipp.R;

public class Mode {

    private static final String TAG = Mode.class.getName();

    protected Mode mPendMode;
    protected Context mContext;

    protected int mScreenWidth = 240;
    protected int mScreenHeight = 320;

    Bitmap mBackground;

    static public enum ModeAction {
        NoAction, ChangeMode, Exit
    }

    public void screenChanged(int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
    }


    public void setup(Context context)
    {
        mContext = context;
        mBackground = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.table));
    }

    public String getButtonText() {
        return "Default";
    }

    public ModeAction tick(int timespan) {
        return ModeAction.NoAction;
    }

    public void redraw(Canvas canvas) {
        canvas.drawBitmap(mBackground, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
    }

    public void handleTap(int x, int y) {

    }

    // TODO: erase
    public String handleTopButton() {
        return "";
    }

    public String handleBottomButton() {
        return "";
    }

    public Mode teardown() {
        Mode pendMode = mPendMode;
        mPendMode = null;
        return pendMode;
    }

}
