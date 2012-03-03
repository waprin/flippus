package us.flipp.animation;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {

    private static final String TAG = Hexagon.class.getName();

    private ArrayList<Point> points;

    private int xOrigin;
    private int yOrigin;

    private int heightStrokeSpan;
    private int widthStrokeSpan;

    private Path path;

    public Hexagon(int xOrigin, int yOrigin, int width, int height) {
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;

        widthStrokeSpan = width / 2;
        heightStrokeSpan = height / 3;

        points = new ArrayList<Point>();

        points.add(new Point(xOrigin, yOrigin));
        points.add(new Point(xOrigin + widthStrokeSpan, yOrigin + heightStrokeSpan));
        points.add(new Point(xOrigin + widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)));
        points.add(new Point(xOrigin, yOrigin + (3* heightStrokeSpan)));
        points.add(new Point(xOrigin - widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)));
        points.add(new Point(xOrigin - widthStrokeSpan, yOrigin + heightStrokeSpan));

        initPath();
    }

    public Path getPath() {
        return path;
    }

    private void initPath() {
        path = new Path();
        List<Point> points =  getPoints();
        path.moveTo(points.get(0).x, points.get(0).y);

        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.lineTo(points.get(0).x, points.get(0).y);
    }

    public Point getCenter() {
        int height = heightStrokeSpan * 3;
        int width =  widthStrokeSpan * 2;
        return new Point(xOrigin, yOrigin + (height / 2));
    }

    public List<Point> getPoints() {
        return points;
    }
}

