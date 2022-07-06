package com.example.swipe_it;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swipe_it.Fragments.Home;
import com.example.swipe_it.Fragments.Profile;
import com.example.swipe_it.Fragments.Upload;
import com.example.swipe_it.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Home home_Fragment=new Home();
        Profile profile_fragment=new Profile();
        Upload upload_fragment=new Upload();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, home_Fragment).commit();


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profile_fragment ).commit();
                        return true;

                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, home_Fragment).commit();
                        return true;
                }
                return false;
            }
        });

        binding.upload.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, upload_fragment).commit();
        });


       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/
    }

}