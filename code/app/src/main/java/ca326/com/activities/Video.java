package ca326.com.activities;

public class Video {

    //Data Variables
    private String videoUrl;
    private String imageUrl;
    private String name;
    private String description;
    private Float rating;
    private Integer ratingCounter;

    //Getters and Setters
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getRatingCounter() {
        return ratingCounter;
    }

    public void setRatingCounter(Integer ratingCounter) {
        this.ratingCounter = ratingCounter;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
