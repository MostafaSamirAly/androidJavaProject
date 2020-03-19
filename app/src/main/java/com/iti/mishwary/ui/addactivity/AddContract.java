package com.iti.mishwary.ui.addactivity;

import com.iti.mishwary.Models.Trip;

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
