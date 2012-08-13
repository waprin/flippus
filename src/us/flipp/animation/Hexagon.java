package us.flipp.animation;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import us.flipp.simulation.LogicalBoard;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {

    private static final String TAG = Hexagon.class.getName();

    private ArrayList<HexBoard.GamePoint> points;

    private int mXOrigin;
    private int mYOrigin;

    private int mHeightStrokeSpan;
    private int mWidthStrokeSpan;

    private Path mPath;
    private int mWidth;
    private int mHeight;

    public Hexagon(int xOrigin, int yOrigin, int width, int height, LogicalBoard.LogicalHex logicalHex) {
        mXOrigin = xOrigin;
        mYOrigin = yOrigin;

        mWidth = width;
        mHeight = height;

        mWidthStrokeSpan = width / 2;
        mHeightStrokeSpan = height / 3;

        points = new ArrayList<HexBoard.GamePoint>();

        points.add(new HexBoard.GamePoint(new Point(xOrigin, yOrigin), logicalHex.getPoint(0)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin + mWidthStrokeSpan, yOrigin + mHeightStrokeSpan), logicalHex.getPoint(1)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin + mWidthStrokeSpan, yOrigin + (2 * mHeightStrokeSpan)), logicalHex.getPoint(2)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin, yOrigin + (3 * mHeightStrokeSpan)), logicalHex.getPoint(3)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin - mWidthStrokeSpan, yOrigin + (2 * mHeightStrokeSpan)), logicalHex.getPoint(4)));
        points.add(new HexBoard.GamePoint(new Point(xOrigin - mWidthStrokeSpan, yOrigin + mHeightStrokeSpan), logicalHex.getPoint(5)));

        initPath();
    }

    public static Path drawHexagonInRect(Rect rect) {
        int xOrigin = rect.left + rect.width() / 2;
        int yOrigin = rect.top;

        int strokeWidth = rect.width() / 2;
        int strokeHeight = rect.height() / 3;

        Path path = new Path();
        path.moveTo(xOrigin, yOrigin);
        path.lineTo(xOrigin + strokeWidth, yOrigin + strokeHeight);
        path.lineTo(xOrigin + strokeWidth, yOrigin + (2 * strokeHeight));
        path.lineTo(xOrigin, yOrigin + (3 * strokeHeight));
        path.lineTo(xOrigin - strokeWidth, yOrigin + (2 * strokeHeight));
        path.lineTo(xOrigin - strokeWidth, yOrigin + strokeHeight);
        path.lineTo(xOrigin, yOrigin);

        return path;
    }

    public Path getPath() {
        return mPath;
    }

    private void initPath() {
        mPath = new Path();
        List<HexBoard.GamePoint> points =  getPoints();
        mPath.moveTo(points.get(0).visualPoint.x, points.get(0).visualPoint.y);

        for (int i = 1; i < points.size(); i++) {
            mPath.lineTo(points.get(i).visualPoint.x, points.get(i).visualPoint.y);
        }
        mPath.lineTo(points.get(0).visualPoint.x, points.get(0).visualPoint.y);
    }

    public Point getCenter() {
        int height = mHeightStrokeSpan * 3;
        int width =  mWidthStrokeSpan * 2;
        return new Point(mXOrigin, mYOrigin + (height / 2));
    }

    public List<HexBoard.GamePoint> getPoints() {
        return points;
    }

    public Rect getBoundingRect() {
        return new Rect(mXOrigin - (mWidth / 2), mYOrigin, mXOrigin + (mWidth / 2), mYOrigin + mHeight);
    }
}

