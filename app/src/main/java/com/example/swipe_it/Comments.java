package com.example.swipe_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swipe_it.Classes.Comment;
import com.example.swipe_it.Classes.CommentAdapter;
import com.example.swipe_it.databinding.ActivityCommentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comments extends AppCompatActivity {
    ActivityCommentsBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String userid;
    String commentText;
    String videoUserId;
    Comment comment;
    String videoId;
    CommentAdapter adapter;
    RecyclerView recyclerView;
    List<Comment> list;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list=new ArrayList<>();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        binding.back.setOnClickListener(v -> finish());



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        comment = new Comment();
        comment.setUserId(userid);
        Intent intent = getIntent();
        videoId = intent.getStringExtra("VideoId");
        videoUserId = intent.getStringExtra("VideoUserId");

        comment.setVideoUserId(videoUserId);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Users/" + userid);


        comment.setComment_id(UUID.randomUUID().toString());


        binding.addComment.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.addComment.getRight() - binding.addComment.getTotalPaddingRight()) {
                    commentText = binding.addComment.getText().toString();
                    if (!commentText.isEmpty()) {
                        addComment(binding.addComment.getText().toString());
                    } else {
                        Toast.makeText(Comments.this, "Empty Comment not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }

            return false;
        });
        adapter=new CommentAdapter();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("/Comments/"+videoId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(Comment.class));
                    adapter.setCommentList(list);
                    adapter.setContext(Comments.this);
                    adapter.setCurrentUser(userid);
                    adapter.setVideoId(videoId);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void addComment(String commentText) {
        comment.setComment_text(commentText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Comments/" + videoId);
        databaseReference.child(comment.getComment_id()).setValue(comment).addOnSuccessListener(unused -> {
            Toast.makeText(Comments.this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();
            binding.addComment.getText().clear();
           adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(Comments.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
}