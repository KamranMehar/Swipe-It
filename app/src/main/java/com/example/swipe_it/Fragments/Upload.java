package com.example.swipe_it.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swipe_it.R;
import com.example.swipe_it.databinding.FragmentHomeBinding;
import com.example.swipe_it.databinding.FragmentUploadBinding;

public class Upload extends Fragment {
FragmentUploadBinding binding;
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






        return binding.getRoot();
    }
}