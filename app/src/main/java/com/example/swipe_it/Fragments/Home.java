package com.example.swipe_it.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.swipe_it.Classes.Adapter;
import com.example.swipe_it.Classes.Video_Model;
import com.example.swipe_it.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    FragmentHomeBinding binding;
    List<Video_Model> list = new ArrayList<>();
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


        /*FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Video");*/

        FirebaseRecyclerOptions<Video_Model> options =
                new FirebaseRecyclerOptions.Builder<Video_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("/Video"), Video_Model.class)
                        .build();

        adapter=new Adapter(options,getContext());
        binding.viewPager.setAdapter(adapter);




    /*    databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Video_Model video_model = postSnapshot.getValue(Video_Model.class);
                        list.add(video_model);
                        adapter=new Adapter(getContext(),list);
                        binding.viewPager.setAdapter(adapter);
                        binding.viewPager.canScrollHorizontally(0);
                        binding.viewPager.canScrollVertically(1);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }
}