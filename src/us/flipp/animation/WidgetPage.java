package us.flipp.animation;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WidgetPage {
    private List<Widget> mWidgets = new ArrayList<Widget>();
    private Vector2i mOffset = new Vector2i(0, 0);

    public void addWidget(Widget widget) {
        mWidgets.add(widget);
    }

    public void handleTap(int x, int y) {
        for (Widget widget : mWidgets) {
            widget.handleTap(x, y);
        }
    }

    public void draw(Canvas canvas) {
        for (Widget widget : mWidgets) {
            widget.draw(canvas, mOffset);
        }
    }

    public void tick(int timespan) {
        for (Widget widget : mWidgets) {
            widget.tick(timespan);
        }
    }

    public void setOffset(Vector2i offset) {
        mOffset = offset;
    }

    public Vector2i getOffset() {
        return mOffset;
    }
}
