package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HexBoard {

    public static int[] rowCounts = {3, 4, 5, 4 ,3};
    public static final int ROW_MAX = 5;
    public static final int TOTAL_HEXES = 19;

    private Hexagon[] hexagons;

    public Hexagon[] getHexagons() {
            return this.hexagons;
    }

    public HexBoard() {

    }

    public void initialize(Canvas canvas) {

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
