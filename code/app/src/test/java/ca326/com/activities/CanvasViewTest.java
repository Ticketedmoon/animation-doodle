package ca326.com.activities;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CanvasViewTest {

    // TEST NOT WORKING WILL FIX LATER

    @Mock
    private Context context;

    @Mock
    private CanvasView canvas = mock(CanvasView.class);

    @Test
    public void testNewPathMethod(){
        // Method should return an empty path object.
        // Test both outcomes
        // Outcome A
        CanvasView world = new CanvasView(context);
        CanvasView spy = Mockito.spy(world);
        Mockito.doCallRealMethod().when(spy).get_new_Path();
       android.graphics.Path test = spy.get_new_Path();

        assertEquals(test.isEmpty(), spy.get_new_Path());

        // Outcome B
        // Update path
    }
}
