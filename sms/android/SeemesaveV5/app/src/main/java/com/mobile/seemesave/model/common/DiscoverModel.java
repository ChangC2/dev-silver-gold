package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DiscoverModel {
    @SerializedName("story_type")
    @Expose
    private String story_type;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("related_id")
    @Expose
    private int related_id;
    @SerializedName("stories")
    @Expose
    private ArrayList<StoryModel> stories;
    public DiscoverModel(){}

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelated_id() {
        return related_id;
    }

    public void setRelated_id(int related_id) {
        this.related_id = related_id;
    }

    public ArrayList<StoryModel> getStories() {
        return stories;
    }

    public void setStories(ArrayList<StoryModel> stories) {
        this.stories = stories;
    }
}
