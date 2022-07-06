package com.example.swipe_it.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import com.example.swipe_it.R;
import com.example.swipe_it.databinding.FragmentProfileBinding;


public class Profile extends Fragment {
    FragmentProfileBinding binding;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

      Animation  animationD = AnimationUtils.loadAnimation(getContext(),R.anim.digonal_animation);
        binding.diagonalView.setAnimation(animationD);

        return binding.getRoot();
    }
}