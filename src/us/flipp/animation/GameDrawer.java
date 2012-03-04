package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.simulation.Player;
import us.flipp.simulation.BoardState;

import java.util.HashMap;
import java.util.Map;

public class GameDrawer {

    private static final String TAG = GameDrawer.class.getName();
    private static final float VILLAGE_RADIUS = 8.0f;


    private Paint selectedPaint;
    private Paint nonSelectedPaint;

    private HexBoard hexBoard;

    public Bitmap collisionBitmap;
    private Canvas collisionCanvas;

    private int selected;

    private Pair<Player, Integer> suggestedVillage;
    private Pair<Player, Pair<Integer, Integer>> suggestedTrack;

    public void setSuggestedVillage(Pair<Player, Integer> suggestedVillage) {
        this.suggestedVillage = suggestedVillage;
    }

    public Pair<Player, Integer> getSuggestedVillage() {
        return this.suggestedVillage;
    }

    private int gameWidth = -254;
    private int gameHeight = -253;

    private Map<BoardState.HexColor, Paint> colorPaints;

    private boolean down;

    private Paint orangePaint;
    private Paint whitePaint;
    private Paint textPaint;
    private Paint trackPaint;


    public static int[] RANDOM_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.GREEN};

    private int alphaValue;


    public GameDrawer() {
        alphaValue = 255;

        orangePaint = new Paint();
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setARGB(255, 200, 100, 100);

        whitePaint = new Paint();
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setARGB(255, 255, 255, 255);

        textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setTextAlign(Paint.Align.CENTER);

        trackPaint = new Paint();
        trackPaint.setStrokeWidth(2.0f);
        trackPaint.setARGB(255, 125, 25, 75);


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

    public void updateSize(int width, int height, BoardState boardState) {
        Paint indexPaint = new Paint();
        indexPaint.setStyle(Paint.Style.FILL);

        collisionBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        collisionCanvas = new Canvas(collisionBitmap);

        hexBoard = new HexBoard();
        hexBoard.updateSize(width, height, boardState);
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

    public int getClosestTrack(int x, int y) {
        return 0;
    }

    public void drawBoard(Canvas canvas, BoardState boardState) {

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), whitePaint);

        if (gameHeight < 0 || gameWidth < 0) {
            Log.e(TAG, "drawBoard(): Board being drawn without being initialized.");
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
            int value = boardState.getValue(i);
            drawHexagon(canvas, hexes[i], new Paint[]{fillPaint, strokePaint}, true);
            drawValue(canvas, hexes[i], value);
        }

        for (BoardState.Intersection intersection : boardState.getIntersections()) {
            Point p = hexBoard.getPointByIndex(intersection.index);
            canvas.drawCircle((float)p.x, (float)p.y, VILLAGE_RADIUS, orangePaint);
        }

        if (suggestedVillage != null) {
            Point p = hexBoard.getPointByIndex(suggestedVillage.second);
            orangePaint.setAlpha(alphaValue);
            canvas.drawCircle((float) p.x, (float) p.y, VILLAGE_RADIUS, orangePaint); ;
            orangePaint.setAlpha(255);
        }
        if (suggestedTrack != null) {
            Point first = hexBoard.getPointByIndex(suggestedTrack.second.first);
            Point second = hexBoard.getPointByIndex(suggestedTrack.second.second);
            canvas.drawLine(first.x, first.y, second.x, second.y, orangePaint);
        }
    }

    private void drawValue(Canvas canvas, Hexagon hexagon, int value) {
        String text = String.valueOf(value);
        Point center = hexagon.getCenter();
        canvas.drawText(text, center.x, center.y, textPaint);
    }

    private void drawHexagon(Canvas canvas, Hexagon hexagon, Paint[] paints, boolean scaled) {
        Path path = new Path(hexagon.getPath());
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
                alphaValue -= 5;
            } else {
                down = false;
            }
        } else {
            if (alphaValue < 246) {
                alphaValue += 5;
            } else {
                down = true;
            }
        }
    }

    public int getClosestPoint(int x, int y) {
        return hexBoard.getClosesPoints(x, y);
    }

    public void setSuggestedTrack(Player player, Pair<Integer, Integer> suggestedTrack) {
        this.suggestedTrack = new Pair<Player, Pair<Integer, Integer>>(player, suggestedTrack);
    }

    public int getClosestConnectedPoint(int closestIndex, int x, int y) {
        return hexBoard.getClosestConnectedPoint(closestIndex, x, y);
    }
}

