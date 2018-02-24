package ca326.com.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import static ca326.com.activities.Profile_Screen.deciding_string;
import static ca326.com.activities.Profile_Screen.position2;
import static ca326.com.activities.Top_Rated_Screen.position;

public class Test_VideoPlayer extends AppCompatActivity {


    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_test__video_player);

        videoView = (VideoView) findViewById(R.id.videoViews);

        Video video;


        if (deciding_string.equalsIgnoreCase("profile") ){
            video = Profile_Screen.listVideos.get(position2);
        }
        else{
            video = Top_Rated_Screen.listVideos.get(position);
        }
        System.out.println("position :"+ position2 + "  " + video.getVideoUrl());
        String url = video.getVideoUrl();
        Uri videoUri = Uri.parse(url);
        videoView.setVideoURI(videoUri);

        //changes the screen to white from black as video loads
        videoView.setZOrderOnTop(true);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                //continually plays video until back button pressed
                mp.setLooping(true);
            }
        });
    }

}
