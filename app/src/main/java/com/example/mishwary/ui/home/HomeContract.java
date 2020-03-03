package com.example.mishwary.ui.home;

import com.example.mishwary.Models.Trip;

import java.util.List;

public interface HomeContract {
    public  interface  HomeView{
        void displayTrips(List<Trip> upcomingTrips);
        void displayNoTrips();

    }
    public  interface  HomePresenter{
        void getUpcomingTrips();
        void stop();
    }
}
