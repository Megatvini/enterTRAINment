package ge.edu.freeuni.android.entertrainment.map;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import ge.edu.freeuni.android.entertrainment.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

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
        mMap.setOnMarkerClickListener(this);

        // Add a marker in Tbilisi, Georgia, and move the camera.
        LatLng tbilisi = new LatLng(41.7, 44.8);

        mMap.addMarker(new MarkerOptions().position(tbilisi).title("migel"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(tbilisi).zoom(9).build();
        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
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
