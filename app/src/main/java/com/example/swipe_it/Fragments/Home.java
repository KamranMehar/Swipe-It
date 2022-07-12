package com.example.swipe_it.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.swipe_it.Classes.Adapter;
import com.example.swipe_it.Classes.Video_Model;
import com.example.swipe_it.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends Fragment {
    FragmentHomeBinding binding;
    Adapter adapter;
    int itemPosition;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    List<Video_Model> modelList;

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


         recyclerView = binding.recyclerView;
         LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
         layoutManager.setOrientation(RecyclerView.VERTICAL);
         recyclerView.setLayoutManager(layoutManager);

         SnapHelper mSnapHelper = new PagerSnapHelper();
         mSnapHelper.attachToRecyclerView(recyclerView);


        modelList=new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Video");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Video_Model model = postSnapshot.getValue(Video_Model.class);
                    modelList.add(model);
                adapter=new Adapter(modelList,getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "onCancelled: ",error.getMessage() );
            }
        });


        return binding.getRoot();

    }
/*

    @Override
    public void onStart() {
        super.onStart();
     SharedPreferences prefs = requireActivity().getSharedPreferences("Position", Context.MODE_PRIVATE);
        itemPosition = prefs.getInt("PositionItem", 0);
        Objects.requireNonNull(binding.recyclerView.getLayoutManager()).scrollToPosition(itemPosition);
        Log.e("onStart: ", "onStart Called");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("onStop: ", "onStop Called");

        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Position", Context.MODE_PRIVATE).edit();
        editor.putInt("PositionItem", adapter.getItemPosition());
        editor.apply();
    }
*/

   @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause: ", "On Pause called");
       itemPosition= adapter.getItemPosition();
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Position", Context.MODE_PRIVATE).edit();
        editor.putInt("PositionItem",itemPosition);
        editor.apply();
        Log.e("onPause: ", "Position Saved : " + itemPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onPause: ", "On Resume called");

        SharedPreferences prefs = requireActivity().getSharedPreferences("Position", Context.MODE_PRIVATE);
        itemPosition = prefs.getInt("PositionItem", 0);
       Objects.requireNonNull(binding.recyclerView.getLayoutManager()).scrollToPosition(itemPosition-1);
        Log.e("onResume: ", "Position get ___"+itemPosition);
    }
}