package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.LogicalBoard;
import us.flipp.simulation.Player;
import us.flipp.simulation.BoardState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    private Pair<Player, LogicalBoard.LogicalPoint> suggestedVillage;
    private Pair<Player, Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>> suggestedTrack;
    private List<Widget> widgets;

    public void setSuggestedVillage(Pair<Player, LogicalBoard.LogicalPoint> suggestedVillage) {
        this.suggestedVillage = suggestedVillage;
    }

    public Pair<Player, LogicalBoard.LogicalPoint> getSuggestedVillage() {
        return this.suggestedVillage;
    }

    private int gameWidth = -254;
    private int gameHeight = -253;

    private Map<BoardState.Resource, Paint> colorPaints;
    private Paint[] playerPaints;

    private boolean down;

    private Paint orangePaint;
    private Paint whitePaint;
    private Paint textPaint;
    private Paint trackPaint;
    private Paint blackPaint;


    public static int[] HEXAGON_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

    private int alphaValue;

    private ModeGame modeGame;


    public GameDrawer(ModeGame modeGame) {
        alphaValue = 255;

        this.modeGame = modeGame;

        blackPaint = new Paint();
        blackPaint.setARGB(255, 0, 0, 0);

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

        colorPaints = new HashMap<BoardState.Resource, Paint>();

        for (int i = 0; i < BoardState.Resource.values().length; i++) {
            Paint paint= new Paint();
            paint.setColor(HEXAGON_COLORS[i]);
            paint.setStyle(Paint.Style.FILL);
            colorPaints.put(BoardState.Resource.values()[i], paint);
        }

        playerPaints = new Paint[ModeGame.MAX_PLAYERS];

        Paint playerPaint = new Paint();
        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setARGB(255, 248, 123, 180);
        playerPaints[0] = playerPaint;
        Paint player2Paint = new Paint(playerPaint);
        player2Paint.setARGB(255, 130, 220, 180);
        playerPaints[1] = player2Paint;
        Paint player3Paint = new Paint(playerPaint);
        player3Paint.setARGB(255, 110, 80, 140);
        playerPaints[2] = player3Paint;
        Paint player4Paint = new Paint(playerPaint);
        player4Paint.setARGB(255, 100, 190, 120);
        playerPaints[3] = player4Paint;


        selected = -1;
    }

    public void updateSize(int width, int height) {
        Paint indexPaint = new Paint();
        indexPaint.setStyle(Paint.Style.FILL);

        collisionBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        collisionCanvas = new Canvas(collisionBitmap);


        hexBoard = new HexBoard();
        hexBoard.updateSize(width, height, modeGame.getBoardState());
        widgets = new LinkedList<Widget>();
        widgets.add(new ResourceWidget(new Rect((int)(width * .1), (int)(height * .75), (int)(width * .4), (int)(height * .95))));
        widgets.add(new ModeWidget(modeGame, new Rect((int)(width * .7), (int)(height * .75), (int)(width * .8), (int)(height * .8))));
        widgets.add(new TurnWidget(modeGame.getBoardState(), new Rect((int)(width * .7), (int)(height * .9), (int)(width * .9), (int)(height * .9))));
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
            LogicalBoard.LogicalPoint logicalPoint = intersection.point;
            HexBoard.GamePoint gamePoint = hexBoard.getGamePoint(logicalPoint);
            canvas.drawCircle((float) gamePoint.visualPoint.x, (float) gamePoint.visualPoint.y, VILLAGE_RADIUS, playerPaints[intersection.player.getId()]);
        }

        if (suggestedVillage != null) {
            HexBoard.GamePoint p = hexBoard.getGamePoint(suggestedVillage.second);
            Paint playerVillagePaint = playerPaints[suggestedVillage.first.getId()];
            playerVillagePaint.setAlpha(alphaValue);
            canvas.drawCircle((float) p.visualPoint.x, (float) p.visualPoint.y, VILLAGE_RADIUS, playerVillagePaint);
            playerVillagePaint.setAlpha(255);
        }

        if (suggestedTrack != null) {
            HexBoard.GamePoint first = hexBoard.getGamePoint(suggestedTrack.second.first);
            List<LogicalBoard.LogicalPoint> neighbors = first.logicalPoint.getConnected();
            List<HexBoard.GamePoint> gamePoints = hexBoard.getGamePoints(neighbors);
            for (HexBoard.GamePoint p : gamePoints) {
                canvas.drawCircle(p.visualPoint.x, p.visualPoint.y, VILLAGE_RADIUS, orangePaint);
            }
            HexBoard.GamePoint second = hexBoard.getGamePoint(suggestedTrack.second.second);
            canvas.drawLine(first.visualPoint.x, first.visualPoint.y, second.visualPoint.x, second.visualPoint.y, trackPaint);
        }
    }

    private void drawIntersectionIndices(Canvas canvas, BoardState boardState) {
        for (LogicalBoard.LogicalPoint p : boardState.getLogicalBoard().getPoints()) {
            HexBoard.GamePoint gp = hexBoard.getGamePoint(p);
            int index = p.getIndex();
            canvas.drawText(String.valueOf(index), gp.visualPoint.x, gp.visualPoint.y, blackPaint);
        }
    }

    public void drawWidgets(Canvas canvas, BoardState boardState) {
        for (Widget widget : widgets) {
            widget.draw(canvas, boardState);
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

    public LogicalBoard.LogicalPoint getClosestPoint(int x, int y) {
        return hexBoard.getClosestPointToCoords(x, y);
    }

    public void setSuggestedTrack(Player player, Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> suggestedTrack) {
        Log.d(TAG, "setSuggestedTrack(): begin");
        Log.d(TAG, "setSuggestedTrack(): first: " + hexBoard.getGamePoint(suggestedTrack.first).visualPoint.x + "," + hexBoard.getGamePoint(suggestedTrack.first).visualPoint.y);// + " second: " + second.visualPoint.x + "," + second.visualPoint.y);
        Log.d(TAG, "setSuggestedTrack(): second: " + hexBoard.getGamePoint(suggestedTrack.second).visualPoint.x + "," + hexBoard.getGamePoint(suggestedTrack.second).visualPoint.y);// + " second: " + second.visualPoint.x + "," + second.visualPoint.y);
        this.suggestedTrack = new Pair<Player, Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>>(player, suggestedTrack);
}


    public LogicalBoard.LogicalPoint getClosestConnectedPoint(LogicalBoard.LogicalPoint logicalPoint, int x, int y) {
        return hexBoard.getClosestConnectedPoint(logicalPoint, x, y);
    }

    public Pair<Player,Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>> getSuggestedTrack() {
        return suggestedTrack;
    }

    public boolean boardContains(int x, int y) {
        return hexBoard.contains(x, y);
    }
}

