package us.flipp.animation;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import us.flipp.simulation.LogicalBoard;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {

    private static final String TAG = Hexagon.class.getName();

    private ArrayList<HexBoard.GamePoint> points;

    private int xOrigin;
    private int yOrigin;

    private int heightStrokeSpan;
    private int widthStrokeSpan;

    private Path path;
    private LogicalBoard.LogicalHex logicalHex;

    public Hexagon(int xOrigin, int yOrigin, int width, int height, LogicalBoard.LogicalHex logicalHex) {
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;

        widthStrokeSpan = width / 2;
        heightStrokeSpan = height / 3;

        points = new ArrayList<HexBoard.GamePoint>();

        points.add(new HexBoard.GamePoint(new Point(xOrigin, yOrigin), logicalHex.getPoint(0)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin + widthStrokeSpan, yOrigin + heightStrokeSpan), logicalHex.getPoint(1)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin + widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)), logicalHex.getPoint(2)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin, yOrigin + (3 * heightStrokeSpan)), logicalHex.getPoint(3)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin - widthStrokeSpan, yOrigin + (2 * heightStrokeSpan)), logicalHex.getPoint(4)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin - widthStrokeSpan, yOrigin + heightStrokeSpan), logicalHex.getPoint(5)));

        initPath();
    }

    public Path getPath() {
        return path;
    }

    private void initPath() {
        path = new Path();
        List<HexBoard.GamePoint> points =  getPoints();
        path.moveTo(points.get(0).visualPoint.x, points.get(0).visualPoint.y);

        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).visualPoint.x, points.get(i).visualPoint.y);
        }
        path.lineTo(points.get(0).visualPoint.x, points.get(0).visualPoint.y);
    }

    public Point getCenter() {
        int height = heightStrokeSpan * 3;
        int width =  widthStrokeSpan * 2;
        return new Point(xOrigin, yOrigin + (height / 2));
    }

    public List<HexBoard.GamePoint> getPoints() {
        return points;
    }
}

