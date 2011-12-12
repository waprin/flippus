package us.flipp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import us.flipp.moding.GameStateMachine;

public class FlippusActivity extends Activity
{
    private GameView mGameView;
    private GameStateMachine mGame;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

      /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        */
        mGameView = new GameView(this);
        setContentView(mGameView);
    }
}
