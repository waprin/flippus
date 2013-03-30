package us.flipp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import us.flipp.moding.GameStateMachine;
import us.flipp.utility.Constants;

import java.util.concurrent.Semaphore;
import java.util.logging.Handler;

public class GameThread extends Thread {

    private static final String TAG = GameThread.class.getName();

    private SurfaceHolder surfaceHolder;
    private boolean running = true;
    private GameStateMachine game;
    private Semaphore semaphore;

    public GameThread(SurfaceHolder surfaceHolder, Context context, GameStateMachine game, Semaphore semaphore) {
        this.surfaceHolder = surfaceHolder;
        this.semaphore = semaphore;
        this.game = game;
    }

    public void stopRunning() {
        this.running = false;
    }

    @Override
    public void run() {
        Log.d(this.TAG, "Hmm game started at least");

        while (running) {
            long update_start = System.nanoTime();
            try {
                this.semaphore.acquire();
                this.game.tick(Constants.TICK_PERIOD);
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        if (canvas != null) {
                            this.game.redraw(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                this.semaphore.release();
            } catch (InterruptedException e) {

            }

            long runningTime = System.nanoTime() - update_start;
            long numeratorTime = Constants.TICK_PERIOD * 1000000L;
            int sleepTime = (int)((numeratorTime - runningTime) / 1000000L);
           if(sleepTime > 0)
           {
                   try {
                           sleep(sleepTime);
                   } catch (InterruptedException e) {
                           // This basically means that the thread has been interrupted, so it will
                           // already have already been stopped
                   }
           }
        }
    }

}
