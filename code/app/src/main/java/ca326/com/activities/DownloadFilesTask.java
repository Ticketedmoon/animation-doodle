package ca326.com.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.IOException;

class DownloadFilesTask extends AsyncTask<Void, Void, String> {

    private Start_Drawing_Screen instance;
    private SeekableByteChannel out = null;
    private AndroidSequenceEncoder encoder;
    private Bitmap image;
    private ProgressDialog downloading;

    public DownloadFilesTask(Start_Drawing_Screen instance) {
        this.instance = instance;
    }

    @Override
    protected String doInBackground(Void... params) {

        // Verify Storage Permissions
        instance.verifyStoragePermissions(instance);
        // If /Animation_Doodle_Images doesn't exist yet, generate it.
        File file = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle");
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            out = NIOUtils.writableFileChannel(Environment.getExternalStorageDirectory() + "/AnimationDoodle/OUTPUTTESTING.mp4");
            File save_loc = new File(Environment.getExternalStorageDirectory(), "/AnimationDoodle");
            encoder = new AndroidSequenceEncoder(out, Rational.R(instance.frame_rate_value, 1));

            /*
            for(int i = 0; i < instance.canvas_bitmaps.size(); i++)
            {
                image = instance.canvas_bitmaps.get(i);
                encoder.encodeImage(image); // --- This line takes an extreme amount of time to process (ASYNC needed)

                File dir = save_loc;      //where image stores
                String filePrefix = "picture";  //imagename prefix
                String fileExtn = ".jpg";       //image extention
                File src = new File(dir, filePrefix + Start_Drawing_Screen.image_counter + fileExtn);// image name should ne picture001, picture002,picture003 soon  ffmpeg takes as input valid
                Log.i("Download ASYNC", "Encoder: " + "Image (" + i + ") encoded! (50 seconds encoding rate per frame)");
            }
            encoder.finish();  */
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "Complete";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Download", result);
        Toast.makeText(instance.getApplication(), "Animation successfully Downloaded (/sdcard/Animation_Doodle_Images)", Toast.LENGTH_SHORT).show();
        downloading.dismiss();
        NIOUtils.closeQuietly(out);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("Download", "Starting Download...");
        downloading = ProgressDialog.show(this.instance, "Downloading File Locally", "Downloading...", false, true);

    }

    @Override
    protected void onProgressUpdate(Void... values) {}

}

