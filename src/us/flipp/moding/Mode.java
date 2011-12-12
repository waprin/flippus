package us.flipp.moding;


import android.graphics.Canvas;

public class Mode {

    Mode pendMode;


    static public enum ModeAction {
        NoAction, ChangeMode, Exit
    }

    public ModeAction tick(int timespan) {
        return ModeAction.NoAction;
    }

    public void redraw(Canvas canvas) {

    }

    public Mode teardown() {
        Mode pendMode = this.pendMode;
        this.pendMode = null;
        return pendMode;
    }

}
