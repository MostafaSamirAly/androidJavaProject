package com.example.mishwary.ui.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryContract.HistoryView{
    private RecyclerView historyTrips_recyclerView;
    private LinearLayout noTrips_layout;
    private HistoryPresenter historyPresenter;
    private HistoryTripsAdapter adapter;
    private ProgressBar progressBar;
    String id;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        historyTrips_recyclerView = root.findViewById(R.id.historyTrips_recyclerview);
        noTrips_layout = root.findViewById(R.id.no_history_trips_layout);
        progressBar = root.findViewById(R.id.progress_bar);
        historyTrips_recyclerView.setVisibility(View.INVISIBLE);
        if (getArguments() != null) {
            Bundle bundle= getArguments();
            id = bundle.getString("id");
        }
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
    public void onStop() {
        super.onStop();
        historyPresenter.stop();
    }


    @Override
    public void displayTrips(List<Trip> historyTrips) {
        System.out.println("inside  display trips");
        historyTrips_recyclerView.setVisibility(View.VISIBLE);
        noTrips_layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        adapter = new HistoryTripsAdapter(getActivity(),historyTrips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        historyTrips_recyclerView.setLayoutManager(layoutManager);
        historyTrips_recyclerView.setHasFixedSize(true);
        historyTrips_recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayNoTrips() {
        System.out.println("inside  display no trips");
        historyTrips_recyclerView.setVisibility(View.INVISIBLE);
        noTrips_layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}