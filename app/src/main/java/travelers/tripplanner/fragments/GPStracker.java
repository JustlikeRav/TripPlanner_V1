package travelers.tripplanner.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import travelers.tripplanner.fragments.MyLocation;

public class GPStracker {
    private static final String TAG = "GPStracker_Message";
    Context context;
    TextView LatText,LngText,Address;
    private RequestQueue requestQueue;
    double lat, lng;

    public GPStracker(Context c){
        context = c;
        requestQueue = Volley.newRequestQueue(c);
    }

    public Location getLocation(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Permission not granted",Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, new LocationListener(){

                @Override
                public void onLocationChanged(Location location) {
                    lat = round(location.getLatitude(),5);
                    lng = round(location.getLongitude(),5);
                    Address = MyLocation.Address;
                    LatText = MyLocation.LatText;
                    LngText = MyLocation.LngText;
                    if(!(Address == null||LatText == null||LngText == null)){
                        LatText.setText("Latitude: " + lat);
                        LngText.setText("Longitude: " + lng);
                        startReverseGeocoding();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        }else{
            Log.i( TAG , "Is GPS Enabled: False");
            Toast.makeText(context,"Please enable GPS",Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void startReverseGeocoding() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?" +
                        "latlng="+lat+","+lng,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response) {

                                String location = null;
                                try {
                                    location = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                    Address.setText(location);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(request);

            }
        });
    }

}