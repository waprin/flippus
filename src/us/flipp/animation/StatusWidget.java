package us.flipp.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;
import us.flipp.moding.ModeGame;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.Player;

import java.util.ArrayList;
import java.util.List;

public class StatusWidget  {

    private ModeGame modeGame;
    private Rect rect;
    private List<Pair<Player, Rect>> playerStatuses;
    private Paint[] paints;
    public StatusWidget(ModeGame modeGame, Rect rect, Paint[] paints) {
        this.paints = paints;
        int width = rect.width();
        int boxWidth = width / ModeGame.MAX_PLAYERS;

           /*
        this.playerStatuses = new ArrayList<Pair<Player, Rect>>();
        List<Player> players = modeGame.getBoardState().getAllPlayers();
        for (int i = 0; i < ModeGame.MAX_PLAYERS; i++) {
            this.playerStatuses.add(new Pair<Player, Rect>(players.get(i), new Rect(rect.left + (boxWidth * i), rect.top, rect.left + (boxWidth * i + boxWidth), rect.bottom)));
        }    */
    }


    public void draw(Canvas canvas, BoardState boardState) {
        for (int i = 0; i < playerStatuses.size(); i++) {
            canvas.drawRect(this.playerStatuses.get(i).second, this.paints[i]);
        }
    }

    public void handleTap(int x, int y) {
    }

    public void tick(int timespan) {
    }
}
