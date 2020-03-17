package com.example.mishwary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.mishwary.Models.FireBaseCreator;
import com.example.mishwary.ui.History.HistoryFragment;
import com.example.mishwary.ui.floatingwidget.FloatingWidgetService;
import com.example.mishwary.ui.home.HomeFragment;
import com.example.mishwary.ui.login.login;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import static com.example.mishwary.ui.home.HomeFragment.DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    String id, name, email;
    SharedPreferences pref; // 0 - for private mode
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Settings.canDrawOverlays(this)) {
            int REQUEST_CODE = 101;
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            myIntent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(myIntent, REQUEST_CODE);
        }
        FireBaseCreator.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");


        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("email", email);
        HomeFragment home = new HomeFragment();
        home.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, home).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFregment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        logout();
                        Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_home:
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("name", name);
                        bundle.putString("email", email);
                        selectedFregment = new HomeFragment();
                        selectedFregment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFregment).commit();
                        break;
                    case R.id.nav_history:
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("id", id);
                        selectedFregment = new HistoryFragment();
                        selectedFregment.setArguments(bundle2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFregment).commit();
                        break;
                }
                drawer.closeDrawers();
                return true;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_LONG).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startFloatingWidgetService() {
        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish();
    }

    private void logout() {
        //SharedPrefManger.getInstance(this).clear();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        login.mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


}
