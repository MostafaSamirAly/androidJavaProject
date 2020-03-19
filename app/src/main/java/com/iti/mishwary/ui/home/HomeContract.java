package com.iti.mishwary.ui.home;

import com.iti.mishwary.Models.Trip;

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
