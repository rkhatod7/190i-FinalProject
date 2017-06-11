package com.example.radhika.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static com.example.radhika.finalproject.ImageAdapter.imageList;
import static com.example.radhika.finalproject.MapsActivity.mAdapter;

// TODO REFACTOR NAME BECAUSE THIS ACTUALLY ISNT A POPUP ANYMORE
public class PlaceReview extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_PLACE_ID = "place_id";
    FloatingActionButton back_button;
    EditText tv;
    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mPlace_id;
    MapsActivity mActivity;
    TextView textViewTitle;
    String place_id;
    RatingBar ratingBar;
    FirebaseStorage storage;
    Bitmap bitmap;
    GridView listView;

    public PlaceReview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceReview.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceReview newInstance(String title, String place_id) {
        PlaceReview fragment = new PlaceReview();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PLACE_ID, place_id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mPlace_id = getArguments().getString(ARG_PLACE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_review, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        back_button = (FloatingActionButton) view.findViewById(R.id.fabBackPlaceReview);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitlePlaceReview);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        listView = (GridView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        // Set title
        textViewTitle.setText(getArguments().getString(ARG_TITLE));
        imageList = new ArrayList<Bitmap>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setMessage("Submit a review for " + getArguments().getString(ARG_TITLE) + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int id){
                                mActivity.showPopup(mTitle, mPlace_id);
                            }
                        }).setNegativeButton("No",null)
                        .show();

            }
        });

        tv = (EditText) view.findViewById(R.id.tvComments);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference placeDetailsTable = database.getReference("PlaceDetails");
        DatabaseReference imagesTable = database.getReference("Images");
        storage = FirebaseStorage.getInstance();
        place_id = getArguments().getString(ARG_PLACE_ID);

        imagesTable.child(place_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DREW", "Updating images");
                Log.d("DREW", place_id);
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String value = singleSnapshot.getValue(String.class);
                    Log.d("DREW", value);
                    try {
                        URL url = new URL(value);
                        bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
                        imageList.add(bitmap);
                        Log.d("DREW", "Added bitmap");
                    }
                    catch (Exception e) {
                        Log.d("DREW", "failed to convert to bitmap");
                        e.printStackTrace();
                    }
                }
                Log.d("DREW", "succesful");
                Log.d("DREW", Integer.toString(imageList.size()));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DREW", "Failed to read value.", error.toException());
            }
        });

        // PlaceDetail(rating, count, comment);
        placeDetailsTable.child(place_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PlaceDetail value = dataSnapshot.getValue(PlaceDetail.class);
                if (value != null) {
                    Log.d("DREW", value.rating);
                    Log.d("DREW", value.count);
                    Log.d("DREW", value.comment);

                    if (value.rating != null) {
                        ratingBar.setRating(Float.parseFloat(value.rating));
                    }

                    if (value.comment != null) {
                        tv.setText(value.comment);
                        Log.d("DREW", "updated comment section...");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DREW", "Failed to read value.", error.toException());
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity =  (MapsActivity) context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}