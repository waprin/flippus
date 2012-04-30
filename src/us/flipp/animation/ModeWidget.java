package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import us.flipp.moding.GameStateMachine;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;

public class ModeWidget implements Widget {

    private Rect rect;
    private Paint textPaint;
    private Paint borderPaint;
    private ModeGame modeGame;

    public ModeWidget(ModeGame game, Rect rect) {
        this.modeGame = game;
        this.rect = rect;
        textPaint = new Paint();
        textPaint.setARGB(255, 255, 0, 0);
        textPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint();
        borderPaint.setARGB(255, 0, 0, 0);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public void draw(Canvas canvas, BoardState boardState) {

        Paint.FontMetrics fm = new Paint.FontMetrics();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.getFontMetrics(fm);
        canvas.drawText("Start", rect.centerX(), rect.centerY() + -(fm.ascent + fm.descent) / 2, textPaint);
        canvas.drawRect(rect, borderPaint);
    }

    @Override
    public void handleTap(int x, int y) {

    }
}
