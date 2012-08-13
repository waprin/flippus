package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.Log;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

import java.util.Map;
import java.util.TreeMap;

public class ResourceWidget  {

    private static final String TAG = ResourceWidget.class.getName();

    private Paint textPaint;
    private Paint backgroundPaint;
    private Paint smallPaintText;
    private Paint colorPaint;

    public ResourceWidget(int backgroundColor, Rect sizeAndPosition) {
        //super(backgroundColor, sizeAndPosition);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setARGB(255, 60, 210, 170);

        textPaint = new Paint();
        textPaint.setTextSize(16.0f);
        textPaint.setARGB(255, 0, 0, 0);

        smallPaintText = new Paint();
        smallPaintText.setTextSize(12.0f);
        smallPaintText.setARGB(255, 0, 0, 0);

        colorPaint = new Paint();
        colorPaint.setStyle(Paint.Style.FILL);
    }

    public void handleTap(int x, int y) {

    }

    public void tick(int timespan) {

    }
}
