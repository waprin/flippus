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

    private int selected;

    private int[] colors;
    private Hexagon[] hexagons;

    public Bitmap collisionBitmap;
    private Canvas collisionCanvas;

    public int getColor(int index) {
        return colors[index];
    }

    public Canvas getCollisionCanvas() {
        return this.collisionCanvas;
    }

    public HexBoard() {
        this.colors = new int[HexBoard.TOTAL_HEXES];
        int[] random_colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.GREEN};
        Random random = new Random();
        for (int i = 0; i < colors.length; i++) {
            colors[i] = random_colors[random.nextInt(random_colors.length)];
        }
        selected = -1;
    }

    public Hexagon[] getHexagons() {
            return this.hexagons;
    }

    public int getSelected() {
        return selected;
    }

    public void touchSelected(int x, int y) {
        selected = Color.alpha(collisionBitmap.getPixel(x, y));
        Log.w(TAG, "touch selected selected index " + selected);
    }

    public void updateCanvas(Canvas canvas) {
        collisionBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        collisionCanvas = new Canvas(collisionBitmap);

        hexagons = new Hexagon[TOTAL_HEXES];

        int spaceWidthMargin = canvas.getWidth() / 10;
        int boardLeft = spaceWidthMargin;
        int boardWidth = (canvas.getWidth() - spaceWidthMargin) - spaceWidthMargin;

        int spaceHeightMargin = canvas.getHeight() / 10;
        int boardTop = spaceHeightMargin;
        int boardHeight = (canvas.getHeight() - spaceHeightMargin) - spaceHeightMargin;


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
