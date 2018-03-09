package ca326.com.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
@RunWith(PowerMockRunner.class)
public class FileUploadTest {


    @PrepareForTest({Uri.class})
    @Test
    public void uriParsingTest() {
        FileUpload file = new FileUpload();
        Profile_Screen screen = new Profile_Screen();
        String image = "/storage/emulated/0/DCIM/Camera/IMG_20180307_153933.jpg";
        //canvas = screen.setImage(image);
        // assertEquals(null,canvas);

        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        try {
            PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
        } catch (Exception e) {

        }
        Test_VideoPlayer testing = new Test_VideoPlayer();
        String video_url = "http://animationdoodle2017.com/videos/uploads/NewAnimation.mp4";
        uri = testing.check_video_url(video_url);
        assertNotEquals(null, uri);

    }
}