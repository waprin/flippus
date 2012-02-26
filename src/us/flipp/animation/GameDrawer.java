package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import android.widget.TextView;
import us.flipp.simulation.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameDrawer {

    private static final String TAG = GameDrawer.class.getName();

    private Paint selectedPaint;
    private Paint nonSelectedPaint;

    private HexBoard hexBoard;

    public Bitmap collisionBitmap;
    private Canvas collisionCanvas;

    private int selected;

    private int gameWidth = -254;
    private int gameHeight = -253;

    private Map<Integer, Paint> colorPaints;

    public GameDrawer(int width, int height) {

        gameHeight = height;
        gameWidth = width;

        int colorLength = HexBoard.RANDOM_COLORS.length;

        colorPaints = new HashMap<Integer, Paint>();
        for (int i = 0; i < colorLength; i++) {
            Paint paint= new Paint();
            paint.setColor(HexBoard.RANDOM_COLORS[i]);
            paint.setStyle(Paint.Style.FILL);
            colorPaints.put(HexBoard.RANDOM_COLORS[i], paint);
        }

        nonSelectedPaint = new Paint();
        nonSelectedPaint.setColor(Color.BLACK);
        nonSelectedPaint.setStyle(Paint.Style.STROKE);
        nonSelectedPaint.setStrokeWidth(3.0f);

        selectedPaint = new Paint(nonSelectedPaint);
        selectedPaint.setColor(Color.WHITE);

        Paint indexPaint = new Paint();
        indexPaint.setStyle(Paint.Style.FILL);

        collisionBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        collisionCanvas = new Canvas(collisionBitmap);

        hexBoard = new HexBoard();
        hexBoard.updateSize(width, height);
        Hexagon[] hexes = hexBoard.getHexagons();

         for (int i = 0; i < hexes.length; i++) {
             indexPaint.setAlpha(i);
             drawHexagon(collisionCanvas, hexes[i], new Paint[] {indexPaint}, false);
         }
        selected = -1;
    }

    public void touchSelected(int x, int y) {
        selected = Color.alpha(collisionBitmap.getPixel(x, y));
        Log.w(TAG, "touch selected selected index " + selected);
    }

    public void drawBoard(Canvas canvas) {

        if (gameHeight < 0 || gameWidth < 0) {
            Log.e(TAG, " CHECK ME OUT!!!");
        }

        Hexagon[] hexes = hexBoard.getHexagons();

        for(int i = 0; i < hexes.length; i++) {
            Paint fillPaint = colorPaints.get(hexBoard.getColor(i));
            Paint strokePaint;
            if (i == selected) {
                strokePaint = selectedPaint;
            } else  {
                strokePaint = nonSelectedPaint;
            }
            drawHexagon(canvas, hexes[i], new Paint[]{fillPaint, strokePaint}, true);
        }
    }

    private void drawHexagon(Canvas canvas, Hexagon hexagon, Paint[] paints, boolean scaled) {
        Path path = new Path(hexagon.getPath());
       // matrix.reset();
        if (scaled) {
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(.9f, .9f, hexagon.getCenter().x, hexagon.getCenter().y);
            path.transform(scaleMatrix);
        }
        for(Paint p : paints) {
            canvas.drawPath(path, p);
        }
    }
}

