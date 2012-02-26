package us.flipp.animation;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {

    private ArrayList<Point> points;

    private int heightStrokeSpan;
    private int widthStrokeSpan;

    public Hexagon(int xOrigin, int yOrigin, int width, int height) {
        widthStrokeSpan = width / 2;
        heightStrokeSpan = height / 3;

        points = new ArrayList<Point>();

        points.add(new Point(xOrigin, yOrigin));
        points.add(new Point(xOrigin + widthStrokeSpan, yOrigin + heightStrokeSpan));
        points.add(new Point(xOrigin + widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)));
        points.add(new Point(xOrigin, yOrigin + (3* heightStrokeSpan)));
        points.add(new Point(xOrigin - widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)));
        points.add(new Point(xOrigin - widthStrokeSpan, yOrigin + heightStrokeSpan));
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getHeightStrokeSpan() {
        return heightStrokeSpan;
    }

    public int getWidthStrokeSpan() {
        return widthStrokeSpan;
    }
}

