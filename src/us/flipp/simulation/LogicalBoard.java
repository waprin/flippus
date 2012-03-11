package us.flipp.simulation;

import android.graphics.Point;
import android.nfc.Tag;
import android.util.Log;
import us.flipp.animation.Hexagon;

import java.util.ArrayList;
import java.util.List;

public class LogicalBoard {

    private static final String TAG = LogicalBoard.class.getName();

    private List<LogicalHexRow> rows;

    public List<LogicalHexRow> getRows() {
        return rows;
    }

    private ArrayList<LogicalPoint> logicalPoints;

    public LogicalPoint getLogicalPointByIndex(int i) {
        return logicalPoints.get(i);
    }

    static public class LogicalPoint {
        private List<LogicalPoint> connected;
        private int index;
        public LogicalPoint(int index) {
            this.index = index;
            connected = new ArrayList<LogicalPoint>();
        }

        public void addConnected(LogicalPoint point) {
            connected.add(point);
        }

        public List<LogicalPoint> getConnected() {
            return connected;
        }

        public int getIndex() {
            return index;
        }
    }

    public void print() {
        Log.d(TAG, "print() begin");
        for (LogicalHexRow row : rows) {
            Log.d(TAG, "print(): row size is " + row.size);
            for (int i = 0; i < row.getSize(); i++) {
                Log.d(TAG, "print(): cell value is " + row.getHex(i));
            }
        }
    }

    private static void linkPoints(LogicalPoint first, LogicalPoint second) {
        if (first == second) {
            Log.e(TAG, "linkPoints(): SERIOUS ERROR: trying to link a point to itself");
        }
        Log.d(TAG, "linkPoints(): linking together nodes " + first.index + " and node " + second.index);
        first.addConnected(second);
        second.addConnected(first);
    }

    private LogicalPoint newPoint() {
        LogicalPoint point = new LogicalPoint(logicalPoints.size());
        logicalPoints.add(point);
        Log.d(TAG, "adding logical point with index " + point.getIndex());
        return point;
    }

    public class LogicalHex {

        private List<LogicalPoint> points;
        public LogicalPoint getPoint(int index) {
            return points.get(index);
        }


        public LogicalHex(LogicalHex leftAbove, LogicalHex rightAbove, LogicalHex left) {
            LogicalPoint firstPoint;
            LogicalPoint secondPoint;
            LogicalPoint thirdPoint;
            LogicalPoint fourthPoint;
            LogicalPoint fifthPoint;
            LogicalPoint sixthPoint;

            if (leftAbove != null) {
                firstPoint = leftAbove.getPoint(2);
            } else if (rightAbove != null) {
                firstPoint = rightAbove.getPoint(4);
            } else {
                firstPoint = newPoint();
            }

            if (rightAbove != null) {
                secondPoint = rightAbove.getPoint(3);
            } else {
                secondPoint = newPoint();
            }

            thirdPoint = newPoint();
            fourthPoint = newPoint();
            if (left != null) {
                fifthPoint = left.getPoint(2);
                sixthPoint = left.getPoint(1);
            } else {
                fifthPoint = newPoint();
                sixthPoint = newPoint();
            }

            linkPoints(firstPoint, secondPoint);
            linkPoints(secondPoint, thirdPoint);
            linkPoints(thirdPoint, fourthPoint);
            linkPoints(fourthPoint, fifthPoint);
            linkPoints(fifthPoint, sixthPoint);
            linkPoints(sixthPoint, firstPoint);

            points = new ArrayList<LogicalPoint>();
            points.add(firstPoint);
            points.add(secondPoint);
            points.add(thirdPoint);
            points.add(fourthPoint);
            points.add(fifthPoint);
            points.add(sixthPoint);
        }

    }

    public List<LogicalPoint> getPoints() {
        return logicalPoints;
    }

    public class LogicalHexRow {
        private int size;
        private List<LogicalHex> hexes;

        public LogicalHex getHex(int index) {
            if (index < 0 || index >= hexes.size()) {
                return null;
            }
            return hexes.get(index);
        }

        public int getSize() {
            return size;
        }

        public LogicalHexRow(LogicalHexRow aboveRow, int size) {
            if (aboveRow == null) {
                if (rows.size() > 0) {
                    Log.wtf(TAG, "adding the first hex row but rows isn´t empty!");
                    return;
                }
            }

            hexes = new ArrayList<LogicalHex>();
            LogicalHex leftHex = null;
            if (aboveRow == null) {
                for (int i = 0; i < size; i++) {
                    LogicalHex hex = new LogicalHex(null, null, leftHex);
                    hexes.add(hex);
                    leftHex = hex;
                }
            } else {
                boolean smaller;
                if (size == (aboveRow.getSize() - 1)) {
                    smaller = true;
                } else if (size == (aboveRow.getSize() + 1)) {
                    smaller = false;
                } else {
                    Log.wtf(TAG, "row being added is not a valid size!");
                    return;
                }
                int aboveIndex = smaller ? 0 : -1;
                Log.d(TAG, "LogicalHexRow() aboveRow is " + aboveRow.getSize());
                for (int i = 0; i < size; i++) {
                    Log.d(TAG, "LogicalHexRow(): i is " + i);
                    LogicalHex aboveRightHex = aboveRow.getHex(aboveIndex + 1);
                    LogicalHex hex = new LogicalHex(aboveRow.getHex(aboveIndex), aboveRightHex, leftHex);
                    leftHex = hex;
                    hexes.add(hex);
                    aboveIndex++;
                }
            }

            this.size = size;
        }
    }

    public LogicalBoard() {
        rows = new ArrayList<LogicalHexRow>();
        logicalPoints = new ArrayList<LogicalPoint>();
        LogicalHexRow firstRow = new LogicalHexRow(null, 3);
        LogicalHexRow secondRow = new LogicalHexRow(firstRow, 4);
        LogicalHexRow thirdRow = new LogicalHexRow(secondRow, 5);
        LogicalHexRow fourthRow = new LogicalHexRow(thirdRow, 4);
        LogicalHexRow fifthRow = new LogicalHexRow(fourthRow, 3);
        rows.add(firstRow);
        rows.add(secondRow);
        rows.add(thirdRow);
        rows.add(fourthRow);
        rows.add(fifthRow);
    }

}
