package us.flipp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import org.apache.http.cookie.Cookie;
import org.w3c.dom.Attr;
import us.flipp.moding.GameStateMachine;

import java.text.AttributedCharacterIterator;
import java.util.concurrent.Semaphore;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameView.class.getName();

    private Context context;
    private GameThread gameThread;
    private GameStateMachine gameStateMachine;
    private Semaphore semaphore;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        requestFocus();
        this.context = context;
        Log.d(TAG, "initializing the game view");
        Thread.dumpStack();
        gameStateMachine = new GameStateMachine(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        this.semaphore = new Semaphore(1);

        setOnTouchListener(mTouchListener);
    }

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            try {
                semaphore.acquire();
                gameStateMachine.HandleTouch(motionEvent);
                semaphore.release();
            } catch (InterruptedException e) {
                Log.e(TAG, "Semaphore interrupted");
            }
            return true;
        }
    };

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(this.TAG, "Surface created");
        gameThread = new GameThread(getHolder(), context, gameStateMachine, semaphore);
        gameThread.start();
    }

    public void buttonPressed() {
        Log.d(TAG, "button pressed");
        try {
            semaphore.acquire();
            gameStateMachine.handleButton();
            semaphore.release();
        } catch (InterruptedException e) {
            Log.e("buttonPressed", "sempahore interrupted");
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameThread.stopRunning();
		boolean retry = true;
		while(retry)
		{
			try
			{
				gameThread.join();
				retry = false;
			} catch(InterruptedException e)
			{
				Log.e("GameView.surfaceDestroyed", "Semaphore interupted");
			}
		}
    }
}
