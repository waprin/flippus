package us.flipp.moding;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.util.Pair;
import us.flipp.R;
import us.flipp.animation.GameDrawer;
import us.flipp.animation.Vector2i;
import us.flipp.animation.Widget;
import us.flipp.animation.WidgetPage;
import us.flipp.simulation.BoardState;
import us.flipp.simulation.LogicalBoard;
import us.flipp.simulation.Resource;

import java.util.EnumMap;

public class ModeGame extends Mode {

    static private String TAG = ModeGame.class.getName();

    private GameDrawer gameDrawer;
    private BoardState boardState;


    static public final int MAX_PLAYERS = 4;


    private WidgetPage mResourceCountWidgets;
    private EnumMap<Resource, Widget> mResourceWidgetMap;

    private Widget mResourcePane;

    private EnumMap<Resource, Bitmap> mResourceBitmaps;

    private int mResourceSubpanelHeight;
    private int mResourceSubpanelBorder;

    @Override
    public ModeAction tick(int timespan) {
        gameDrawer.tick(timespan);
        return ModeAction.NoAction;
    }


    @Override
    public void screenChanged(int width, int height) {
        super.screenChanged(width, height);
        Log.d(TAG, "screenChanged(): width: " + width + " height " + height);
        boardState.getLogicalBoard().print();
        gameDrawer.updateSize(width, height, boardState);

        /*Vector2i resourceCountOffset = new Vector2i(mScreenWidth / 2 - (mContext.getResources().getInteger(R.integer.resource_display_width) * 5) / 2
                ,(mScreenHeight - mContext.getResources().getInteger(R.integer.resource_display_height)));
        mResourceCountWidgets.setOffset(resourceCountOffset);
          */
        Bitmap pane = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.resource_panel));

        Rect resourcePaneRect = new Rect(mScreenWidth - pane.getWidth(), mScreenHeight - pane.getHeight(), mScreenWidth, mScreenHeight);

        mResourcePane = new Widget(BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.raw.resource_panel)),
                resourcePaneRect);

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
            mResourceWidgetMap.put(Resource.values()[i], resourceWidget);
            mResourceCountWidgets.addWidget(resourceWidget);
        }
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
    }

    @Override
    public String handleBottomButton() {
        Log.d(TAG, "handling bottom button");
        return "";
    }

    @Override
    public String handleTopButton() {
        Log.d(TAG, "handling button press");
        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
                LogicalBoard.LogicalPoint suggestedVillage = gameDrawer.getSuggestedVillage();
                if (suggestedVillage != null) {
                    gameDrawer.setSuggestedVillage(null);
                    boardState.buildVillage(suggestedVillage);
                }

                boardState.endTurn();
                break;
            case BUILD_FIRST_TRACK:
            case BUILD_SECOND_TRACK:
                Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint> suggestedTrack = gameDrawer.getSuggestedTrack();
                if (suggestedTrack != null) {
                    boardState.buildTrack(suggestedTrack);
                }
                boardState.endTurn();
                break;
        }
        return "";
    }

    @Override
    public void handleTap(int x, int y) {
        gameDrawer.handleTap(x, y);
        if (gameDrawer.boardContains(x, y)) {
            handleBoardTap(x, y);
        }

    }

    private void handleBoardTap(int x, int y) {
        Log.d(TAG, "handle tap");
        switch (boardState.getGameState()) {
            case BUILD_FIRST_VILLAGE:
            case BUILD_SECOND_VILLAGE:
            {
                LogicalBoard.LogicalPoint closestPoint = gameDrawer.getClosestPoint(x, y);
                gameDrawer.setSuggestedVillage(closestPoint);
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
                gameDrawer.setSuggestedTrack(boardState.getCurrentPlayer(), new Pair<LogicalBoard.LogicalPoint, LogicalBoard.LogicalPoint>(closestPoint, connectedPoint));
                break;
            }
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
        updateResourceStock();
        mResourcePane.draw(canvas, new Vector2i(0, 0));
        mResourceCountWidgets.draw(canvas);
    }

    private void updateResourceStock() {
        EnumMap<Resource, Integer> resources = boardState.getCurrentPlayer().getResources();
        for (Resource resource : resources.keySet()) {
//            mResourceWidgetMap.get(resource).setText(Integer.toString(resources.get(resource)));
        }
    }

    private Bitmap createBitmap(Rect sizeAndPosition, Resource resource) {
        Bitmap bitmap = Bitmap.createBitmap(sizeAndPosition.width(), sizeAndPosition.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas =  new Canvas(bitmap);
       // Paint clearPaint = new Paint();
       // clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
      //  clearPaint.setARGB(255, 255, 0, 0);
       // canvas.drawPaint(clearPaint);

        Paint circlePaint = new Paint();
        //circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        Path path = new Path();
        path.addCircle(sizeAndPosition.width() / 3, sizeAndPosition.height() / 2, 9.0f, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(mResourceBitmaps.get(resource), 0.f, 0.f, null);
        canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

        return bitmap;
    }
}
