package ge.edu.freeuni.android.entertrainment.map;


import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Handler handler = new Handler();
    private Marker trainMarker;

    private Runnable runTrain = new Runnable() {
        @Override
        public void run() {
            String url = "http://entertrainment.herokuapp.com/webapi/map/location";

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    updateTrainLocation(response);
                }
            });
            handler.postDelayed(runTrain, 2000);
        }
    };

    private void updateTrainLocation(JSONObject response) {
        try {
            Double latitude = response.getDouble("latitude");
            Double longitude = response.getDouble("longitude");
            LatLng currLocation = new LatLng(latitude, longitude);

            if (trainMarker != null)
                trainMarker.remove();

            trainMarker = mMap.addMarker(new MarkerOptions().position(currLocation));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawMarkers();
        drawPolylines();
        mMap.setOnMarkerClickListener(this);

        LatLng tbilisi = new LatLng(41.7, 44.8);
        trainMarker = mMap.addMarker(new MarkerOptions()
                .position(tbilisi));
        handler.post(runTrain);

        // Move camera over Tbilisi, Georgia.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(tbilisi).zoom(8).build();
        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    private void drawPolylines() {
    }

    private String[] cities = {"Rustavi", "Tbilisi","Gori", "Khashuri", "Zestafoni", "Kutaisi", "Samtredia", "Poti", "Kobuleti", "Batumi"};
    
    private void drawMarkers() {
        LatLng prevCoord = null;
        
        for (int i = 0; i < cities.length; i++) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(cities[i] + ", Georgia", 1);
                if (addresses.size() > 0) {
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();
                    LatLng currCoord = new LatLng(latitude, longitude);

                    addMarker(currCoord, cities[i]);
                    addPolyline(currCoord, prevCoord);
                    prevCoord = currCoord;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPolyline(LatLng currCoord, LatLng prevCoord) {
        if (currCoord == null || prevCoord == null)
            return;

        PolylineOptions line = new PolylineOptions()
                .add(currCoord, prevCoord)
                .width(5)
                .color(Color.GRAY);
        mMap.addPolyline(line);
    }

    private void addMarker(LatLng coord, String city) {
        MarkerOptions marker = new MarkerOptions()
                .position(coord)
                .title(city)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent newIntent = new Intent(getActivity(), StationInfoActivity.class);
        Bundle newBundle = new Bundle();
        newIntent.putExtra("Bundle", newBundle);
        newBundle.putString("Station", marker.getTitle());
        startActivity(newIntent);
        return true;
    }
}
