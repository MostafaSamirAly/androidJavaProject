package com.example.mishwary.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.ui.addactivity.AddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.HomeView {
    private RecyclerView upcomingTrips_recyclerView;
    private LinearLayout noTrips_layout;
    private HomePresenter homePresenter;
    private UpcomingTripsAdapter adapter;
    String id,name,email;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homePresenter = new HomePresenter(this);
        upcomingTrips_recyclerView = root.findViewById(R.id.upcomingTrips_recyclerview);
        noTrips_layout = root.findViewById(R.id.no_upcoming_trips_layout);
        if (getArguments() != null) {
            Bundle bundle= getArguments();
           id = bundle.getString("id");
           name = bundle.getString("name");
           email= bundle.getString("email");
            Toast.makeText(this.getActivity(), " id "+id +" name  "+name+" email "+email, Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = root.findViewById(R.id.fab);
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

        homePresenter = new HomePresenter(this);
        homePresenter.getUpcomingTrips();
    }

    @Override
    public void onStop() {
        super.onStop();
        homePresenter.stop();
    }


    @Override
    public void displayTrips(List<Trip> upcomingTrips) {
        System.out.println("inside  display trips");
        upcomingTrips_recyclerView.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        adapter = new UpcomingTripsAdapter(getActivity(),upcomingTrips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        upcomingTrips_recyclerView.setLayoutManager(layoutManager);
        upcomingTrips_recyclerView.setHasFixedSize(true);
        upcomingTrips_recyclerView.setAdapter(adapter);

    }

    @Override
    public void displayNoTrips() {
        System.out.println("inside  display no trips");
        upcomingTrips_recyclerView.setVisibility(View.INVISIBLE);
        noTrips_layout.setVisibility(View.VISIBLE);
    }
}