package com.example.mishwary.ui.home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.MainActivity;
import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.ui.addactivity.AlertReceiver;
import com.example.mishwary.ui.edittrip.EditTrip;
import com.example.mishwary.ui.floatingwidget.FloatingWidgetService;
import com.example.mishwary.ui.notes.AddNote;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class UpcomingTripsAdapter extends RecyclerView.Adapter<UpcomingTripsAdapter.UpcomingViewHolder>{
    LayoutInflater inflater;
    Context context;
    List<Trip> upcomingTrips;
    public static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

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
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, final int position) {
        holder.tripTitle.setText(upcomingTrips.get(position).getTripName());
        holder.tripDate.setText(upcomingTrips.get(position).getDate());
        holder.tripTime.setText(upcomingTrips.get(position).getTime());
        holder.tripStart.setText(upcomingTrips.get(position).getStartPoint());
        holder.tripDestination.setText(upcomingTrips.get(position).getDestination());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromUpcoming(upcomingTrips.get(position));
                addToHistory(upcomingTrips.get(position));
                cancelAlarm(position);
                //floating icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays((context))) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + (context).getPackageName()));
                    ((MainActivity) context).startActivityForResult(intent,DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                } else
                    //If permission is granted start floating widget service
                    startFloatingWidgetService(position);
                // open google maps with start and destination provided with the path
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+upcomingTrips.get(position).getStartPoint()+"&daddr="+upcomingTrips.get(position).getDestination()));
                context.startActivity(intent);
            }
        });

        holder.menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open drop down menu wirh the options
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.inflate(R.menu.popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_item:
                                deleteTrip(upcomingTrips.get(position));
                                cancelAlarm(position);
                                return true;
                            case R.id.edit_item:
                                Intent intent = new Intent(context, EditTrip.class);
                                intent.putExtra("id",upcomingTrips.get(position).getUserId());
                                intent.putExtra("title",upcomingTrips.get(position).getTripName());
                                intent.putExtra("date",upcomingTrips.get(position).getDate());
                                intent.putExtra("desc",upcomingTrips.get(position).getDescription());
                                intent.putExtra("start",upcomingTrips.get(position).getStartPoint());
                                intent.putExtra("dest",upcomingTrips.get(position).getDestination());
                                intent.putExtra("tripId",upcomingTrips.get(position).getId());
                                intent.putExtra("repeat",upcomingTrips.get(position).getRepeat());
                                intent.putExtra("time",upcomingTrips.get(position).getTime());
                                context.startActivity(intent);
                                return true;
                            case R.id.cancel_item:
                                removeFromUpcoming(upcomingTrips.get(position));
                                addToHistory(upcomingTrips.get(position));
                                return true;
                            case R.id.showNotes_item:
                                Intent Noteintent = new Intent(context, AddNote.class);
                                Noteintent.putExtra("tripId",upcomingTrips.get(position).getId());
                                context.startActivity(Noteintent);
                                Toast.makeText(context, "showNotes_item", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        holder.notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent Noteintent = new Intent(context, AddNote.class);
                Noteintent.putExtra("tripId",upcomingTrips.get(position).getId());
                context.startActivity(Noteintent);
            }
        });

    }

    private void startFloatingWidgetService(int position) {
        Intent intent = new Intent(context, FloatingWidgetService.class);
        intent.putExtra("tripId",upcomingTrips.get(position).getId());
        intent.putExtra("tripTitle",upcomingTrips.get(position).getTripName());
        context.startService(intent);
    }

    private void addToHistory(Trip trip) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("history_trip").child(trip.getUserId());
        databaseReference.child(trip.getId()).setValue(trip);
    }
    private void deleteTrip(Trip trip) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(trip.getUserId());
        databaseReference.child(trip.getId()).removeValue();
        databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(trip.getId());
        databaseReference.removeValue();
    }
    private void removeFromUpcoming(Trip trip) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(trip.getUserId());
        databaseReference.child(trip.getId()).removeValue();
    }
    private void cancelAlarm(int pos) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, upcomingTrips.get(pos).getId().hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
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
