package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("Media")
    @Expose
    private String Media;

    @SerializedName("Media_Type")
    @Expose
    private String Media_Type;

    @SerializedName("Music")
    @Expose
    private String Music;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created")
    @Expose
    private String created;

    public StoryModel(){}

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

    public String getMusic() {
        return Music;
    }

    public void setMusic(String music) {
        Music = music;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
