package com.example.swipe_it.Classes;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swipe_it.R;
import com.example.swipe_it.databinding.ReelDesignBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
Context context;
List<Video_Model> videoList;
boolean isPlayed=false;
int seekTime;
    public Adapter(Context context, List<Video_Model> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.reel_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video_Model model=videoList.get(position);

        holder.binding.videoView.setVideoPath(model.getVideo_url());
        Glide.with(context).load(model.getProfile_url()).into(holder.binding.profileImage);
        holder.binding.profileName.setText(model.getProfile_name());

        holder.binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                isPlayed=true;
                    holder.binding.playPause.setVisibility(View.INVISIBLE);
            }
        });
        holder.binding.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayed){
                    holder.binding.videoView.pause();
                    isPlayed=false;
                    holder.binding.playPause.setVisibility(View.VISIBLE);
                    seekTime=holder.binding.videoView.getCurrentPosition();
                }else {
                    holder.binding.videoView.seekTo(seekTime);
                    holder.binding.videoView.start();
                    isPlayed=true;
                    holder.binding.playPause.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ReelDesignBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReelDesignBinding.bind(itemView);



        }
    }
}
