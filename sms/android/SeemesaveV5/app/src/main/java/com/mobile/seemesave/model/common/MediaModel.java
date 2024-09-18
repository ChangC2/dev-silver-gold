package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("Media")
    @Expose
    private String Media;
    @SerializedName("Media_Type")
    @Expose
    private String Media_Type;
    @SerializedName("VideoThumb")
    @Expose
    private String VideoThumb;
    @SerializedName("VideoGIF")
    @Expose
    private String VideoGIF;
    public MediaModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia() {
        return Media;
    }

    public void setMedia(String media) {
        Media = media;
    }

    public String getMedia_Type() {
        return Media_Type;
    }

    public void setMedia_Type(String media_Type) {
        Media_Type = media_Type;
    }

    public String getVideoThumb() {
        return VideoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        VideoThumb = videoThumb;
    }

    public String getVideoGIF() {
        return VideoGIF;
    }

    public void setVideoGIF(String videoGIF) {
        VideoGIF = videoGIF;
    }
}
