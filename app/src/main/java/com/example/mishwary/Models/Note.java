package com.example.mishwary.Models;

public class Note {
    private String id;
    private String description;
    private String TripId;

    public Note() {
    }

    public Note(String id, String description, String tripId) {
        this.id = id;
        this.description = description;
        TripId = tripId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }
}
