package com.example.radhika.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// TODO REFACTOR NAME BECAUSE THIS ACTUALLY ISNT A POPUP ANYMORE
public class PlaceReview extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button back_button;
    EditText tv;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MapsActivity mActivity;
    Context mContext;
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
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_review, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        back_button = (Button) view.findViewById(R.id.button3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.showPopup();
            }
        });

        tv = (EditText) view.findViewById(R.id.tvComments);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference placeDetailsTable = database.getReference("PlaceDetails");
        DatabaseReference imagesTable = database.getReference("Images");
        DatabaseReference imagePostsTable = database.getReference("ImagePosts");

        // PlaceDetail(rating, count, comment);
        placeDetailsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    PlaceDetail value = singleSnapshot.getValue(PlaceDetail.class);
                    Log.d("DREW", value.rating);
                    Log.d("DREW", value.count);
                    Log.d("DREW", value.comment);
                    tv.setText(value.comment);
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
