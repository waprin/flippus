package us.flipp.animation;

import android.graphics.*;
import android.util.Log;
import us.flipp.simulation.World;

import java.util.List;
import java.util.Random;

public class GameDrawer {

    private static final String TAG = GameDrawer.class.getName();

    public GameDrawer() {
    }

    public void drawBoard(Canvas canvas, HexBoard hexBoard) {
        Hexagon[] hexes = hexBoard.getHexagons();

        for(int i = 0; i < hexes.length; i++) {
            drawHexagon(canvas, hexBoard, i);
        }
    }

    private void drawHexagon(Canvas canvas, HexBoard hexBoard, int index) {
        Hexagon hexagon = hexBoard.getHexagons()[index];
        boolean selected = hexBoard.getSelected() == index;

        Paint fillPaint = new Paint();
        fillPaint.setColor(hexBoard.getColor(index));
        fillPaint.setStyle(Paint.Style.FILL);

        Paint strokePaint = new Paint();
        strokePaint.setColor(selected ? Color.WHITE : Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3.0f);


        Paint indexPaint = new Paint();
        indexPaint.setAlpha(index);
        indexPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        List<Point> points =  hexagon.getPoints();
        path.moveTo(points.get(0).x, points.get(0).y);

        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.lineTo(points.get(0).x, points.get(0).y);

        hexBoard.getCollisionCanvas().drawPath(path, indexPaint);

        Matrix scaleMatrix = new Matrix();
       // matrix.reset();
        scaleMatrix.setScale(.9f, .9f, hexagon.getCenter().x, hexagon.getCenter().y);


        path.transform(scaleMatrix);

        canvas.drawPath(path, fillPaint);
        canvas.drawPath(path, strokePaint);
    }
}

