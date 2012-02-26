package us.flipp.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HexBoard {
    private static final String TAG = HexBoard.class.getName();

    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;

    private int[] colors;
    private Hexagon[] hexagons;

    public static int[] RANDOM_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.GREEN};

    public int getColor(int index) {
        return colors[index];
    }

    public HexBoard() {
        this.colors = new int[HexBoard.TOTAL_HEXES];
        Random random = new Random();
        for (int i = 0; i < colors.length; i++) {
            colors[i] = RANDOM_COLORS[random.nextInt(RANDOM_COLORS.length)];
        }
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

        for (int i = 0; i < rowCounts.length; i++) {
            int count = rowCounts[i];
            int diff = ROW_MAX - count;
            int outerLeftOffset = boardLeft + (hexLineWidth * diff);
            int topOffset = boardTop + ( i * (hexLineHeight * 2) );
            for (int j = 0; j < count; j++) {
                int leftOffset = outerLeftOffset + (j * (hexLineWidth * 2));
                Hexagon hexagon = new Hexagon(leftOffset, topOffset, hexWidth, hexHeight);
                hexagons[index] = hexagon;
                index++;
            }
        }
    }

}
