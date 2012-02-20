package us.flipp.tests;

import android.test.InstrumentationTestCase;
import junit.framework.TestCase;
import us.flipp.FlippusActivity;
import us.flipp.simulation.World;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class TestWorld extends InstrumentationTestCase {

    public void testWorldCreation() throws IOException {
        World world = new World(getInstrumentation().getContext().getAssets().open("Levels/level-1.level"));
        assertEquals(world.getAuthor(), "Bill");
    }

}
