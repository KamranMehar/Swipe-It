package com.example.swipe_it.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swipe_it.Classes.Adapter;
import com.example.swipe_it.Classes.Video_Model;
import com.example.swipe_it.R;
import com.example.swipe_it.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

FragmentHomeBinding binding;
    List<Video_Model> list=new ArrayList<>();
    Adapter adapter;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        list.add(new Video_Model("android.resource://" + getContext().getPackageName() + "/" + R.raw.a,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getContext().getPackageName() + "/" + R.raw.b,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getContext().getPackageName() + "/" + R.raw.c,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getContext().getPackageName() + "/" + R.raw.d,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" + getContext().getPackageName()+ "/" + R.raw.e,
                R.drawable.my_profile,"Kamran Mehar"));
        list.add(new Video_Model("android.resource://" +getContext().getPackageName()+ "/" + R.raw.f,
                R.drawable.my_profile,"Kamran Mehar"));
        adapter=new Adapter(getContext(),list);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.canScrollHorizontally(0);
        binding.viewPager.canScrollVertically(1);








        return binding.getRoot();
    }
}