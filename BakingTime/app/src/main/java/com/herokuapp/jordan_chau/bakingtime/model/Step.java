package com.herokuapp.jordan_chau.bakingtime.model;

public class Step {
    private int id;
    private String shortDescription, description, videoURL, thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}
