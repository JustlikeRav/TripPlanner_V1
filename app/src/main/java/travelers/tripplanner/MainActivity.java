package travelers.tripplanner;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import travelers.tripplanner.fragments.Dashboard;
import travelers.tripplanner.fragments.GPStracker;
import travelers.tripplanner.fragments.Maps;
import travelers.tripplanner.fragments.Settings;
import travelers.tripplanner.fragments.history;
import travelers.tripplanner.fragments.MyLocation;
import travelers.tripplanner.register.signUp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static double latitude, longitude;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting current user
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if(user == null){
            finish();
            startActivity(new Intent(this, signUp.class));
        }

        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //GPS functionality
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        final GPStracker g = new GPStracker(getApplicationContext());
        Location l = g.getLocation();

        if(l!=null){
            latitude = l.getLatitude();
            longitude = l.getLongitude();
        }

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check the dashboard
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager fm = getFragmentManager();

        if (id == R.id.nav_dashboard) {
            fm.beginTransaction().replace(R.id.content_frame, new Dashboard()).commit();
        } else if (id == R.id.nav_maps) {
            fm.beginTransaction().replace(R.id.content_frame, new Maps()).commit();
        } else if (id == R.id.nav_history) {
            fm.beginTransaction().replace(R.id.content_frame, new history()).commit();
        } else if (id == R.id.nav_my_location) {
            fm.beginTransaction().replace(R.id.content_frame, new MyLocation()).commit();
        } else if (id == R.id.nav_settings) {
            fm.beginTransaction().replace(R.id.content_frame, new Settings()).commit();
        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(MainActivity.this, signUp.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
