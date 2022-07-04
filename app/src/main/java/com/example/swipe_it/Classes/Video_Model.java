package com.example.swipe_it.Classes;

public class Video_Model {
    String  video_url;
    int  profile_url;
    String  profile_name;

    public String getProfile_name() {

        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public Video_Model(String video_url, int profile_url,  String profile_name) {
        this.video_url = video_url;
        this.profile_url = profile_url;
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

    public int getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(int profile_url) {
        this.profile_url = profile_url;
    }

}
