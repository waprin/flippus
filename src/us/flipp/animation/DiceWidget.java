package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;

public class DiceWidget implements Widget {

    private ModeGame modeGame;
    private Rect rect;
    private Paint redPaint;

    public DiceWidget(ModeGame modeGame, Rect rect) {
        this.modeGame  = modeGame;
        this.rect = rect;
        this.redPaint = new Paint();
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setARGB(255, 255, 0, 0);
    }

    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public void draw(Canvas canvas, BoardState boardState) {
        canvas.drawRect(this.rect, this.redPaint);
    }

    @Override
    public void handleTap(int x, int y) {
    }

    @Override
    public void tick(int timespan) {
    }
}
