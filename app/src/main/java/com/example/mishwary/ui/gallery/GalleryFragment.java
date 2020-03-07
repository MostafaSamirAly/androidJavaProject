package com.example.mishwary.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.ui.home.HomePresenter;
import com.example.mishwary.ui.home.UpcomingTripsAdapter;

import java.util.List;

public class GalleryFragment extends Fragment implements HistoryContract.HistoryView{
    private RecyclerView historyTrips_recyclerView;
    private LinearLayout noTrips_layout;
    private HistoryPresenter historyPresenter;
    private HistoryTripsAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        historyTrips_recyclerView = root.findViewById(R.id.historyTrips_recyclerview);
        noTrips_layout = root.findViewById(R.id.no_upcoming_trips_layout);
        historyTrips_recyclerView.setVisibility(View.INVISIBLE);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        historyPresenter = new HistoryPresenter(this);
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
    }
}