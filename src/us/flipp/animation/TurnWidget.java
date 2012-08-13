package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

public class TurnWidget {

    private Rect rect;
    private Paint textPaint;
    private BoardState boardState;

    public TurnWidget(BoardState boardState, Rect rect) {
        this.boardState = boardState;
        this.rect = rect;
        textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 0);
    }

    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawText(boardState.getCurrentPlayer().toString(), rect.left, rect.top, textPaint);
    }

    public void handleTap(int x, int y) {

    }

    public void tick(int timespan) {

    }
}
