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

import com.example.swipe_it.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends Fragment {
FragmentUploadBinding binding;
boolean isVidePlaying=false;
    Uri videoURI;
    String videoUrl,description;

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

        SharedPreferences prefs = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE);
        isVidePlaying = prefs.getBoolean("isPlaying", false);
       if (!isVidePlaying) {
           Intent intent = new Intent(Intent.ACTION_PICK);
           intent.setType("video/*");
           startActivityForResult(intent, 10);

           SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
           editor.putBoolean("isPlaying",true);
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
            description= binding.description.getText().toString();
              if (!description.isEmpty()) {
                  binding.videoView.pause();
                  uploadToStorage(videoURI);
              }else {
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
                isVidePlaying=true;

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
                editor.putBoolean("isPlaying",false);
                editor.apply();
        }
    }
    void uploadToStorage(Uri uri){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Video Uploading");
        progressDialog.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("/Videos/"+description);

        storageReference.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                                videoUrl=uri.toString();
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("/Video");
                                reference.child(description).setValue(videoUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        binding.videoView.resume();
                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
        editor.putBoolean("isPlaying",false);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("upload", getActivity().MODE_PRIVATE).edit();
        editor.putBoolean("isPlaying",false);
        editor.apply();
    }

}