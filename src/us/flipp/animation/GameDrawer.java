package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import android.widget.TextView;
import us.flipp.simulation.BoardState;
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

    private Map<BoardState.HexColor, Paint> colorPaints;

    private boolean down;

    private Paint orangePaint;
    private Paint blackPaint;


    public static int[] RANDOM_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.GREEN};

    private int alphaValue;


    public GameDrawer() {
        alphaValue = 255;

        orangePaint = new Paint();
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setARGB(255, 200, 100, 100);

        blackPaint = new Paint();
        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setARGB(255, 255, 255, 255);

        nonSelectedPaint = new Paint();
        nonSelectedPaint.setColor(Color.BLACK);
        nonSelectedPaint.setStyle(Paint.Style.STROKE);
        nonSelectedPaint.setStrokeWidth(3.0f);

        selectedPaint = new Paint(nonSelectedPaint);
        selectedPaint.setColor(Color.WHITE);

        colorPaints = new HashMap<BoardState.HexColor, Paint>();

        for (int i = 0; i < BoardState.HexColor.values().length; i++) {
            Paint paint= new Paint();
            paint.setColor(RANDOM_COLORS[i]);
            paint.setStyle(Paint.Style.FILL);
            colorPaints.put(BoardState.HexColor.values()[i], paint);
        }
        selected = -1;
    }

    public void updateSize(int width, int height) {
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


        gameHeight = height;
        gameWidth = width;
    }

    public void selectHexagon(int x, int y) {
            selected = Color.alpha(collisionBitmap.getPixel(x, y));
        Log.w(TAG, "touch selected selected index " + selected);
    }

    public void drawBoard(Canvas canvas, BoardState boardState) {

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), blackPaint);

        if (gameHeight < 0 || gameWidth < 0) {
            Log.e(TAG, " CHECK ME OUT!!!");
        }

        Hexagon[] hexes = hexBoard.getHexagons();

        for(int i = 0; i < hexes.length; i++) {
            Paint fillPaint = colorPaints.get(boardState.getColor(i));
            Paint strokePaint;
            if (i == selected) {
                strokePaint = selectedPaint;
            } else  {
                strokePaint = nonSelectedPaint;
            }
            drawHexagon(canvas, hexes[i], new Paint[]{fillPaint, strokePaint}, true);
        }

        for (BoardState.Intersection intersection : boardState.getIntersections()) {
            if (intersection.suggested) {
                orangePaint.setAlpha(alphaValue);
            } else {
                orangePaint.setAlpha(255);
            }
            Point p = hexBoard.getPointByIndex(intersection.index);
            canvas.drawCircle((float)p.x, (float)p.y, 8.0f, orangePaint);
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

    public void tick() {
        if (down) {
            if (alphaValue > 100) {
                orangePaint.setAlpha(alphaValue - 5);
            } else {
                down = false;
            }
        } else {
            if (alphaValue < 246) {
                orangePaint.setAlpha(alphaValue + 5);
            } else {
                down = true;
            }
        }
    }

    public int getPoint(int x, int y) {
        return hexBoard.getClosesPoint(x, y);
    }
}

