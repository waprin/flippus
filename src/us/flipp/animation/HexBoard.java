package us.flipp.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.nfc.Tag;
import android.util.Log;

import java.util.*;

public class HexBoard {
    private static final String TAG = HexBoard.class.getName();

    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;

    private Map<Point, List<Integer>> pointMaps;

    private Hexagon[] hexagons;

    public int getClosesPoint(int x, int y) {
        Log.d(TAG, "getClosestPoint: " + x + " " + y);

        Set<Point> points = pointMaps.keySet();
        int minDistance = 10000000;
        int i = 0;
        int index = i;
        for (Point p : points) {
            int xdiff = p.x - x;
            int ydiff = p.y - y;
            xdiff = Math.abs(xdiff);
            ydiff = Math.abs(ydiff);
            int distance = xdiff + ydiff;
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
            i++;
        }
        Log.d(TAG, "closes point found " + index);
        return index;
    }

    public Point getPointByIndex(int index) {
        Set<Point> points = pointMaps.keySet();
        int i = 0;
        for (Point p : points) {
            if (i == index) {
                return p;
            }
            i++;
        }
        return null;
    }

    public Hexagon[] getHexagons() {
            return this.hexagons;
    }

    public void updateSize(int width, int height) {

        Log.e(TAG, "update size");

        hexagons = new Hexagon[TOTAL_HEXES];

        int spaceWidthMargin = width / 10;
        int boardLeft = spaceWidthMargin;
        int boardWidth = (width - spaceWidthMargin) - spaceWidthMargin;

        int spaceHeightMargin = height / 10;
        int boardTop = spaceHeightMargin;
        int boardHeight = (height - spaceHeightMargin) - spaceHeightMargin;

        int hexWidth = boardWidth / ROW_MAX;
        int hexHeight = boardHeight / rowCounts.length;

        int hexLineWidth = hexWidth / 2;
        int hexLineHeight = hexHeight / 3;

        int index = 0;

        pointMaps = new HashMap<Point, List<Integer>>();

        for (int i = 0; i < rowCounts.length; i++) {
            int count = rowCounts[i];
            int diff = ROW_MAX - count;
            int outerLeftOffset = boardLeft + (hexLineWidth * diff);
            int topOffset = boardTop + ( i * (hexLineHeight * 2) );
            for (int j = 0; j < count; j++) {
                int leftOffset = outerLeftOffset + (j * (hexLineWidth * 2));
                Hexagon hexagon = new Hexagon(leftOffset, topOffset, hexWidth, hexHeight);
                hexagons[index] = hexagon;

                for (Point p : hexagon.getPoints()) {
                    List<Integer> l = pointMaps.get(p);
                    if (l == null) {
                        l = new ArrayList<Integer>();
                        pointMaps.put(p, l);
                    }
                    l.add(index);
                }

                index++;
            }
        }
    }

}
