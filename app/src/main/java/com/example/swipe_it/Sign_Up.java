package com.example.swipe_it;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.swipe_it.Classes.User;
import com.example.swipe_it.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Sign_Up extends AppCompatActivity {
ActivitySignUpBinding binding;
FirebaseAuth mAuth;
boolean isImage=false;
Uri imageUri;
User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Animation slideDown = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        binding.upperShape.setAnimation(slideDown);

        mAuth = FirebaseAuth.getInstance();
        user=new User();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_Up.this,Login.class);
                startActivity(intent);
            }
        });

        binding.upload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 20);
        });
        binding.showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.passwordEdt2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else {
                    binding.passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.passwordEdt2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        binding.signupBtn.setOnClickListener(v -> {
            if (isImage){
                String name,email,setPassword,confirmPassword;
                name=binding.nameEdt.getText().toString().trim();
                email=binding.emailEdt.getText().toString().trim();
                setPassword=binding.passwordEdt.getText().toString().trim();
                confirmPassword=binding.passwordEdt2.getText().toString().trim();
                if (name.isEmpty()){
                    binding.nameEdt.setError("Required");
                }else {
                    name=binding.nameEdt.getText().toString().trim();
                }
                if (setPassword.isEmpty()){
                    binding.emailEdt.setError("Required");
                }else {
                    email=binding.emailEdt.getText().toString().trim();
                }
                if (setPassword.isEmpty()){
                    binding.passwordEdt.setError("Required");
                }else {
                    setPassword=binding.passwordEdt.getText().toString().trim();
                }
                if (confirmPassword.isEmpty()){
                    binding.passwordEdt2.setError("Required");
                }else {
                    confirmPassword=binding.passwordEdt2.getText().toString().trim();
                }
                if (confirmPassword.equals(setPassword)){
                    user.setEmail(email);
                    user.setName(name);
                    user.setPassword(confirmPassword);
                    signup(user);
                }else {
                    Toast.makeText(this, "Set Password and Confirm Password not Match", Toast.LENGTH_LONG).show();
                }


            }else {
                Toast.makeText(this, "Upload Image", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void signup(User user) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               user.setUserId(mAuth.getUid());

                StorageReference storageReference= FirebaseStorage.getInstance().getReference("/"+user.getUserId()+"/Image");
                storageReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + " %");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                user.setProfileUrl(uri.toString());
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("/Users/"+user.getUserId());
                                myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Sign_Up.this, "Signup Successfully", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(Sign_Up.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Sign_Up.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Sign_Up.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sign_Up.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==20){
            imageUri=data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(binding.profileImage);
            isImage=true;
        }
    }

}