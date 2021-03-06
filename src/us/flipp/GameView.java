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
        gameStateMachine = new GameStateMachine(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        this.semaphore = new Semaphore(1);

        setOnTouchListener(mTouchListener);
    }

    public String getButtonText() {
        return gameStateMachine.getButtonText();
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

    public String handleTopButton() {
        Log.d(TAG, "top button pressed");
        String buttonText = "ERROR 42";
        try {
            semaphore.acquire();
            buttonText = gameStateMachine.handleTopButton();
            semaphore.release();
        } catch (InterruptedException e) {
            Log.e(TAG, "handleTopButton(): sempahore interrupted");
        }
        return buttonText;
    }

    public String handleBottomButton() {
        Log.d(TAG, "bottom button pressed");
        String buttonText = "ERROR 42";
        try {
            semaphore.acquire();
            buttonText = gameStateMachine.handleBottomButton();
            semaphore.release();
        } catch (InterruptedException e) {
            Log.e(TAG, "handleBottomButton(): sempahore interrupted");
        }
        return buttonText;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        try {
            semaphore.acquire();
            gameStateMachine.screenChanged(width, height);
            semaphore.release();
        } catch (InterruptedException e) {
            Log.e(TAG, "surfaceChanged(): semaphore interrupted");
        }
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
