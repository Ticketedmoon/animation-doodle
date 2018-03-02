package ca326.com.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.io.File;

class DownloadAnimationTask extends AsyncTask<Void, Void, String> {

    private Start_Drawing_Screen instance;
    private ProgressDialog downloading;
    private FFmpeg ffmpeg;
    private File dir;

    // Store Animation Title
    public static String ANIMATION_TITLE;

    public DownloadAnimationTask(Start_Drawing_Screen instance) {
        this.instance = instance;
    }

    @Override
    protected String doInBackground(Void... params) {

        // Verify Storage Permissions
        instance.verifyStoragePermissions(instance);
        dir = new File(Environment.getExternalStorageDirectory() + "/AnimationDoodle/Temp");
        String filePrefix = "frame"; //imagename prefix
        String fileExtn = ".jpg";//image extention
        File src = new File(dir, filePrefix + "%d" + fileExtn);// image name should ne picture001, picture002,picture003 soon  ffmpeg takes as input valid
        Log.i("TEST", src.toString());
        loadFFmpeg();

        String[] complexCommand = new String[]{"-i", src + "", "-c:v", "libx264", "-c:a", "aac", "-vf", "setpts=N/(" + instance.frame_rate_value.toString() + "*TB)", "-pix_fmt", "yuv420p", "-crf", "10", "-r", instance.frame_rate_value.toString(), "-shortest", "-y", "/storage/emulated/0/" + "AnimationDoodle/Animations/" + instance.ANIMATION_TITLE  + ".mp4"};
        executeFFmpeg(complexCommand);

        return "Complete";
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // After show progress Dialog
        downloading = ProgressDialog.show(instance, "Downloading File Locally", "Downloading...", false, false);
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
                public void onProgress(String message) {
                    Log.i("FFmpeg", message);
                }

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {}

                @Override
                public void onFinish() {
                    Log.i("Download", "FFmpeg Execute finished");
                    Log.i("Download", "Download Complete");
                    Toast.makeText(instance.getApplication(), "Animation successfully Downloaded (/sdcard/AnimationDoodle)", Toast.LENGTH_SHORT).show();
                    downloading.dismiss();

                    // Delete tmp folder
                    if (dir.isDirectory())
                    {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++)
                        {
                            new File(dir, children[i]).delete();
                        }
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

}
