package com.example.mishwary.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.mishwary.ui.home.UpcomingTripsAdapter;

import java.util.List;

public class HistoryTripsAdapter extends RecyclerView.Adapter<HistoryTripsAdapter.HistoryTripsViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Trip> historyTrips;

    public HistoryTripsAdapter(Context context, List<Trip> historyTrips) {
        this.context = context;
        this.historyTrips = historyTrips;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HistoryTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcoming_trip_item,parent,false);
        return new HistoryTripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTripsViewHolder holder, final int position) {
        holder.tripTitle.setText(historyTrips.get(position).getTripName());
        holder.tripDate.setText(historyTrips.get(position).getDate());
        holder.tripTime.setText(historyTrips.get(position).getTime());
        holder.tripStart.setText(historyTrips.get(position).getStartPoint());
        holder.tripDestination.setText(historyTrips.get(position).getDestination());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open google maps with start and destination provided with the path

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+historyTrips.get(position).getStartPoint()+"&daddr="+historyTrips.get(position).getDestination()));
                context.startActivity(intent);
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
        return historyTrips.size();
    }

    public class HistoryTripsViewHolder extends RecyclerView.ViewHolder {
        private TextView tripTitle;
        private TextView tripDate;
        private TextView tripTime;
        private TextView tripStart;
        private TextView tripDestination;
        private ImageView menuImg;
        private ImageView notesImg;
        private Button startBtn;

        public HistoryTripsViewHolder(@NonNull View itemView) {
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
