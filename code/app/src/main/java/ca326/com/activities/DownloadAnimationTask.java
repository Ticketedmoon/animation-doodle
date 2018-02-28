package ca326.com.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.IOException;

class DownloadAnimationTask extends AsyncTask<Void, Void, String> {

    private Start_Drawing_Screen instance;
    private ProgressDialog downloading;
    private FFmpeg ffmpeg;
    private int frame_num = 1;

    public DownloadAnimationTask(Start_Drawing_Screen instance) {
        this.instance = instance;
        this.downloading = downloading;
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
        File dir = new File(Environment.getExternalStorageDirectory() + "/AnimationDoodle/Temp");
        String filePrefix = "frame"; //imagename prefix
        String fileExtn = ".jpg";//image extention
        File src = new File(dir, filePrefix + "%d" + fileExtn);// image name should ne picture001, picture002,picture003 soon  ffmpeg takes as input valid
        Log.i("TEST", src.toString());
        loadFFmpeg();
        String [] complexCommand = new String[]{"-i", src + "", "-c:v", "libx264", "-c:a", "aac", "-vf", "setpts=2*PTS", "-pix_fmt", "yuv420p", "-crf", "10", "-r", instance.frame_rate_value.toString(), "-shortest", "-y", "/storage/emulated/0/" + "AnimationDoodle/" + "Video" + frame_num +".mp4"};
        executeFFmpeg(complexCommand);

        return "Complete";
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downloading = ProgressDialog.show(this.instance, "Downloading File Locally", "Downloading...", false, false);
        Log.i("Download", "Starting Download...");
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private void loadFFmpeg() {
        ffmpeg = FFmpeg.getInstance(instance.getApplicationContext());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

    private void executeFFmpeg(String[] cmd)
    {
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {}

                @Override
                public void onFinish() {
                    Log.i("Download", "FFmpeg Execute finished");
                    Log.i("Download", result);
                    Toast.makeText(instance.getApplication(), "Animation successfully Downloaded (/sdcard/AnimationDoodle)", Toast.LENGTH_SHORT).show();
                    downloading.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

}

