package travelers.tripplanner.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import travelers.tripplanner.MainActivity;
import travelers.tripplanner.R;

public class MyLocation extends Fragment {

    public static TextView LatText, LngText, Address;
    double lat,lng;
    private RequestQueue requestQueue;

    public MyLocation() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_location, container, false);

        LatText = (TextView) view.findViewById(R.id.Lat);
        LngText = (TextView) view.findViewById(R.id.Lng);
        Address = (TextView) view.findViewById(R.id.Address);
        requestQueue = Volley.newRequestQueue(getContext());

        lat = MainActivity.latitude;
        lng = MainActivity.longitude;

        LatText.setText("Latitude: "+round(lat,5));
        LngText.setText("Longitude: "+round(lng,5));

        startReverseGeocoding();

        return view;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static TextView getAddress() {
        return Address;
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
