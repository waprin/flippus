package us.flipp.moding;


import android.content.Context;
import android.graphics.Canvas;

public class Mode {

    private Mode pendMode;
    private Context context;

    static public enum ModeAction {
        NoAction, ChangeMode, Exit
    }

    public void setup(Context context)
    {
        this.context = context;
    }

    public ModeAction tick(int timespan) {
        return ModeAction.NoAction;
    }

    public void redraw(Canvas canvas) {

    }

    // TODO: erase
    public void handleButton() {

    }

    public Mode teardown() {
        Mode pendMode = this.pendMode;
        this.pendMode = null;
        return pendMode;
    }

}
