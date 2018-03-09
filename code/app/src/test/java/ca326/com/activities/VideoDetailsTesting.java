package ca326.com.activities;

import android.net.Uri;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class VideoDetailsTesting {

    public List<Video> videos;

    @Test
    public void video_validation() throws Exception {
        Video video = new Video();
        video.setName("video name");
        video.setVideoUrl("animationdoodle2017.com/videos/uploads/FreeFire.mp4");
        video.setDescription("video description");
        video.setRatingCounter(2);
        video.setRating(1.0f);
        video.setImageUrl("animationdoodle2017.com/videos/uploads/FreeFire.jpg");

        int ratingCounter = video.getRatingCounter();
        float rating = video.getRating();
        String name = video.getName();
        String description = video.getDescription();
        String imageUrl = video.getImageUrl();
        assertEquals("video description",description);
        assertEquals("animationdoodle2017.com/videos/uploads/FreeFire.jpg",imageUrl);
        assertEquals("video name",name);
        assertEquals(2,ratingCounter);
        assertEquals(1.0f,rating,0.0002);

        assertNotNull(video.getVideoUrl());

        video.setVideoUrl(null);
        assertNull(video.getVideoUrl());
        Video newVideo = new Video();

        newVideo.setName("video name");
        newVideo.setVideoUrl("animationdoodle2017.com/videos/uploads/FreeFire.mp4");
        newVideo.setDescription("video description");
        newVideo.setRatingCounter(2);
        newVideo.setRating(1.0f);
        newVideo.setImageUrl("animationdoodle2017.com/videos/uploads/FreeFire.jpg");

        //This is used to test the average rating of
        // the videos

        ItemThreeFragment screen = new ItemThreeFragment();
        String newRating = "2";
        Float averageRating = newVideo.getRating();
        Integer rating_counter = 4;
        Float ratingInt = null;
        Float ratingValue = screen.changeRating(newRating,ratingInt,averageRating,rating_counter);
        assertEquals(0.6,ratingValue,0.02);

        newVideo.setRating(5.0f);
        averageRating = newVideo.getRating();
        rating_counter = 1;
        newRating = "1";
        ratingValue = screen.changeRating(newRating,ratingInt,averageRating,rating_counter);
        assertEquals(3.0,ratingValue,0.00000);

        newVideo.setRating(5.0f);
        averageRating = newVideo.getRating();
        rating_counter = 1;
        newRating = "0.0";
        ratingValue = screen.changeRating(newRating,ratingInt,averageRating,rating_counter);
        assertEquals(5.0,ratingValue,0.00000);

        videos = new ArrayList<>();
        videos.add(newVideo);

        int size = videos.size();
        assertEquals(1,size);

        videos.remove(0);
        size = videos.size();
        assertEquals(0,size);



    }
}
