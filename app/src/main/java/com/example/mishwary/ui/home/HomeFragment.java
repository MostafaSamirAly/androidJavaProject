package com.example.mishwary.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.ui.addactivity.AddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.HomeView {
    public static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    private RecyclerView upcomingTrips_recyclerView;
    private LinearLayout noTrips_layout;
    private HomePresenter homePresenter;
    private UpcomingTripsAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    String id,name,email;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        upcomingTrips_recyclerView = root.findViewById(R.id.upcomingTrips_recyclerview);
        noTrips_layout = root.findViewById(R.id.no_upcoming_trips_layout);
        progressBar = root.findViewById(R.id.progress_bar);
        if (getArguments() != null) {
            Bundle bundle= getArguments();
           id = bundle.getString("id");
           name = bundle.getString("name");
           email= bundle.getString("email");
        }
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        upcomingTrips_recyclerView.setVisibility(View.INVISIBLE);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        homePresenter = new HomePresenter(this,id);
        progressBar.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        homePresenter.getUpcomingTrips();
    }

    @Override
    public void onStop() {
        super.onStop();
        homePresenter.stop();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void displayTrips(List<Trip> upcomingTrips) {
        upcomingTrips_recyclerView.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        adapter = new UpcomingTripsAdapter(getContext(),upcomingTrips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        upcomingTrips_recyclerView.setLayoutManager(layoutManager);
        upcomingTrips_recyclerView.setHasFixedSize(true);
        upcomingTrips_recyclerView.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void displayNoTrips() {
        System.out.println("inside  display no trips");
        upcomingTrips_recyclerView.setVisibility(View.INVISIBLE);
        noTrips_layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
    }
}