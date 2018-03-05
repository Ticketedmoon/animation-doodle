package ca326.com.activities;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExampleUnitTest {

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
    }
}