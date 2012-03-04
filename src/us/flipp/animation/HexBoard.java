package us.flipp.animation;

import android.graphics.Point;
import android.util.Log;
import android.util.Pair;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.LogicalBoard;

import java.util.*;

public class HexBoard {
    private static final String TAG = HexBoard.class.getName();

    private Map<Point, List<Integer>> pointHexNeighorMap;
    private ArrayList<Point> sortedPoints;

    private Hexagon[] hexagons;

    private class GamePoint {
        public Point visualPoint;
        public LogicalBoard.LogicalPoint logicalPoint;

        public GamePoint(Point visualPoint, LogicalBoard.LogicalPoint logicalPoint) {
            this.visualPoint = visualPoint;
            this.logicalPoint = logicalPoint;
        }
    }

    public int getClosesPoints(int x, int y) {
        Log.d(TAG, "getClosestPoint: " + x + " " + y);

        Set<Point> points = pointHexNeighorMap.keySet();
        int minDistance = 10000000;
        int i = 0;

        int closest = i;
        for (Point p : points) {
            int xdiff = p.x - x;
            int ydiff = p.y - y;
            xdiff = Math.abs(xdiff);
            ydiff = Math.abs(ydiff);
            int distance = xdiff + ydiff;
            if (distance < minDistance) {
                closest = i;
                minDistance = distance;
            }
            i++;
        }
        Log.d(TAG, "closes point found " + closest);
        return closest;
    }


    public int getClosestConnectedPoint(int closestIndex, int x, int y) {
        Log.d(TAG, "getClosestConnectedPoint: index " + closestIndex + " x " + x + " y " + y);
        Pair<Integer, Integer> intersectionCoords = BoardState.reverseCoords[closestIndex];
        Log.d(TAG, "getClosestConnectedPoint: intersection coords are x: " + x + " y " + y);
        boolean pointsUp = intersectionCoords.second  % 2 == 0;
        ArrayList<Integer> potentialIndices = new ArrayList<Integer>();
        if (pointsUp) {
            if (intersectionCoords.second > 0) {
                potentialIndices.add(BoardState.intersectionCoords[intersectionCoords.first][intersectionCoords.second-1]);
            }
        }
        return 0;
    }

    public Point getPointByIndex(int index) {
        return sortedPoints.get(index);
    }

    public Hexagon[] getHexagons() {
            return this.hexagons;
    }

    public void updateSize(int width, int height, BoardState boardState) {
        hexagons = new Hexagon[BoardState.TOTAL_HEXES];

        int spaceWidthMargin = width / 10;
        int boardLeft = spaceWidthMargin;
        int boardWidth = (width - spaceWidthMargin) - spaceWidthMargin;

        int spaceHeightMargin = height / 10;
        int boardTop = spaceHeightMargin;
        int boardHeight = (height - spaceHeightMargin) - spaceHeightMargin;

        int hexWidth = boardWidth / BoardState.ROW_MAX;
        int hexHeight = boardHeight / BoardState.rowCounts.length;

        int hexLineWidth = hexWidth / 2;
        int hexLineHeight = hexHeight / 3;

        int index = 0;

        pointHexNeighorMap = new TreeMap<Point, List<Integer>>(new Comparator<Point>() {
            @Override
            public int compare(Point point1, Point point2) {
                if (point1.y < point2.y) {
                    return -1;
                } else if (point2.y > point1.y) {
                    return 1;
                } else {
                    if (point1.x < point2.x) {
                        return -1;
                    } else if (point1.x  > point2.x) {
                        return -1;
                    }  else {
                        return 0;
                    }
                }
            }
        });

        for (int i = 0; i < BoardState.rowCounts.length; i++) {
            int count = BoardState.rowCounts[i];
            int diff = BoardState.ROW_MAX - count;
            int outerLeftOffset = boardLeft + (hexLineWidth * diff);
            int topOffset = boardTop + ( i * (hexLineHeight * 2) );
            for (int j = 0; j < count; j++) {
                int leftOffset = outerLeftOffset + (j * (hexLineWidth * 2));
                Hexagon hexagon = new Hexagon(leftOffset, topOffset, hexWidth, hexHeight);
                hexagons[index] = hexagon;
                LogicalBoard.LogicalHex logicalHex = boardState.getLogicalBoard().getRows().get(i).getHex(j);
                for (Point p : hexagon.getPoints()) {
                    List<Integer> l = pointHexNeighorMap.get(p);
                    if (l == null) {
                        l = new ArrayList<Integer>();
                        pointHexNeighorMap.put(p, l);
                    }
                    l.add(index);
                }

                index++;
            }
        }
        sortedPoints = new ArrayList<Point>(pointHexNeighorMap.keySet());
    }

}
