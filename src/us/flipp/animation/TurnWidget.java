package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

public class TurnWidget implements Widget {

    private Rect rect;
    private Paint textPaint;
    private BoardState boardState;

    public TurnWidget(BoardState boardState, Rect rect) {
        this.boardState = boardState;
        this.rect = rect;
        textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 0);
    }

    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawText(boardState.getCurrentPlayer().toString(), rect.left, rect.top, textPaint);
    }
}
