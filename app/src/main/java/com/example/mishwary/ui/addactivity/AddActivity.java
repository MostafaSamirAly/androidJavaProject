package com.example.mishwary.ui.addactivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, AddContract.AddView {
    Button btnDatePicker, btnTimePicker, btnAdd;
    TextView txtDate, txtTime;
    EditText titleTxt;
    AutoCompleteTextView startTxt, endTxt;
    final Calendar c = Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private AddPresenter addPresenter;
    private String userID,tripId;
    private Spinner repeatSpinner, descSpinner;
    private ArrayAdapter<CharSequence> repeatAdapter, descAdapter;
    private String startPoint = "At Start Location";
    private boolean currntIsChecked = false;
    CheckBox CurrentLoc;
    private static final int REQUEST_CODE_PERMISSION = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String Lon,lat,loc = " ";
    Geocoder geocoder;
    List<Address> addresses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        userID = intent.getStringExtra("id");
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btnAdd = (Button) findViewById(R.id.btn_add);
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        titleTxt = findViewById(R.id.trip_title);
        startTxt = findViewById(R.id.trip_start_point);
        endTxt = findViewById(R.id.trip_end_point);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        CurrentLoc = findViewById(R.id.currentLocation);
        CurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentLoc.isChecked())
                {
                    startTxt.setVisibility(View.GONE);
                   // GetLoc();

                    currntIsChecked = true;
                }
                else
                {
                    startTxt.setVisibility(View.VISIBLE);
                    currntIsChecked = false;
                }
            }
        });

        startTxt.setAdapter(new PlaceAutoSuggestAdapter(AddActivity.this, android.R.layout.simple_list_item_1));
        endTxt.setAdapter(new PlaceAutoSuggestAdapter(AddActivity.this, android.R.layout.simple_list_item_1));
        repeatSpinner = findViewById(R.id.spinner_repeat);
        descSpinner = findViewById(R.id.spinner_desc);
        btnAdd.setOnClickListener(this);
        repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeatSpinner, R.layout.support_simple_spinner_dropdown_item);
        repeatAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatAdapter);
        descAdapter = ArrayAdapter.createFromResource(this, R.array.descSpinner, R.layout.support_simple_spinner_dropdown_item);
        descAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        descSpinner.setAdapter(descAdapter);
        addPresenter = new AddPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GetLoc();
    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                           selectedDay = dayOfMonth;
                           selectedYear = year;
                           selectedMonth = monthOfYear;
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            selectedHour = hourOfDay;
                            selectedMinute = minute;
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == btnAdd) {
            if (validateInputs()) {
                addTriptoFireBase();
                btnAdd.setEnabled(false);
            }

        }
    }

    private boolean validateInputs() {
        boolean flag = true;
        if (titleTxt.getText().toString().trim().isEmpty()) {
            flag = false;
            titleTxt.setError("Enter Title");
            titleTxt.requestFocus();
        }

        if (endTxt.toString().trim().isEmpty()) {
            flag = false;
            //Toast.makeText(this,"Please Enter the Destination",Toast.LENGTH_LONG).show();
            endTxt.setError("Enter Destination");
            endTxt.requestFocus();
        }
        if (txtDate.getText().toString().trim().isEmpty()) {
            flag = false;
            Toast.makeText(this, "Please Select Date", Toast.LENGTH_LONG).show();
        }
        if (txtTime.getText().toString().trim().isEmpty()) {
            flag = false;
            Toast.makeText(this, "Please Select Time", Toast.LENGTH_LONG).show();
        }


        return flag;
    }

    private void addTriptoFireBase() {
        Trip trip = new Trip();
        trip.setUserId(userID);
        trip.setTripName(titleTxt.getText().toString());
        if(currntIsChecked){
            if(!loc.trim().isEmpty())
            {
                startPoint = loc;
            }
            trip.setStartPoint(startPoint);
        }else {
            trip.setStartPoint(startTxt.getText().toString());
        }
        trip.setDestination(endTxt.getText().toString());
        trip.setDate(txtDate.getText().toString());
        trip.setTime(txtTime.getText().toString());
        trip.setHourOfDay(selectedHour);
        trip.setMinutes(selectedMinute);
        trip.setDayOfMnoth(selectedDay);
        trip.setYear(selectedYear);
        trip.setMonths(selectedMonth+1);
        trip.setRepeat(repeatSpinner.getSelectedItem().toString());
        trip.setDescription(descSpinner.getSelectedItem().toString());
        addPresenter.addTrip(trip);
    }

    @Override
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Override
    public void gotoHome() {
        finish();
    }

}
