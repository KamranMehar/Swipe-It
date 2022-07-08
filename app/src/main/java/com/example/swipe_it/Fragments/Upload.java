package com.example.swipe_it.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.swipe_it.Classes.User;
import com.example.swipe_it.Classes.Video_Model;
import com.example.swipe_it.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Upload extends Fragment {
    FragmentUploadBinding binding;
    boolean isVidePlaying = false;
    Uri videoURI;
    String  description;
    String userId;
    Video_Model video_model;
    String vidID;
    public Upload() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadBinding.inflate(inflater, container, false);
        video_model = new Video_Model();

        userId = FirebaseAuth.getInstance().getUid();
        vidID= UUID.randomUUID().toString();
        video_model.setVideoId(vidID);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Users/" + userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    video_model.setProfile_name(user.getName());
                    video_model.setProfile_url(user.getProfileUrl());
                    video_model.setUserId(user.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        SharedPreferences prefs = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE);
        isVidePlaying = prefs.getBoolean("isPlaying", false);
        if (!isVidePlaying) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, 10);

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
            editor.putBoolean("isPlaying", true);
            editor.apply();
        }

        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        binding.replaceVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, 10);

        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = binding.description.getText().toString();
                if (!description.isEmpty()) {
                    binding.videoView.pause();
                    video_model.setDescription(description);
                    uploadToStorage();
                } else {
                    Toast.makeText(getContext(), "Enter Video Description", Toast.LENGTH_SHORT).show();
                    binding.description.setError("Caption Required");
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            videoURI = data.getData();
            binding.videoView.setVideoURI(videoURI);
            binding.videoView.start();
            isVidePlaying = true;

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
            editor.putBoolean("isPlaying", false);
            editor.apply();
        }
    }

    void uploadToStorage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Video Uploading");
        progressDialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("/Videos/" + userId+"/"+video_model.getVideoId());

        storageReference.putFile(videoURI).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + " %");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                video_model.setVideo_url(uri.toString());
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/Video");
                                reference.child(video_model.getVideoId()).setValue(video_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/SpecificUsersVideos/"+userId+"/Videos");
                                ref.child(video_model.getVideoId()).setValue(video_model).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        binding.description.getText().clear();
                                        binding.description.clearFocus();
                                        binding.videoView.requestFocus();
                                        binding.videoView.start();
                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
        editor.putBoolean("isPlaying", false);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
        editor.putBoolean("isPlaying", false);
        editor.apply();
    }

}