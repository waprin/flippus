package us.flipp.animation;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.R;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.LogicalBoard;
import us.flipp.simulation.Player;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Resource;

import java.util.*;

public class GameDrawer {

    private static final String TAG = GameDrawer.class.getName();
    private static final float VILLAGE_RADIUS = 8.0f;

    private Paint nonSelectedPaint;

    private HexBoard hexBoard;

    private LogicalBoard.LogicalPoint suggestedVillage;
    private Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> suggestedTrack;
    private List<Widget> widgets;

    private int gameWidth = -254;
    private int gameHeight = -253;

    private EnumMap<Player.PlayerID, Paint> mPlayerPaints;

    private boolean down;

    private Paint textPaint;
    private Paint trackPaint;

    private int alphaValue;

    private EnumMap<Resource, Bitmap> mResourceBitmaps;

    public GameDrawer(Context context, EnumMap<Resource, Bitmap> resourceBitmaps) {
        alphaValue = 255;

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

        mResourceBitmaps = resourceBitmaps;

        mPlayerPaints = new EnumMap<Player.PlayerID, Paint>(Player.PlayerID.class);

        Paint playerPaint = new Paint();
        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setColor(context.getResources().getInteger(R.integer.player_1_color));
        mPlayerPaints.put(Player.PlayerID.PLAYER_1, playerPaint);
        Paint player2Paint = new Paint(playerPaint);
        player2Paint.setColor(context.getResources().getInteger(R.integer.player_2_color));
        mPlayerPaints.put(Player.PlayerID.PLAYER_2, player2Paint);
        Paint player3Paint = new Paint(playerPaint);
        player3Paint.setColor(context.getResources().getInteger(R.integer.player_3_color));
        mPlayerPaints.put(Player.PlayerID.PLAYER_3, player3Paint);
        Paint player4Paint = new Paint(playerPaint);
        player4Paint.setColor(context.getResources().getInteger(R.integer.player_4_color));
        mPlayerPaints.put(Player.PlayerID.PLAYER_4, player4Paint);
    }

    public void setSuggestedVillage(LogicalBoard.LogicalPoint suggestedVillage) {
        this.suggestedVillage = suggestedVillage;
    }

    public LogicalBoard.LogicalPoint getSuggestedVillage() {
        return this.suggestedVillage;
    }


    public void updateSize(int width, int height, BoardState boardState) {
        hexBoard = new HexBoard();
        Rect hexRect = new Rect(0, (int)(height*.1), width, (int)(height * .7));
        hexBoard.updateSize(hexRect, boardState);

        widgets = new LinkedList<Widget>();

        gameHeight = height;
        gameWidth = width;
    }

    public void drawBoard(Canvas canvas, BoardState boardState) {
        //nSelectedPaint.setARGB(255, 0, 128, 200);
        //SelectedPaint.set
        Paint waterPaint = new Paint();
        waterPaint.setARGB(255, 20, 150, 215);
        waterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //canvas.drawRect(hexBoard.getRect(), waterPaint);
        canvas.drawRect(hexBoard.getRect(), nonSelectedPaint);
        //canvas.drawRect(hexBoard.getInnerRect(), nonSelectedPaint);

        if (gameHeight < 0 || gameWidth < 0) {
            Log.e(TAG, "drawBoard(): Board being drawn without being initialized.");
        }

        Hexagon[] hexes = hexBoard.getHexagons();

        for(int i = 0; i < hexes.length; i++) {
            LogicalBoard.LogicalHex logicalHex = boardState.getLogicalBoard().getHexByIndex(i);
            drawHexagon(canvas, hexes[i], logicalHex);

            int value = boardState.getLogicalBoard().getHexByIndex(i).getHexState().getDiceValue();
            //drawValue(canvas, hexes[i], value);
        }

        for (BoardState.Intersection intersection : boardState.getIntersections()) {
            LogicalBoard.LogicalPoint logicalPoint = intersection.point;
            HexBoard.GamePoint gamePoint = hexBoard.getGamePoint(logicalPoint);
            canvas.drawCircle((float) gamePoint.visualPoint.x, (float) gamePoint.visualPoint.y, VILLAGE_RADIUS, mPlayerPaints.get(intersection.player.getPlayerID()));
//            Log.d(TAG, "drawing game point at " + gamePoint.logicalPoint.getIndex() + " x is " + gamePoint.visualPoint.x + " y is " + gamePoint.visualPoint.y);
        }

        if (suggestedVillage != null) {
            HexBoard.GamePoint p = hexBoard.getGamePoint(suggestedVillage);
            //Log.d(TAG, "suggested drawing suggested at " + p.logicalPoint.getIndex() + " x is " + p.visualPoint.x + " y is " + p.visualPoint.y);
            Paint playerVillagePaint = mPlayerPaints.get(boardState.getCurrentPlayer().getPlayerID());
            playerVillagePaint.setAlpha(alphaValue);
            canvas.drawCircle((float) p.visualPoint.x, (float) p.visualPoint.y, VILLAGE_RADIUS, playerVillagePaint);
            playerVillagePaint.setAlpha(255);
        }

        if (suggestedTrack != null) {
            HexBoard.GamePoint first = hexBoard.getGamePoint(suggestedTrack.first);
            HexBoard.GamePoint second = hexBoard.getGamePoint(suggestedTrack.second);
            canvas.drawLine(first.visualPoint.x, first.visualPoint.y, second.visualPoint.x, second.visualPoint.y, trackPaint);
        }
    }

    private void drawHexagon(Canvas canvas, Hexagon hexagon, LogicalBoard.LogicalHex logicalHex) {
        Path path = hexagon.getPath();
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(.9f, .9f, hexagon.getCenter().x, hexagon.getCenter().y);
        Path scaledPath = new Path(path);
        scaledPath.transform(scaleMatrix);
        canvas.clipPath(scaledPath);
        Resource resource = logicalHex.getHexState().getResource();
        canvas.drawBitmap(mResourceBitmaps.get(resource), null, hexagon.getBoundingRect(), null);
        canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

        Paint borderPaint = new Paint();
        borderPaint.setARGB(255, 0, 0, 0);
        borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3.0f);
        canvas.drawPath(path, borderPaint);

        // Draw text circle and text
        borderPaint.setStrokeWidth(.5f);
        borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(hexagon.getBoundingRect().centerX(), hexagon.getBoundingRect().centerY(), 10.0f, borderPaint);

        Paint whitePaint = new Paint();
        whitePaint.setARGB(255, 255, 255, 255);
        canvas.drawCircle(hexagon.getBoundingRect().centerX(), hexagon.getBoundingRect().centerY(), 9.0f, whitePaint);


        String text = String.valueOf(logicalHex.getHexState().getDiceValue());
        Point center = hexagon.getCenter();
        textPaint.setAntiAlias(true);
        canvas.drawText(text, center.x, center.y+5, textPaint);
    }

    public void tick(int timespan) {
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
        this.suggestedTrack = suggestedTrack;
    }


    public LogicalBoard.LogicalPoint getClosestConnectedPoint(LogicalBoard.LogicalPoint logicalPoint, int x, int y) {
        return hexBoard.getClosestConnectedPoint(logicalPoint, x, y);
    }

    public Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> getSuggestedTrack() {
        return suggestedTrack;
    }

    public boolean boardContains(int x, int y) {
        return hexBoard.contains(x, y);
    }

    public void handleTap(int x, int y) {
        Log.d(TAG, "handleTap(): begin ... x " + x + " y " + y);
    }

    public void draw(Canvas canvas, BoardState boardState) {
        drawBoard(canvas, boardState);
    }
}

