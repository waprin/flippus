package us.flipp.simulation;

import android.graphics.Point;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LogicalBoard {

    private static final String TAG = LogicalBoard.class.getName();

    private List<LogicalHexRow> rows;

    public List<LogicalHexRow> getRows() {
        return rows;
    }

    static public class LogicalPoint {
        private List<LogicalPoint> connected;
        private List<LogicalHex> adjacent;
        private int index;

        public LogicalPoint(int index) {
            this.index = index;
            connected = new ArrayList<LogicalPoint>();
            adjacent = new ArrayList<LogicalHex>();
        }

        public void addConnected(LogicalPoint point) {
            connected.add(point);
        }
    }

    private int totalPoints = 0;

    private static void linkPoints(LogicalPoint first, LogicalPoint second) {
        first.addConnected(second);
        second.addConnected(first);
    }

    private LogicalPoint newPoint() {
        LogicalPoint point = new LogicalPoint(totalPoints);
        totalPoints++;
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
                firstPoint = leftAbove.getPoint(4);
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
                int aboveIndex = smaller ? -1 : 0;

                for (int i = 0; i < size; i++) {
                    LogicalHex hex = new LogicalHex(aboveRow.getHex(aboveIndex), aboveRow.getHex(aboveIndex + 1), leftHex);
                    leftHex = hex;
                    hexes.add(hex);
                    aboveIndex++;
                }
            }
        }
    }

    public LogicalBoard() {
        rows = new ArrayList<LogicalHexRow>();
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
