package us.flipp.animation;

import android.graphics.*;
import us.flipp.simulation.World;

import java.util.List;
import java.util.Random;

public class GameDrawer {

    private World world;

    public GameDrawer(World world) {
        this.world = world;
    }

    public void drawBoard(Canvas canvas, HexBoard hexBoard, int[] colors) {
        Hexagon[] hexes = hexBoard.getHexagons();

        assert(hexes.length == colors.length);
        for(int i = 0; i < hexes.length; i++) {
            drawHexagon(canvas, hexes[i], colors[i]);
        }
    }

    private void drawHexagon(Canvas canvas, Hexagon hexagon, int color) {
        Paint fillPaint = new Paint();
        fillPaint.setColor(color);
        fillPaint.setStyle(Paint.Style.FILL);

        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);



        Path path = new Path();
        List<Point> points =  hexagon.getPoints();
        path.moveTo(points.get(0).x, points.get(0).y);

        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.lineTo(points.get(0).x, points.get(0).y);

        canvas.drawPath(path, fillPaint);
        canvas.drawPath(path, strokePaint);
    }
}

