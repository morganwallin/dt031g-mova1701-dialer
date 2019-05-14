package se.miun.mova1701.dt031g.dialer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sweden1 = new LatLng(55.001099, 11.106694);
        LatLng sweden2 = new LatLng(69.063141, 24.16707);
        LatLngBounds sweden = new LatLngBounds(sweden1, sweden2);

        DatabaseHandler dbHandler = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> callsList = dbHandler.getCalls();
        for(HashMap<String, String> map : callsList) {
            MarkerOptions markerOptions = new MarkerOptions();
            boolean addMarker = false;
            for (Map.Entry<String, String> mapEntry : map.entrySet())
            {

                String key = mapEntry.getKey();
                String value = mapEntry.getValue();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker);
                markerOptions.icon(icon);
                if(key.equals("date")) {
                    markerOptions.snippet(value);
                }
                if(key.equals("phone_number")) {
                    markerOptions.title(value);
                }
                if(key.equals("position") && !value.equals("?, ?")) {
                    String[] latlong =  value.split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng pos = new LatLng(latitude, longitude);
                    markerOptions.position(pos);
                    addMarker = true;
                }
            }
            if(addMarker) {
                mMap.addMarker(markerOptions);
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });


        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sweden, 0));
    }
}
