package us.flipp.animation;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.LogicalBoard;

import java.util.*;

public class HexBoard {
    private static final String TAG = HexBoard.class.getName();

    private List<GamePoint> allPoints;

    private Hexagon[] hexagons;
    private Rect rect;

    public static class GamePoint {
        public Point visualPoint;
        public LogicalBoard.LogicalPoint logicalPoint;

        public GamePoint(Point visualPoint, LogicalBoard.LogicalPoint logicalPoint) {
            this.visualPoint = visualPoint;
            this.logicalPoint = logicalPoint;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) return true;
            if ( !(that instanceof GamePoint)) return  false;
            GamePoint rhs = (GamePoint) that;
            return (rhs.visualPoint.x == visualPoint.x && rhs.visualPoint.y == visualPoint.y);
        }

        @Override
        public int hashCode() {
            return (visualPoint.x * 100000) + visualPoint.y;
        }
    }



    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }


    public List<GamePoint> getGamePoints(List<LogicalBoard.LogicalPoint> logicalPoints) {
        List<GamePoint> gamePoints = new LinkedList<GamePoint>();
        for (LogicalBoard.LogicalPoint p : logicalPoints) {
            gamePoints.add(getGamePoint(p));
        }
        return gamePoints;
    }

    public GamePoint getGamePoint(LogicalBoard.LogicalPoint logicalPoint) {
        for (GamePoint gamePoint : allPoints) {
            if (gamePoint.logicalPoint.equals(logicalPoint)) {
                return gamePoint;
            }
        }
        Log.e(TAG, "did not find logical point ");
        return null;
    }


    public Rect getRect() {
        return this.rect;
    }

    private LogicalBoard.LogicalPoint getClosesPoint(int x, int y, Collection<GamePoint> gamePoints) {
        Log.d(TAG, "getClosestPoint: " + x + " " + y);

        int minDistance = 10000000;
        LogicalBoard.LogicalPoint logicalPoint = null;
        for (GamePoint point : gamePoints) {
            Point p = point.visualPoint;
            int xdiff = p.x - x;
            int ydiff = p.y - y;
            xdiff = Math.abs(xdiff);
            ydiff = Math.abs(ydiff);
            int distance = xdiff + ydiff;
            if (distance < minDistance) {
                logicalPoint = point.logicalPoint;
                minDistance = distance;
            }
        }
        return logicalPoint;
    }

    public LogicalBoard.LogicalPoint getClosestPointToCoords(int x, int y) {
        return getClosesPoint(x, y, allPoints);
    }


    public LogicalBoard.LogicalPoint getClosestConnectedPoint(LogicalBoard.LogicalPoint logicalPoint, int x, int y) {
        List<LogicalBoard.LogicalPoint> connectedPoints = logicalPoint.getConnected();
        List<GamePoint> localGamePoints = new ArrayList<GamePoint>();
        for (LogicalBoard.LogicalPoint p : connectedPoints) {
            GamePoint gamePoint = getGamePoint(p);
            localGamePoints.add(gamePoint);
        }
        return getClosesPoint(x, y, localGamePoints);
    }

    public Hexagon[] getHexagons() {
            return this.hexagons;
    }

    public void updateSize(Rect rect, BoardState boardState) {
        this.rect = rect;
        hexagons = new Hexagon[BoardState.TOTAL_HEXES];
        allPoints = new ArrayList<GamePoint>();


        int spaceWidthMargin = rect.width() / 10;
        int boardLeft = rect.left + spaceWidthMargin;
        int boardWidth = (rect.width() - spaceWidthMargin) - spaceWidthMargin;

        int spaceHeightMargin = rect.height() / 10;
        int boardTop = rect.top + spaceHeightMargin;
        int boardHeight = (rect.height() - spaceHeightMargin) - spaceHeightMargin;

        int hexWidth = boardWidth / BoardState.ROW_MAX;
        // TODO not totally sure if we can always divide by 2 for everybody board (but then again who cares)
        int hexHeight = boardHeight / 4;

        int hexLineWidth = (hexWidth / 2) + 1;
        int hexLineHeight = (hexHeight / 3) + 1;

        int index = 0;

        //innerRect = new Rect(boardLeft, boardTop,
         //                    boardLeft + boardWidth, boardTop + boardHeight);

        int maxCount = 0;
        for (int i = 0; i < BoardState.rowCounts.length; i++) {
            int count = BoardState.rowCounts[i];
            if (count > maxCount) maxCount = count;
            int diff = BoardState.ROW_MAX - count;
            int outerLeftOffset = boardLeft + (hexLineWidth * diff);
            outerLeftOffset += hexLineWidth;
            int topOffset = boardTop + ( i * (hexLineHeight * 2) );
            for (int j = 0; j < count; j++) {
                LogicalBoard.LogicalHex logicalHex = boardState.getLogicalBoard().getRows().get(i).getHex(j);
                int leftOffset = outerLeftOffset + (j * (hexLineWidth * 2));
                Hexagon hexagon = new Hexagon(leftOffset, topOffset, hexWidth, hexHeight, logicalHex);
                hexagons[index] = hexagon;
                index++;
                for (GamePoint p : hexagon.getPoints()) {
                    allPoints.add(p);
                }
            }
        }
        //totalHeight = BoardState.rowCounts.length * hexHeight;
        //totalWidth = maxCount * hexWidth;
    }
}
