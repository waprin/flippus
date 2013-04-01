package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.Log;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

public class MenuPopoutWidget {

    private static final String TAG = MenuPopoutWidget.class.getName();

    private Rect rect;
    private Rect movingRect;
    private Rect worldCenter;
    private Paint redPaint;
    private Paint bluePaint;
    private Paint greenPaint;
    private Paint yellowPaint;

    private Rect buildTopRect;
    private Rect cardsTopLeftRect;
    private Rect tradeTopRightRect;
    private Rect moreBottomLeftRect;
    private Rect endTurnBottomRightRect;

    private List<Rect> menuRects;

    private Rect selectedRect;

    private float x_offset;

    private float openOffset;

    private static final float LEFT_SPEED_PER_MS = 500.f/1000.f;
    private static final float RADIUS = 70.f;
    private static final float OPEN_SPEED_PER_MS=200.0f/1000.f;

    private static final float BUTTON_RADIUS = 30.0f;

    private ModeGame modeGame;

    private enum POPOUT_STATE {
        CLOSED,
        MOVING_LEFT,
        MOVING_RIGHT,
        OPENING,
        OPEN,
        CLOSING,
    }

    private POPOUT_STATE state;

    public MenuPopoutWidget(ModeGame modeGame, Rect rect, Rect worldCenter) {
        this.modeGame = modeGame;
        this.rect = rect;
        Log.d(TAG, "MenuPopoutWidget(): initializing starting rect to : " + rect.toString());
        this.worldCenter = worldCenter;
        this.movingRect = new Rect(rect);

        this.redPaint = new Paint();
        this.redPaint.setStyle(Paint.Style.FILL);
        this.redPaint.setARGB(255, 255, 0, 0);

        this.bluePaint = new Paint();
        this.bluePaint.setStyle(Paint.Style.FILL);
        this.bluePaint.setARGB(255, 0, 0, 255);

        this.greenPaint = new Paint();
        this.greenPaint.setStyle(Paint.Style.FILL);
        this.greenPaint.setARGB(255, 0, 255, 0);

        this.yellowPaint = new Paint();
        this.yellowPaint.setStyle(Paint.Style.FILL);
        this.yellowPaint.setARGB(255, 0, 255, 255);

        this.state = POPOUT_STATE.CLOSED;

        this.x_offset = 0;
        this.openOffset = 0.f;
    }
       /*
    @Override
    public boolean contains(int x, int y) {
        switch(state) {
            case CLOSED:
                return rect.contains(x, y);
            case OPEN:
                for (Rect menuRect : menuRects) {
                    if (menuRect.contains(x, y)) {
                        return true;
                    }
                    if (movingRect.contains(x, y)) {
                        return true;
                    }
                }
            default:
                break;
        }
        return false;
    }
    */

    public void draw(Canvas canvas, BoardState boardState) {
        switch (state) {
            case CLOSED:
                canvas.drawRect(rect, redPaint);
                break;
            case MOVING_LEFT:
            case MOVING_RIGHT:
                canvas.drawRect(movingRect, greenPaint);
                break;
            case OPENING:
            case CLOSING:
                canvas.drawCircle(buildTopRect.centerX(), buildTopRect.centerY(), BUTTON_RADIUS, bluePaint);
                canvas.drawCircle(cardsTopLeftRect.centerX(), cardsTopLeftRect.centerY(), BUTTON_RADIUS, bluePaint);
                canvas.drawCircle(tradeTopRightRect.centerX(), tradeTopRightRect.centerY(), BUTTON_RADIUS, bluePaint);
                canvas.drawCircle(moreBottomLeftRect.centerX(), moreBottomLeftRect.centerY(), BUTTON_RADIUS, bluePaint);
                canvas.drawCircle(endTurnBottomRightRect.centerX(), endTurnBottomRightRect.centerY(), BUTTON_RADIUS, bluePaint);
                canvas.drawCircle(movingRect.centerX(), movingRect.centerY(), BUTTON_RADIUS, bluePaint);
                break;
            case OPEN:
                for (Rect menuRect: menuRects) {
                    canvas.drawCircle(menuRect.centerX(), menuRect.centerY(), BUTTON_RADIUS, menuRect == selectedRect ? redPaint : yellowPaint);
                }
                canvas.drawCircle(movingRect.centerX(), movingRect.centerY(), BUTTON_RADIUS, yellowPaint);
                break;
        }
    }

