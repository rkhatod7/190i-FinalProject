package com.example.radhika.finalproject;

import android.*;
import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import static com.example.radhika.finalproject.HomePage.flag;
import static com.example.radhika.finalproject.HomePage.map_num;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    FloatingActionButton backButton;
    private GoogleMap mMap;
    private List<Place> Places;
    View view;
    LatLng pos;
    TextView textViewComments;
    public static LatLng latLng;
    public static double lat;
    public static double lng;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    public static ImageAdapter mAdapter;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        backButton = (FloatingActionButton) findViewById(R.id.fabBackMaps);
        textViewComments = (TextView) findViewById(R.id.tvComments);


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
        if(flag == 0) {
            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }


        if (flag == 1) {

            // Add a marker in Sydney and move the camera
            //2
            LatLng berlin = new LatLng(52.5200, 13.405);
            //3
            LatLng rome = new LatLng(41.9028, 12.4964);
            //4
            LatLng amsterdam = new LatLng(52.3702, 4.8952);
            //1
            LatLng paris = new LatLng(48.8686, 2.3002);

            if (map_num == 1) {
                pos = paris;
            }
            if (map_num == 2) {
                pos = berlin;
            }
            if (map_num == 3) {
                pos = rome;
            }
            if (map_num == 4) {
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

            ClubRetriever.GetPlaces(new ClubRetriever.OnPlaceListRetrievedListener() {
                @Override
                public void OnPlaceListRetrieved(List<Place> places) {
                    Places = places;
                    for (int i = 0; i < Places.size(); i++) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Places.get(i).Lat, Places.get(i).lng)).title(Places.get(i).name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                        marker.setTag(Places.get(i).place_id);


                    }
                }

            });
            AttractionRetriever.GetPlaces(new AttractionRetriever.OnPlaceListRetrievedListener() {
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
                    mAdapter = new ImageAdapter(getApplicationContext());
                    String place_id = marker.getId();
                    View view = findViewById(R.id.map);
                    view.setVisibility(View.INVISIBLE);
                    findViewById(R.id.legend).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fabBackMaps).setVisibility(View.INVISIBLE);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.activity_maps, PlaceReview.newInstance(marker.getTitle(), place_id));
                    fragmentTransaction.commit();
                }
            });
        }





   }

    //location changed code referenced from https://www.androidtutorialpoint.com/intermediate/android-map-app-showing-current-location-android/
    @Override
    public void onLocationChanged(final Location location){
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if(flag == 0) {
            //Place current location marker
            lat = location.getLatitude();
            lng = location.getLongitude();
            //latLng = new LatLng(location.getLatitude(), location.getLongitude());

            pos = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(pos);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //mMap.addMarker(new MarkerOptions().position(pos).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));
            Log.d("TAYLOR", "position is" + pos);
        /*
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 20));
        */


            //stop location updates
            if (mGoogleApiClient != null) {

                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

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

            ClubRetriever.GetPlaces(new ClubRetriever.OnPlaceListRetrievedListener() {
                @Override
                public void OnPlaceListRetrieved(List<Place> places) {
                    Places = places;
                    for (int i = 0; i < Places.size(); i++) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Places.get(i).Lat, Places.get(i).lng)).title(Places.get(i).name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                        marker.setTag(Places.get(i).place_id);


                    }
                }

            });
            AttractionRetriever.GetPlaces(new AttractionRetriever.OnPlaceListRetrievedListener() {
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
                    mAdapter = new ImageAdapter(getApplicationContext());
                    String place_id = marker.getId();
                    View view = findViewById(R.id.map);
                    view.setVisibility(View.INVISIBLE);
                    findViewById(R.id.legend).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fabBackMaps).setVisibility(View.INVISIBLE);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.activity_maps, PlaceReview.newInstance(marker.getTitle(), place_id));
                    fragmentTransaction.commit();
                }
            });
        }



    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }




    public void showPopup(String title, String place_id)
   {
       // TODO ADD CONFIRMATION DIALOG
       AddEntryFragment.newInstance(title, place_id).show(getSupportFragmentManager(),"Add entry");
       findViewById(R.id.fabBackMaps).setVisibility(View.INVISIBLE);
   }

}
