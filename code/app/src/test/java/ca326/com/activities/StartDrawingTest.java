package ca326.com.activities;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;

public class StartDrawingTest {

    @Mock
    private CanvasView view;

    @Test
    public void testFrameRate() {

        Start_Drawing_Screen instance_test = new Start_Drawing_Screen();
        int fr = instance_test.frame_rate_value;
        boolean check_frame_rate = (fr > 0 && fr < 31);

        assertEquals(true, check_frame_rate);
    }
}
