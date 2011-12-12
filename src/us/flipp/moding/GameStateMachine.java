package us.flipp.moding;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

public class GameStateMachine {

    private static final String TAG = GameStateMachine.class.getName();

    private Mode mode;
    private Context context;

    public GameStateMachine(Context context) {
        this.context = context;
        this.mode = new GameMode();
    }


    public boolean tick(int timespan) {
        Mode.ModeAction action = mode.tick(timespan);
        if (action == Mode.ModeAction.ChangeMode) {

        }
        return false;
    }

    public void redraw(Canvas canvas) {
        Log.e(this.TAG, "TESTING IT GOT HERE");
        mode.redraw(canvas);
    }

}
