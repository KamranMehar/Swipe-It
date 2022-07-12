package com.example.swipe_it.Classes;

public class Comment extends User{
    String comment_id;
    String comment_text;
    String videoUserId;
    public Comment(String comment_id, String comment_text,String videoUserId) {
        this.comment_id = comment_id;
        this.comment_text = comment_text;
        this.videoUserId=videoUserId;
    }

    public Comment() {
    }

    public String getVideoUserId() {
        return videoUserId;
    }

    public void setVideoUserId(String videoUserId) {
        this.videoUserId = videoUserId;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }
}
