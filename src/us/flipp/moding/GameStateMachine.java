package us.flipp.moding;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

public class GameStateMachine {

    private static final String TAG = GameStateMachine.class.getName();

    private Mode mode;
    private Context context;

    public GameStateMachine(Context context) {
        this.context = context;
        this.mode = new ModeGame();
        this.mode.setup(this.context);
    }

    public void HandleTouch(MotionEvent event) {
        Log.d(TAG, "HandleTouch: begin ... ");
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            Log.d(TAG, "HandleTouch: motion up");
            mode.handleTap((int)event.getX(), (int)event.getY());
        }
    }

    public String handleTopButton() {
        return mode.handleTopButton();
    }

    public String handleBottomButton() {
        return mode.handleBottomButton();
    }

    public boolean tick(int timespan) {
        Mode.ModeAction action = mode.tick(timespan);
        if (action == Mode.ModeAction.ChangeMode) {

        }
        return false;
    }

    public void redraw(Canvas canvas) {
        mode.redraw(canvas);
    }

    public void screenChanged(int width, int height) {
        mode.screenChanged(width, height);
    }

    public String getButtonText() {
        return mode.getButtonText();
    }
}
