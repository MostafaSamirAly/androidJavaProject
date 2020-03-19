package com.iti.mishwary.ui.History;

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

import com.iti.mishwary.Models.Trip;
import com.iti.mishwary.R;
import com.iti.mishwary.ui.history_map.HistoryMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements HistoryContract.HistoryView{
    private RecyclerView historyTrips_recyclerView;
    private LinearLayout noTrips_layout;
    private HistoryPresenter historyPresenter;
    private HistoryTripsAdapter adapter;
    private ProgressBar progressBar;
   // private Button showMap;
    String id;
    private FloatingActionButton showMap;
    public  List<String>StartPoints;
    public List<String>EndPoints;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        historyTrips_recyclerView = root.findViewById(R.id.historyTrips_recyclerview);
        noTrips_layout = root.findViewById(R.id.no_history_trips_layout);
        progressBar = root.findViewById(R.id.progress_bar);
        historyTrips_recyclerView.setVisibility(View.INVISIBLE);
        StartPoints = new ArrayList<String>();
        EndPoints =  new ArrayList<String>();
        showMap = root.findViewById(R.id.showMap);
        if (getArguments() != null) {
            Bundle bundle= getArguments();
            id = bundle.getString("id");
        }
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap.setEnabled(false);
                Intent intent = new Intent(getActivity().getApplicationContext(), HistoryMap.class);
                intent.putStringArrayListExtra("startPoints", (ArrayList<String>) StartPoints);
               intent.putStringArrayListExtra("endPoints", (ArrayList<String>) EndPoints);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        historyPresenter = new HistoryPresenter(this,id);
        historyPresenter.getHistoryTrips();
    }
    @Override
    public void onResume() {
        super.onResume();
        showMap.setEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        historyPresenter.stop();
    }



    @SuppressLint("RestrictedApi")
    @Override
    public void displayTrips(List<Trip> historyTrips) {
        System.out.println("inside  display trips");
        historyTrips_recyclerView.setVisibility(View.VISIBLE);
        showMap.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        adapter = new HistoryTripsAdapter(getActivity(),historyTrips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        historyTrips_recyclerView.setLayoutManager(layoutManager);
        historyTrips_recyclerView.setHasFixedSize(true);
        historyTrips_recyclerView.setAdapter(adapter);
        StartPoints=adapter.getStartPoints();
        EndPoints = adapter.getEndPoints();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void displayNoTrips() {
        System.out.println("inside  display no trips");
        showMap.setVisibility(View.INVISIBLE);
        historyTrips_recyclerView.setVisibility(View.INVISIBLE);
        noTrips_layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}