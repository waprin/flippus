package us.flipp;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import us.flipp.moding.GameStateMachine;
import java.util.concurrent.Semaphore;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameView.class.getName();

    private Context context;
    private GameThread gameThread;
    private GameStateMachine gameStateMachine;
    private Semaphore semaphore;

    public GameView(Context context) {
        super(context);
        requestFocus();
        this.context = context;
        gameStateMachine = new GameStateMachine(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        this.semaphore = new Semaphore(1);
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(this.TAG, "Surface CREATED BITCH");
        gameThread = new GameThread(getHolder(), context, gameStateMachine, semaphore);
        gameThread.start();
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