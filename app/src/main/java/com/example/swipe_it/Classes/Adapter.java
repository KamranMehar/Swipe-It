package com.example.swipe_it.Classes;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swipe_it.R;
import com.example.swipe_it.databinding.ReelDesignBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Adapter extends FirebaseRecyclerAdapter<Video_Model, Adapter.ViewHolder> {
    boolean isPlayed = false;
    Context context;
    int seekTime;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    public Adapter(@NonNull FirebaseRecyclerOptions<Video_Model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video_Model model) {
         firebaseDatabase = FirebaseDatabase.getInstance();
         databaseReference = firebaseDatabase.getReference("/Users/" + model.getUserId()+"/name");

        if (Objects.equals(model.getUserId(), "9xP5sUpSVtY1cWOXMYhltPrp6Pp1")) {
            holder.binding.verified.setVisibility(View.VISIBLE);
        } else {
            holder.binding.verified.setVisibility(View.INVISIBLE);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.profileName.setText( snapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        holder.binding.playPause.setVisibility(View.INVISIBLE);
        holder.binding.videoView.setVideoPath(model.getVideo_url());
        Glide.with(context).load(model.getProfile_url()).into(holder.binding.profileImage);

        holder.binding.description.setText(model.getDescription());
        holder.binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                isPlayed = true;

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        holder.binding.playPause.setVisibility(View.INVISIBLE);
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            holder.binding.mProgressBar.setVisibility(View.VISIBLE);
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            holder.binding.mProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayed) {
                    holder.binding.videoView.requestFocus();
                    holder.binding.videoView.pause();
                    isPlayed = false;
                    holder.binding.playPause.setVisibility(View.VISIBLE);
                    seekTime = holder.binding.videoView.getCurrentPosition();
                } else {
                    holder.binding.videoView.requestFocus();
                    holder.binding.videoView.seekTo(seekTime);
                    holder.binding.videoView.start();
                    isPlayed = true;
                    holder.binding.playPause.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayed) {
                    holder.binding.videoView.requestFocus();
                    holder.binding.videoView.pause();
                    isPlayed = false;
                    holder.binding.playPause.setVisibility(View.VISIBLE);
                    seekTime = holder.binding.videoView.getCurrentPosition();
                } else {
                    holder.binding.videoView.requestFocus();
                    holder.binding.videoView.seekTo(seekTime);
                    holder.binding.videoView.start();
                    isPlayed = true;
                    holder.binding.playPause.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reel_design, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ReelDesignBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReelDesignBinding.bind(itemView);

        }
    }

   void getVideoUserName(String userId, TextView profileName) {

    }
}
