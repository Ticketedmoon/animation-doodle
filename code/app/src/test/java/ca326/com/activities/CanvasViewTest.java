package ca326.com.activities;

import android.content.Context;
import android.graphics.Path;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;

public class CanvasViewTest {

    // TEST NOT WORKING WILL FIX LATER

    @Mock
    private Context context;

    @Test
    public void testNewPathMethod(){
        // Method should return an empty path object.
        // Test both outcomes
        // Outcome A
        CanvasView canvas = new CanvasView(context);
        Path testPath = canvas.get_new_Path();

        boolean positiveResult = testPath.isEmpty();
        assertEquals(true, positiveResult);

        // Outcome B
        // Update path
        testPath.lineTo(50, 50);
        testPath.lineTo(50, 50);

        boolean negativeResult = testPath.isEmpty();
        assertEquals(false, negativeResult);
    }
}
