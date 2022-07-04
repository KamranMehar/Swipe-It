package com.example.swipe_it;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.swipe_it.Classes.Adapter;
import com.example.swipe_it.Classes.Video_Model;
import com.example.swipe_it.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
List<Video_Model> list=new ArrayList<>();
Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.a,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.b,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.c,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.d,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.e,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getPackageName() + "/" + R.raw.f,
                R.drawable.my_profile,"Kamran Mehar"));
        adapter=new Adapter(this,list);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.canScrollHorizontally(0);
        binding.viewPager.canScrollVertically(1);
    }
}