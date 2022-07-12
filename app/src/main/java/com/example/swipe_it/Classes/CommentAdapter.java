package com.example.swipe_it.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swipe_it.R;

import com.example.swipe_it.databinding.CommentItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    List<Comment> commentList;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String currentUser;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    String videoId;
    public CommentAdapter(List<Comment> commentList, Context context,String videoId) {
        this.commentList = commentList;
        this.context = context;
        this.videoId=videoId;
    }

    public CommentAdapter() {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Comment model = commentList.get(position);
        holder.binding.commentText.setText(model.getComment_text());

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getUid();

        if (model.getUserId().equals(currentUser ) || model.getVideoUserId().equals(currentUser)){
            holder.binding.deleteComment.setVisibility(View.VISIBLE);
        }else {
            holder.binding.deleteComment.setVisibility(View.INVISIBLE);
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users/"+model.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                assert user != null;
                holder.binding.userName.setText(user.getName());
                Glide.with(context).load(user.getProfileUrl()).into(holder.binding.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(model.getComment_id(),videoId,holder.getAdapterPosition());
            }
        });
    }

    private void delete(String comment_id, String videoId,int position) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Comments/"+videoId+"/"+comment_id);
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

     class Holder extends RecyclerView.ViewHolder {
        CommentItemBinding binding;

        public Holder(@NonNull View itemView) {
            super(itemView);
            binding = CommentItemBinding.bind(itemView);
        }
    }
}
