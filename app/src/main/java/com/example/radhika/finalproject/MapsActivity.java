package com.example.radhika.finalproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.radhika.finalproject.HomePage.map_num;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    FloatingActionButton backButton;
    private GoogleMap mMap;
    private List<Place> Places;
    View view;
    LatLng pos;

    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        backButton = (FloatingActionButton) findViewById(R.id.floatingActionButton4);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        //2
        LatLng berlin = new LatLng(52.5200, 13.405);
        //3
        LatLng rome = new LatLng(41.9028, 12.4964);
        //4
        LatLng amsterdam = new LatLng(52.3702,4.8952 );
        //1
        LatLng paris = new LatLng(48.8686,2.3002);

        if (map_num == 1){
            pos = paris;
        }
        if (map_num == 2){
            pos = berlin;
        }
        if (map_num == 3){
            pos = rome;
        }
        if (map_num == 4){
            pos = amsterdam;
        }

        mMap.addMarker(new MarkerOptions().position(pos).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));

        Places = new ArrayList<>();
        PlacesRetriever.GetPlaces(new PlacesRetriever.OnPlaceListRetrievedListener() {
            @Override
            public void OnPlaceListRetrieved(List<Place> places) {
                Places = places;
                for (int i = 0; i < Places.size(); i++) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Places.get(i).Lat, Places.get(i).lng)).title(Places.get(i).name));

                    marker.setTag(Places.get(i).place_id);


                }
            }
        });

        ClubRetriever.GetPlaces(new ClubRetriever.OnPlaceListRetrievedListener(){
            @Override
            public void OnPlaceListRetrieved(List<Place> places) {
                Places = places;
                for (int i = 0; i < Places.size(); i++) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Places.get(i).Lat, Places.get(i).lng)).title(Places.get(i).name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                    marker.setTag(Places.get(i).place_id);


                }
            }

        });
        AttractionRetriever.GetPlaces(new AttractionRetriever.OnPlaceListRetrievedListener(){
            @Override
            public void OnPlaceListRetrieved(List<Place> places) {
                Places = places;
                for (int i = 0; i < Places.size(); i++) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Places.get(i).Lat, Places.get(i).lng)).title(Places.get(i).name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    marker.setTag(Places.get(i).place_id);


                }
            }

        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                fragment.show(getSupportFragmentManager(), "Attraction Detail");
               View view = findViewById(R.id.map);
                view.setVisibility(View.INVISIBLE);
                findViewById(R.id.legend).setVisibility(View.INVISIBLE);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_maps, PlaceReview.newInstance()).commit();
            }
        });


   }

   public void showPopup()
   {
       // TODO ADD CONFIRMATION DIALOG
       AddEntryFragment.newInstance().show(getSupportFragmentManager(),"Add entry");
   }

}
