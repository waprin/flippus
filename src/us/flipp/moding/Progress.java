package us.flipp.moding;

import android.content.Context;
import android.util.Log;
import us.flipp.simulation.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import android.util.Log;


public class Progress {

    private static final String TAG = Progress.class.getName();
    private int levelPackIndex;
    private Context context;
    private ArrayList<LevelPack> levels = new ArrayList<LevelPack>();

    private class ProgressRecord
    {
        public boolean beaten = false;
        public String filename = "";
    }

    private class LevelPack
    {
        public String levelPackName;
        public ArrayList<ProgressRecord> levels = new ArrayList<ProgressRecord>();
        public int levelIndex;
        ProgressRecord getLevel()
        {
            Log.d(TAG, "getting the specific level from level index " + levelIndex + " from a pack with " + levels.size() + " level");
            ProgressRecord record = levels.get(levelIndex);
            Log.d(TAG, "got progress record "  + record.filename);
            levelIndex++;
            levelIndex %= levels.size();
            return record;
        }
    }

    public Progress(Context context) {
        Log.d(TAG, "initializing context to " + context);
        this.context = context;
        reload();
    }

    public void reload() {
        reloadLevels();
    }

    public World getWorld(String levelName) throws IOException {
        Log.d(TAG, "getWorld(): attempting to load level " + levelName);
        if (levelName.startsWith("assets://"))
        {
            World world = new World(context.getAssets().open(levelName.substring(9)));
            world.setIdentifier(levelName);
            Log.d(TAG, "getWorld(): returning world with author " + world.getAuthor());
            return world;
        }
        return null;
    }

    public String getLevelPack() {
        return levels.get(levelPackIndex).levelPackName;
    }

    public String getLevel() {
        Log.d(TAG, "getting level from level pack index "+ levelPackIndex + " from a level pack of size " + levels.size());
        String levelName = levels.get(levelPackIndex).getLevel().filename;
        Log.d(TAG, "returning level " + levelName);
        return levelName;
    }

    public World getWorld() throws IOException {
        String levelName = getLevel();
        return getWorld(levelName);
    }

    public void reloadLevels() {
        Log.d(TAG, "reloadLevels(): begin ");
        try
        {
            Log.d(TAG, "context is " + this.context);
            String[] levelPackNames = this.context.getAssets().list("Levels");
            Log.d(TAG, "reloadLevels(): number of level packs " + levelPackNames.length);
            levels.clear();

            for (String levelPackName : levelPackNames)
            {
                LevelPack levelPack = new LevelPack();
                levelPack.levelPackName = levelPackName;

                String [] levelNames = context.getAssets().list("Levels/" + levelPackName);
                Log.d(TAG, "found " + levelNames.length + " levels in level pack" + levelPackName);
                for (String levelName : levelNames)
                {
                    Log.d(TAG, "reloadLevels(): loading level " + levelName);
                    ProgressRecord progressRecord = new ProgressRecord();
                    progressRecord.beaten = false;
                    progressRecord.filename = "assets://Levels/" + levelPackName + "/"+ levelName;
                    levelPack.levels.add(progressRecord);
                }

                levels.add(levelPack);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "Caught exception while loading levels "+ e);
        }
    }
}
