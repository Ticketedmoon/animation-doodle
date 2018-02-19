package ca326.com.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class Test_VideoPlayer extends AppCompatActivity {


    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__video_player);

        videoView = (VideoView) findViewById(R.id.videoViews);

        Video video = Top_Rated_Screen.listVideos.get(0);
        System.out.println("video" + video.getVideoUrl());
        String url = video.getVideoUrl();
        Uri videoUri = Uri.parse(url);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

}
