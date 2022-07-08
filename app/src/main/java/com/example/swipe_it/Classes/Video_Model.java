package com.example.swipe_it.Classes;

public class Video_Model extends User{
    String  video_url;
    String  profile_url;
    String  profile_name;
    String description;
    String videoId;
    public String getDescription() {
        return description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Video_Model(String video_url, String profile_url, String profile_name, String description, String videoId) {
        this.video_url = video_url;
        this.profile_url = profile_url;
        this.profile_name = profile_name;
        this.description = description;
        this.videoId = videoId;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getProfile_name() {

        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }



    public Video_Model() {
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

}
