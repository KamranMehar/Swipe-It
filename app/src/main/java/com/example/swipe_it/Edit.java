package com.example.swipe_it;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.swipe_it.Classes.User;
import com.example.swipe_it.databinding.ActivityEditBinding;
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

public class Edit extends AppCompatActivity {
    ActivityEditBinding binding;
    User user;
    Uri uriImage;
    boolean isImage=false;
    String name, about;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> finish());
        Animation swipe_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.upperShape.setAnimation(swipe_up);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/Users/"+userId);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Users/" + userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    assert user != null;
                    try {
                        Toast.makeText(Edit.this, "" + user.getName(), Toast.LENGTH_SHORT).show();
                        Glide.with(Edit.this)
                                .load(user.getProfileUrl())
                                .into(binding.profileImage);
                        binding.profileNameText.setText(user.getName());
                        binding.aboutText.setText(user.getAbout());
                    }catch (Exception e){
                        Toast.makeText(Edit.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 30);
            }
        });

        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = binding.nameEdt.getText().toString();
                about = binding.about.getText().toString();
                if (isImage){
                    updateImage(uriImage);
                }
                 if (!name.isEmpty()){
                    updateName(name);
                }
                 if (!about.isEmpty()){
                    updateAbout(about);
                }
                 if (name.isEmpty() && about.isEmpty() && !isImage){
                     finish();
                 }
            }
        });
    }

    private void updateImage(Uri uriImage) {
         progressDialog=new ProgressDialog(Edit.this);
        progressDialog.setTitle("Updating Profile...");
        progressDialog.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("/"+user.getUserId()+"/Image");
        storageReference.putFile(uriImage).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + (int) progress + " %");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myRef.child("profileUrl").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void updateAbout(String about) {
        binding.about.getText().clear();
        myRef.child("about").setValue(about).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Edit.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName(String name) {
        binding.nameEdt.getText().clear();
        myRef.child("name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Edit.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30) {
            uriImage = data.getData();
            Glide.with(getApplicationContext())
                    .load(uriImage)
                    .into(binding.profileImage);
            isImage=true;
        }
        else {
            isImage=false;
        }
    }
}