package com.example.mishwary.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;

import java.util.List;

public class UpcomingTripsAdapter extends RecyclerView.Adapter<UpcomingTripsAdapter.UpcomingViewHolder>{
    LayoutInflater inflater;
    Context context;
    List<Trip> upcomingTrips;

    public UpcomingTripsAdapter(Context context, List upcomingTrips) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.upcomingTrips = upcomingTrips;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcoming_trip_item,parent,false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        holder.tripTitle.setText(upcomingTrips.get(position).getTripName());
        holder.tripDate.setText(upcomingTrips.get(position).getDate());
        holder.tripTime.setText(upcomingTrips.get(position).getTime());
        holder.tripStart.setText(upcomingTrips.get(position).getStartPoint());
        holder.tripDestination.setText(upcomingTrips.get(position).getDestination());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open google maps with start and destination provided with the path
            }
        });

        holder.notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open drop down menu wirh the options
            }
        });

        holder.notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open new activity to add notes to the trip
            }
        });

    }

    @Override
    public int getItemCount() {
        return upcomingTrips.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder{
        private TextView tripTitle;
        private TextView tripDate;
        private TextView tripTime;
        private TextView tripStart;
        private TextView tripDestination;
        private ImageView menuImg;
        private ImageView notesImg;
        private Button startBtn;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            tripTitle = itemView.findViewById(R.id.trip_title_text);
            tripDate = itemView.findViewById(R.id.trip_date_text);
            tripTime = itemView.findViewById(R.id.trip_time_text);
            tripStart = itemView.findViewById(R.id.trip_start_text);
            tripDestination = itemView.findViewById(R.id.trip_destination_text);
            menuImg = itemView.findViewById(R.id.image_menu);
            notesImg = itemView.findViewById(R.id.image_notes);
            startBtn = itemView.findViewById(R.id.startBtn);
        }
    }


}
