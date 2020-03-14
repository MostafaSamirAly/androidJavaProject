package com.example.mishwary.ui.addactivity;

import com.example.mishwary.Models.Trip;

public interface AddContract {
    public interface AddView{
        void setTripId(String tripId);
        void gotoHome();
    }
    public interface AddPresenter{
        void addTrip(Trip trip);
        void editTrip(Trip trip);
        void stop();
    }
}
