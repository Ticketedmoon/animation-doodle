package ca326.com.activities;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class CanvasViewTest {

    // TEST NOT WORKING WILL FIX LATER

    @Mock
    private Context context;

    @Test
    public void testClearCanvas() throws Exception {

        // This test method will check the clear button's functionality.
        // We will carry out two tests
        // The first being that the canvas correctly has items in it.

        CanvasView canvas = new CanvasView(context);
        canvas.newPaths.add(new Pair<Path, Paint>(new Path(), new Paint()));
        canvas.newPaths.add(new Pair<Path, Paint>(new Path(), new Paint()));
        boolean checkBefore = canvas.newPaths.size() > 0;
        assertEquals(true, checkBefore);

        // The second test being that once we call the clear method, all items will be removed
        // Thus the size() should be == 1 since we add a pathway in after clearing.
        // test call clear
        canvas.clearCanvas();
        boolean isEmpty = canvas.newPaths.size() == 1;
        assertEquals(true, isEmpty);

    }

    @Test
    public void testUndoAction() throws Exception {
        // Create your newPaths array of pathways
        CanvasView canvas = new CanvasView(context);
        canvas.newPaths.add(new Pair<Path, Paint>(new Path(), new Paint()));
        canvas.newPaths.add(new Pair<Path, Paint>(new Path(), new Paint()));

        // Test for a positive result
        int pathways_size = canvas.newPaths.size();
        canvas.undoAction();
        assertEquals(pathways_size-1, canvas.newPaths.size());
        // NewPaths size should be equal to one less

        // Give a False case
        canvas.undoAction();
        assertEquals(false, canvas.newPaths.isEmpty());
    }

}
