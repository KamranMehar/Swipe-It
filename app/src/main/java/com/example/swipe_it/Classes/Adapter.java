package com.example.swipe_it.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.swipe_it.Comments;
import com.example.swipe_it.R;

import com.example.swipe_it.databinding.ReelDesignBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    boolean isPlayed = false;
    Context context;
    int seekTime,positionItem;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    List<Video_Model> list;
    Boolean isLiked = false;
    String userId;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    DatabaseReference likeReference = FirebaseDatabase.getInstance().getReference("/Likes/");
    public Adapter(List<Video_Model> list, Context context) {
      this.list=list;
        this.context = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reel_design, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Video_Model model=list.get(position);

       userId= FirebaseAuth.getInstance().getUid();



        likeReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(model.videoId).hasChild(userId)){
                    holder.binding.like.setColorFilter(context.getResources().getColor(R.color.red));
                    int count=  (int)snapshot.child(model.videoId).getChildrenCount();
                    holder.binding.likesCount.setText(count+"");
                }else {
                    holder.binding.like.setColorFilter(context.getResources().getColor(R.color.white));
                    int count=  (int)snapshot.child(model.videoId).getChildrenCount();
                    holder.binding.likesCount.setText(count+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked=true;
                final Drawable drawable=holder.binding.like.getDrawable();
                if (drawable instanceof  AnimatedVectorDrawableCompat){
                    avd=(AnimatedVectorDrawableCompat) drawable;
                   avd.start();

                }else if (drawable instanceof AnimatedVectorDrawable){
                    avd2=(AnimatedVectorDrawable) drawable;
                    avd2.start();

                }

                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isLiked){
                            if (snapshot.child(model.videoId).hasChild(userId)){
                                likeReference.child(model.videoId).child(userId).removeValue();
                                isLiked=false;
                            }else {
                                likeReference.child(model.videoId).child(userId).setValue(userId);
                                isLiked=false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Users/" + model.getUserId());

          positionItem= holder.getBindingAdapterPosition();

        holder.binding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Comments.class);
                intent.putExtra("VideoId",model.getVideoId());
                intent.putExtra("VideoUserId",model.getVideoId());
                context.startActivity(intent);
            }
        });

        if (Objects.equals(model.getUserId(), "9xP5sUpSVtY1cWOXMYhltPrp6Pp1")) {
            holder.binding.verified.setVisibility(View.VISIBLE);
        } else {
            holder.binding.verified.setVisibility(View.INVISIBLE);
        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user=snapshot.getValue(User.class);
                    assert user != null;
                    holder.binding.profileName.setText(user.getName());
                    Glide.with(context).load(user.getProfileUrl()).into(holder.binding.profileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.binding.playPause.setVisibility(View.INVISIBLE);
        holder.binding.videoView.setVideoPath(model.getVideo_url());
        holder.binding.description.setText(model.getDescription());
        holder.binding.videoView.setOnPreparedListener(mp -> {
            mp.start();
            mp.setLooping(true);
            isPlayed = true;

            mp.setOnInfoListener((mp1, what, extra) -> {
                holder.binding.playPause.setVisibility(View.INVISIBLE);
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                    holder.binding.mProgressBar.setVisibility(View.VISIBLE);
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                    holder.binding.mProgressBar.setVisibility(View.INVISIBLE);
                return false;
            });
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

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference reference=db.getReference("/Comments/"+model.videoId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.binding.commentsCount.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       DatabaseReference ref=db.getReference("/Likes/"+model.videoId);
       ref.addValueEventListener(new ValueEventListener() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               holder.binding.likesCount.setText(snapshot.getChildrenCount()+"");
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        ReelDesignBinding binding;
        public Holder(@NonNull View itemView) {
            super(itemView);
            binding = ReelDesignBinding.bind(itemView);
        }
    }
    public int getItemPosition(){
        return positionItem;
    }
}
