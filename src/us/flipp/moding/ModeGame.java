package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.R;
import us.flipp.animation.*;
import us.flipp.simulation.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class ModeGame extends Mode implements BoardState.EventHandler {

    static private String TAG = ModeGame.class.getName();

    private GameDrawer gameDrawer;
    private BoardState boardState;

    private WidgetPage mResourceCountWidgets;
    private EnumMap<Resource, Widget> mResourceWidgetMap;

    private Widget mResourcePane;
    private Widget mConfirmWidget;

    private WidgetPage mDebugOverlay;
    private Widget mModeWidget;
    private Widget mPercentWidget;

    private EnumMap<Resource, Bitmap> mResourceBitmaps;
    private EnumMap<Player.PlayerID, Widget> mPlayerStatusWidgets;

    private Map<Player.PlayerID, Bitmap> mStatusPanelBitmaps;

    private int mResourceSubpanelHeight;
    private int mResourceSubpanelBorder;
    private WidgetPage mStatusWidgetPage;

    private Queue<Animation> animations;
    private boolean animating;
    private boolean mDebug = true;

    @Override
    public ModeAction tick(int timespan) {
        gameDrawer.tick(timespan);
        if (!animations.isEmpty()) {
            animating = true;
            Animation top = animations.peek();
            if (top.finished()) {
                animations.remove();
            }
            if (!animations.isEmpty()) {
                top = animations.peek();
                top.tick();
                mPercentWidget.setText(Integer.toString(top.getTickCount()) + " / " + Integer.toString(top.getMaxTicks()));
            }
            mPercentWidget.setVisible(true);
        } else {
            animating = false;
            mPercentWidget.setVisible(false);
        }
        return ModeAction.NoAction;
    }


    @Override
    public void screenChanged(int width, int height) {
        super.screenChanged(width, height);
        Log.d(TAG, "screenChanged(): width: " + width + " height " + height);
        boardState.getLogicalBoard().print();
        gameDrawer.updateSize(width, height, boardState);

        Bitmap pane = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.resource_panel));
        Rect resourcePaneRect = new Rect(mScreenWidth - pane.getWidth(), mScreenHeight - pane.getHeight(), mScreenWidth, mScreenHeight);
        mResourcePane = new Widget(pane, resourcePaneRect);

        int checkboxTop = mContext.getResources().getInteger(R.integer.check_box_offset_top);
        int checkboxLeft = mContext.getResources().getInteger(R.integer.check_box_offset_left);
        Bitmap confirmImage = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.checkmark));
        //confirmImage.
        Rect confirmRect = new Rect(checkboxLeft, checkboxTop, checkboxLeft + confirmImage.getWidth(), checkboxTop + confirmImage.getHeight());
        mConfirmWidget = new Widget(confirmImage, confirmRect);
        mConfirmWidget.setVisible(false);
        mConfirmWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClickListener() {
                ModeGame.this.confirmButtonPressed();
            }
        });
        Log.d(TAG, "confirm widget is " + mConfirmWidget.getBounds().toString());


        mResourceCountWidgets = new WidgetPage();
        mResourceWidgetMap = new EnumMap<Resource, Widget>(Resource.class);

        int len = Resource.values().length;
        for (int i = 0; i < len; i++) {
            Log.d(TAG, "screenChanged() calc "  + len + " + i is " + Integer.toString(i) + " calc is " + Integer.toString((len-(i+1))/len));
            Rect innerRect = new Rect(mScreenWidth - resourcePaneRect.width(),
                                      mScreenHeight - (mResourceSubpanelHeight * (i+1)) + mResourceSubpanelBorder,
                                      mScreenWidth,
                                      mScreenHeight - (mResourceSubpanelHeight * i) - mResourceSubpanelBorder
                                      );
            Log.d(TAG, "sreenChanged(): inner rect is of size " + innerRect.width() + " , " + innerRect.height() + " and " +
                    "screen height is " + mScreenHeight + " and innerct bottom is " + innerRect.bottom);

            Widget resourceWidget = new Widget(this.createBitmap(innerRect, Resource.values()[i]), innerRect);

            resourceWidget.setFontSize(10);
            mResourceWidgetMap.put(Resource.values()[i], resourceWidget);
            mResourceCountWidgets.addWidget(resourceWidget);
        }

        int playerStatusPanelHeight = mContext.getResources().getInteger(R.integer.player_status_panel_height);
        int playerStatusPanelWidth = mContext.getResources().getInteger(R.integer.player_status_panel_width);

        mStatusWidgetPage = new WidgetPage();
        mPlayerStatusWidgets = new EnumMap<Player.PlayerID, Widget>(Player.PlayerID.class);

        for (int i = 0; i < Player.PlayerID.values().length; i++) {
            Rect innerRect = new Rect(0, mScreenHeight - (playerStatusPanelHeight * (i+1)), playerStatusPanelWidth, mScreenHeight - (playerStatusPanelHeight * i) );
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mStatusPanelBitmaps.get(Player.PlayerID.values()[i]), innerRect.width(), innerRect.height(), false);
            Widget statusWidget = new Widget(scaledBitmap, innerRect);
            mStatusWidgetPage.addWidget(statusWidget);
            statusWidget.setBorderSize(4.0f);
            Bitmap piechartBitmap = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.piechart));
            int pieChartOffsetLeft = mContext.getResources().getInteger(R.integer.pie_chart_offset_left);
            int pieChartOffsetTop = mContext.getResources().getInteger(R.integer.pie_chart_offset_top);
            Rect piechartRect = new Rect(innerRect.left + pieChartOffsetLeft,
                    innerRect.top + pieChartOffsetTop,
                    innerRect.left + pieChartOffsetLeft + piechartBitmap.getWidth(),
                    innerRect.top + pieChartOffsetTop + piechartBitmap.getHeight());
            Widget piechartWidget = new Widget(piechartBitmap, piechartRect);
            piechartWidget.setText("3");
            piechartWidget.setFontSize(9.0f);
            mStatusWidgetPage.addWidget(piechartWidget);
            mPlayerStatusWidgets.put(Player.PlayerID.values()[i], statusWidget);
        }

        updateHighlightedPlayer();

        mModeWidget = new Widget(new Rect(0, 0, mScreenWidth, mScreenHeight / 5));
        Paint whiteTextPaint = new Paint();
        whiteTextPaint.setARGB(255, 255, 255, 255);
        mModeWidget.setTextPaint(whiteTextPaint);
        mModeWidget.setAlign(Widget.Align.Center);
        mModeWidget.setText(boardState.getGameState().toString());


        mPercentWidget = new Widget(new Rect(0, 0, mScreenWidth / 2, mScreenHeight / 5));
        mPercentWidget.setTextPaint(whiteTextPaint);
        mPercentWidget.setAlign(Widget.Align.Center);

        mDebugOverlay = new WidgetPage();
        mDebugOverlay.addWidget(mModeWidget);
        mDebugOverlay.addWidget(mPercentWidget);

        animations = new LinkedBlockingDeque<Animation>();
        boardState.setEventHandler(this);
    }

    public void updateHighlightedPlayer() {
        for (Player.PlayerID id : Player.PlayerID.values()) {
            mPlayerStatusWidgets.get(id).setHighlighted(false);
        }
        mPlayerStatusWidgets.get(boardState.getCurrentPlayer().getPlayerID()).setHighlighted(true);
    }

    @Override
    public void setup(Context context) {
        super.setup(context);
        Log.d(TAG, "setup(): begin");
        boardState = new BoardState();

        Bitmap stoneBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.stone));
        Bitmap lumberBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.wood));
        Bitmap brickBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.brick));
        Bitmap woolBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.wool));
        Bitmap grainBitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.wheat));

        mResourceBitmaps = new EnumMap<Resource, Bitmap>(Resource.class);
        mResourceBitmaps.put(Resource.Stone, stoneBitmap);
        mResourceBitmaps.put(Resource.Lumber, lumberBitmap);
        mResourceBitmaps.put(Resource.Brick, brickBitmap);
        mResourceBitmaps.put(Resource.Wool, woolBitmap);
        mResourceBitmaps.put(Resource.Grain, grainBitmap);

        gameDrawer = new GameDrawer(context, mResourceBitmaps);

        mResourceSubpanelHeight = context.getResources().getInteger(R.integer.resource_subpanel_height);
        mResourceSubpanelBorder = context.getResources().getInteger(R.integer.resource_subpanel_border);

        mStatusPanelBitmaps = new TreeMap<Player.PlayerID,Bitmap>();
        mStatusPanelBitmaps.put(Player.PlayerID.PLAYER_1, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.player_1_panel)));

        mStatusPanelBitmaps.put(Player.PlayerID.PLAYER_2, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.player_2_panel)));
        mStatusPanelBitmaps.put(Player.PlayerID.PLAYER_3, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.player_3_panel)));
        mStatusPanelBitmaps.put(Player.PlayerID.PLAYER_4, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.player_4_panel)));

        animating = false;
    }

    @Override
    public String handleBottomButton() {
        Log.d(TAG, "handling bottom button");
        return "";
    }

    @Override
    public String handleTopButton() {
        Log.d(TAG, "handling button press");

        return "";
    }

    @Override
    public void handleTap(int x, int y) {
        Log.d(TAG, "handleTap: x " + x + " y " + y);
        gameDrawer.handleTap(x, y);
        if (gameDrawer.boardContains(x, y)) {
            handleBoardTap(x, y);
        }
        mConfirmWidget.handleTap(x, y);
    }

    private void handleBoardTap(int x, int y) {
        Log.d(TAG, "handle tap");
        if (animating) {
            return;
        }

        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
            {
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                if (boardState.isVillageLegal(closestPoint)) {
                    gameDrawer.setSuggestedVillage(closestPoint);
                    mConfirmWidget.setVisible(true);
                }
                break;
            }
            case BUILD_FIRST_TRACK:
            case BUILD_SECOND_TRACK:
            {
                Log.d(TAG, "handleTap(): Building Track");
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                LogicalBoard.LogicalPoint connectedPoint = gameDrawer.getClosestConnectedPoint(closestPoint, x, y);
                if (closestPoint == connectedPoint) {
                    Log.e(TAG, "handleTap(): somehow a point was detected as connected to itself");
                }
                if (boardState.isLegalTrack(closestPoint, connectedPoint)) {
                    gameDrawer.setSuggestedTrack(new Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>(closestPoint, connectedPoint));
                    mConfirmWidget.setVisible(true);
                }
                break;
            }
        }
    }

    private void endTurn() {
        boardState.endTurn();
        mModeWidget.setText(boardState.getGameState().toString());
        updateResourceStock();
        updateHighlightedPlayer();
        startTurn();
    }

    public void startTurn() {
        boardState.startTurn();
    }

    private void confirmButtonPressed() {
        Log.d(TAG, "Confirm button pressed!");
        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
                LogicalBoard.LogicalPoint suggestedVillage = gameDrawer.getSuggestedVillage();
                if (suggestedVillage != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(suggestedVillage);
                    mConfirmWidget.setVisible(false);
                }
                endTurn();
                break;
            case BUILD_FIRST_TRACK:
            case BUILD_SECOND_TRACK:
                Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> suggestedTrack = gameDrawer.getSuggestedTrack();
                if (suggestedTrack != null) {
                    boardState.buildTrack(suggestedTrack);
                }
                endTurn();
                mConfirmWidget.setVisible(false);
                gameDrawer.setSuggestedTrack(null);
                break;
        }
    }

    @Override
    public Mode teardown() {
        return super.teardown();
    }

    @Override
    public void redraw(Canvas canvas) {
        super.redraw(canvas);
        gameDrawer.draw(canvas, boardState);
        mResourcePane.draw(canvas, new Vector2i(0, 0));
        mConfirmWidget.draw(canvas, new Vector2i(0, 0));
        mResourceCountWidgets.draw(canvas);
        mStatusWidgetPage.draw(canvas);

        if (mDebug) {
            mDebugOverlay.draw(canvas);
        }

        for (Animation animation : animations) {
            animation.draw(canvas);
        }
    }

    private void updateResourceStock() {
        EnumMap<Resource, Integer> resources = boardState.getCurrentPlayer().getResources();
        for (Resource resource : Resource.values()) {
            Integer count = resources.get(resource);
            if (count != null) {
                mResourceWidgetMap.get(resource).setText(Integer.toString(count));
            } else {
                mResourceWidgetMap.get(resource).setText("0");
            }
        }
    }

    private Bitmap createBitmap(Rect sizeAndPosition, Resource resource) {
        Bitmap bitmap = Bitmap.createBitmap(sizeAndPosition.width(), sizeAndPosition.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas =  new Canvas(bitmap);
        Path path = new Path();
        path.addCircle(sizeAndPosition.width() / 3, sizeAndPosition.height() / 2, 9.0f, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(mResourceBitmaps.get(resource), 0.f, 0.f, null);
        canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);
        return bitmap;
    }

    @Override
    public void notify(BoardState.BoardEvent be, Object[] args) {
        switch(be) {
            case RESOURCE_UPDATE:
                Resource resource = (Resource) args[0];
                int left = mResourceWidgetMap.get(resource).getBounds().left;
                int top = mResourceWidgetMap.get(resource).getBounds().top;
                Animation resourceIncreaseAnimation = new ResourceIncreaseAnimation(new Vector2i(left, top), 500);
                animations.add(resourceIncreaseAnimation);
                break;
            case DICE_ROLL:
                break;
        }
    }
}
