package us.flipp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import us.flipp.moding.GameStateMachine;
import android.view.View.OnClickListener;

public class FlippusActivity extends Activity
{
    private GameView gameView;

    private Button topButton;
    private Button bottomButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

      /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);
        gameView = (GameView) findViewById(R.id.game_view_id);

     /*   topButton = (Button) findViewById(R.id.top_button);
        bottomButton = (Button) findViewById(R.id.bottom_button);

        topButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String nextString = gameView.handleTopButton();
                topButton.setText(nextString);
            }
        });
        bottomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String nextString = gameView.handleBottomButton();
                bottomButton.setText(nextString);
            }
        });*/
    }
}
