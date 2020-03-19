package com.iti.mishwary.ui.History;

import com.iti.mishwary.Models.Trip;

import java.util.List;

public interface HistoryContract {
    public  interface  HistoryView{
        void displayTrips(List<Trip> historyTrips);
        void displayNoTrips();

    }
    public  interface  HistoryPresenter{
        void getHistoryTrips();
        void stop();
    }
}
