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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class DownloadFrameTask extends AsyncTask<Void, Void, String> {

    private ArrayList<Bitmap> frames;
    private int image_counter = 1;
    private Start_Drawing_Screen instance;

    public DownloadFrameTask(ArrayList<Bitmap> frames, Start_Drawing_Screen instance) {
        this.frames = frames;
        this.instance = instance;
    }

    @Override
    protected String doInBackground(Void... params) {
        File f = null;
        for (Bitmap bitmap : frames) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory(), "AnimationDoodle/Temp");
                if (!file.exists()) {
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath() + file.separator + "frame" + image_counter + ".jpg");
                Log.i("Download", "frame (" + image_counter +").jpg downloaded");
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);
                image_counter++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Reset the counter
        image_counter = 1;
        return "Complete";
    }
    @Override
    protected void onPostExecute(String result) {
        Log.i("Download", "JPEG Conversion: " + result);
        // ASYNC TASK(Encode Images)
        Log.i("Download", "Starting JPEG to Video (mp4) conversion");
        DownloadAnimationTask task = new DownloadAnimationTask(instance);
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("Download", "Starting Frame Download...");

    }

    @Override
    protected void onProgressUpdate(Void... values) {}

}


