package com.example.mishwary.ui.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.main.MainActivity;
import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.ui.floatingwidget.FloatingWidgetService;
import com.example.mishwary.ui.home.UpcomingTripsAdapter;
import com.example.mishwary.ui.notes.AddNote;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HistoryTripsAdapter extends RecyclerView.Adapter<HistoryTripsAdapter.HistoryTripsViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Trip> historyTrips;

    public List<String> getStartPoints() {
        return StartPoints;
    }

    public List<String> getEndPoints() {
        return EndPoints;
    }

    List<String>StartPoints;
    List<String>EndPoints;

    public HistoryTripsAdapter(Context context, List<Trip> historyTrips) {
        this.context = context;
        this.historyTrips = historyTrips;
        this.StartPoints=new ArrayList<>();
        this.EndPoints =new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HistoryTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_trip_item,parent,false);
        return new HistoryTripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryTripsViewHolder holder, final int position) {
        holder.tripTitle.setText(historyTrips.get(position).getTripName());
        holder.tripDate.setText(historyTrips.get(position).getDate());
        holder.tripTime.setText(historyTrips.get(position).getTime());
        holder.tripStart.setText(historyTrips.get(position).getStartPoint());
        holder.tripDestination.setText(historyTrips.get(position).getDestination());
        StartPoints.add(historyTrips.get(position).getStartPoint());
        EndPoints.add(historyTrips.get(position).getDestination());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //floating icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays((context))) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + (context).getPackageName()));
                    ((MainActivity) context).startActivityForResult(intent, UpcomingTripsAdapter.DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                } else
                    //If permission is granted start floating widget service
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context)) {
                            startFloatingWidgetService(position);
                        }
                    }
                // open google maps with start and destination provided with the path
                if(historyTrips.get(position).getStartPoint().equals("At Start Location")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+historyTrips.get(position).getDestination()));
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+historyTrips.get(position).getStartPoint()+"&daddr="+historyTrips.get(position).getDestination()));
                    context.startActivity(intent);
                }
            }
        });
        holder.notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open new activity to add notes to the trip
                Intent Noteintent = new Intent(context, AddNote.class);
                Noteintent.putExtra("tripId",historyTrips.get(position).getId());
                context.startActivity(Noteintent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.detailsLayout.getVisibility() == View.GONE){
                    holder.detailsLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.detailsLayout.setVisibility(View.GONE);
                }
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAlert(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyTrips.size();
    }
    private void startFloatingWidgetService(int position) {
        Intent intent = new Intent(context, FloatingWidgetService.class);
        intent.putExtra("tripId",historyTrips.get(position).getId());
        intent.putExtra("tripTitle",historyTrips.get(position).getTripName());
        context.startService(intent);
    }
    private void deleteTrip(Trip trip) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("history_trip").child(trip.getUserId());
        databaseReference.child(trip.getId()).removeValue();
        databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(trip.getId());
        databaseReference.removeValue();
    }
    private void showDeleteAlert(final int pos) {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(context);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you Want to delete "+historyTrips.get(pos).getTripName()+" ???");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTrip(historyTrips.get(pos));
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert=alertdialog.create();
        alertdialog.show();

    }
    public class HistoryTripsViewHolder extends RecyclerView.ViewHolder {
        private TextView tripTitle;
        private TextView tripDate;
        private TextView tripTime;
        private TextView tripStart;
        private TextView tripDestination;
        private ImageView notesImg;
        private ImageView imageView;
        private Button startBtn;
        private Button deleteBtn;
        private LinearLayout detailsLayout;

        public HistoryTripsViewHolder(@NonNull View itemView) {
            super(itemView);
            tripTitle = itemView.findViewById(R.id.trip_title_text);
            tripDate = itemView.findViewById(R.id.trip_date_text);
            tripTime = itemView.findViewById(R.id.trip_time_text);
            tripStart = itemView.findViewById(R.id.trip_start_text);
            tripDestination = itemView.findViewById(R.id.trip_destination_text);
            notesImg = itemView.findViewById(R.id.image_notes);
            startBtn = itemView.findViewById(R.id.startBtn);
            imageView = itemView.findViewById(R.id.imageView);
            detailsLayout = itemView.findViewById(R.id.details_Layout);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

    }
}