    public void handleTap(int x, int y) {
        Log.d(TAG, "handleTap(): begin ... ");
        switch (this.state) {
            case CLOSED:
                Log.d(TAG, "handleTap(): handling closed state.");
                state = POPOUT_STATE.MOVING_LEFT;
                break;
            case OPEN:
                for (Rect menuRect : menuRects) {
                    if (menuRect.contains(x, y)) {
                        selectedRect = menuRect;
                    }
                    if (movingRect.contains(x, y)) {
                        state = POPOUT_STATE.CLOSING;
                    }
                }
                break;

        }
    }

    private void startOpening() {
        this.buildTopRect = new Rect(movingRect);
        this.cardsTopLeftRect = new Rect(movingRect);
        this.tradeTopRightRect = new Rect(movingRect);
        this.moreBottomLeftRect = new Rect(movingRect);
        this.endTurnBottomRightRect = new Rect(movingRect);

        this.menuRects = new ArrayList<Rect>();
        this.menuRects.add(this.buildTopRect);
        this.menuRects.add(this.cardsTopLeftRect);
        this.menuRects.add(this.tradeTopRightRect);
        this.menuRects.add(this.moreBottomLeftRect);
        this.menuRects.add(this.endTurnBottomRightRect);

        this.state = POPOUT_STATE.OPENING;
        this.selectedRect = null;
    }

    public void tick(int timespan) {
        switch (state) {
            case MOVING_LEFT:
            case MOVING_RIGHT:
                float move = LEFT_SPEED_PER_MS * (float)timespan;
                if (state == POPOUT_STATE.MOVING_LEFT) {
                    x_offset -= move;
                } else if (state == POPOUT_STATE.MOVING_RIGHT) {
                    x_offset += move;
                }
                movingRect.left = rect.left + (int)x_offset;
                movingRect.right = rect.right + (int)x_offset;
                Log.d(TAG, "moving rect left is now " + movingRect.left);
                if (state == POPOUT_STATE.MOVING_LEFT && movingRect.left <= worldCenter.left) {
                    startOpening();
                }
                if (state == POPOUT_STATE.MOVING_RIGHT && movingRect.left >= rect.left) {
                    state = POPOUT_STATE.CLOSED;
                }

                break;
            case OPENING:
            case CLOSING:
                if (state == POPOUT_STATE.OPENING) {
                    openOffset += OPEN_SPEED_PER_MS * (float) timespan;
                } else {
                    openOffset -= OPEN_SPEED_PER_MS * (float) timespan;
                }
                buildTopRect.top = movingRect.top - (int)openOffset;
                buildTopRect.bottom = movingRect.bottom - (int)openOffset;
                cardsTopLeftRect.top = movingRect.top - (int)(openOffset*.5);
                cardsTopLeftRect.bottom = movingRect.bottom - (int)(openOffset*.5);
                cardsTopLeftRect.left = movingRect.left - (int) openOffset;
                cardsTopLeftRect.right = movingRect.right - (int) openOffset;
                tradeTopRightRect.top = movingRect.top - (int)(openOffset*.5);
                tradeTopRightRect.bottom = movingRect.bottom - (int)(openOffset*.5);
                tradeTopRightRect.left = movingRect.left + (int) openOffset;
                tradeTopRightRect.right = movingRect.right + (int) openOffset;
                moreBottomLeftRect.top = movingRect.top + (int) openOffset;
                moreBottomLeftRect.bottom = movingRect.bottom + (int) openOffset;
                moreBottomLeftRect.left = movingRect.left - (int) (openOffset*.5);
                moreBottomLeftRect.right = movingRect.right - (int)(openOffset*.5);
                endTurnBottomRightRect.top = movingRect.top + (int) openOffset;
                endTurnBottomRightRect.bottom = movingRect.bottom+ (int) openOffset;
                endTurnBottomRightRect.left = movingRect.left + (int)(openOffset*.5);
                endTurnBottomRightRect.right = movingRect.right + (int)(openOffset*.5);

                if (state == POPOUT_STATE.OPENING && openOffset >= RADIUS) {
                    state = POPOUT_STATE.OPEN;
                }
                if (state == POPOUT_STATE.CLOSING && openOffset <= 0) {
                    state = POPOUT_STATE.MOVING_RIGHT;
                }
                break;
        }
    }
}
