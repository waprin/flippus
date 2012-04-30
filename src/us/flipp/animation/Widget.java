package us.flipp.animation;

import android.graphics.Canvas;
import us.flipp.simulation.BoardState;


public interface Widget {
    public boolean contains(int x, int y);
    public void draw(Canvas canvas, BoardState boardState);
    void handleTap(int x, int y);
}
