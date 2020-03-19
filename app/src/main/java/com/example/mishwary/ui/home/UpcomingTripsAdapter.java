package com.example.mishwary.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.example.mishwary.main.MainActivity;
import com.example.mishwary.ui.addactivity.AlertReceiver;
import com.example.mishwary.ui.edittrip.EditTrip;
import com.example.mishwary.ui.floatingwidget.FloatingWidgetService;
import com.example.mishwary.ui.notes.AddNote;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpcomingTripsAdapter extends RecyclerView.Adapter<UpcomingTripsAdapter.UpcomingViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Trip> upcomingTrips;
    public static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    FusedLocationProviderClient mFusedLocationClient;
    Activity activity;
    private double longitude;
    private double latitude;
    Geocoder geocoder;
    List<Address> addresses;
    String loc;


    public UpcomingTripsAdapter(Activity activity, Context context, List upcomingTrips) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.upcomingTrips = upcomingTrips;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.activity = activity;
        geocoder = new Geocoder(context);
        addresses = new ArrayList<>();
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.upcoming_trip_item, parent, false);
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cancelAlarm(position);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                //floating icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(context)) {
                        startFloatingWidgetService(position);
                    }
                }
                // open google maps with start and destination provided with the path
                if (upcomingTrips.get(position).getStartPoint().equals("At Start Location")) {
                    getLoc(upcomingTrips.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + "&daddr=" + upcomingTrips.get(position).getDestination()));
                    context.startActivity(intent);
                } else {
                    addToHistory(upcomingTrips.get(position));
                    removeFromUpcoming(upcomingTrips.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + upcomingTrips.get(position).getStartPoint() + "&daddr=" + upcomingTrips.get(position).getDestination()));
                    context.startActivity(intent);
                }
            }
        });

        holder.menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open drop down menu wirh the options
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_item:
                                showDeleteAlert(position);
                                return true;
                            case R.id.edit_item:
                                Intent intent = new Intent(context, EditTrip.class);
                                intent.putExtra("id", upcomingTrips.get(position).getUserId());
                                intent.putExtra("title", upcomingTrips.get(position).getTripName());
                                intent.putExtra("date", upcomingTrips.get(position).getDate());
                                intent.putExtra("desc", upcomingTrips.get(position).getDescription());
                                intent.putExtra("start", upcomingTrips.get(position).getStartPoint());
                                intent.putExtra("dest", upcomingTrips.get(position).getDestination());
                                intent.putExtra("tripId", upcomingTrips.get(position).getId());
                                intent.putExtra("repeat", upcomingTrips.get(position).getRepeat());
                                intent.putExtra("time", upcomingTrips.get(position).getTime());
                                context.startActivity(intent);
                                return true;
                            case R.id.cancel_item:
                                removeFromUpcoming(upcomingTrips.get(position));
                                addToHistory(upcomingTrips.get(position));
                                return true;
                            case R.id.showNotes_item:
                                Intent Noteintent = new Intent(context, AddNote.class);
                                Noteintent.putExtra("tripId", upcomingTrips.get(position).getId());
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
                Noteintent.putExtra("tripId", upcomingTrips.get(position).getId());
                context.startActivity(Noteintent);
            }
        });

    }

    public void getLoc(final Trip trip) {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            try {
                                addresses = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1);

                                Address address = addresses.get(0);
                                loc = address.getAddressLine(0);
                                trip.setStartPoint(loc);
                                addToHistory(trip);
                                removeFromUpcoming(trip);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {



        }
    };


    private void showDeleteAlert(final int pos) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you Want to delete " + upcomingTrips.get(pos).getTripName() + " ???");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTrip(upcomingTrips.get(pos));
                cancelAlarm(pos);
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertdialog.create();
        alertdialog.show();

    }


    private void startFloatingWidgetService(int position) {
        Intent intent = new Intent(context, FloatingWidgetService.class);
        intent.putExtra("tripId", upcomingTrips.get(position).getId());
        intent.putExtra("tripTitle", upcomingTrips.get(position).getTripName());
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

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {
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
