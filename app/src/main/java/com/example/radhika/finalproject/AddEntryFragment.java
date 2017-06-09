package com.example.radhika.finalproject;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class AddEntryFragment extends DialogFragment {

    DatabaseReference placeDetailsTable;
    DatabaseReference imagesTable;
    DatabaseReference imagePostsTable;
    FirebaseDatabase database;
    FloatingActionButton fab;

    //CHANGES
    private static final int REQ_CODE_GET_IMAGE = 0;
    TextView photoText;
    Bitmap bitmap;
    TextView commentView;

    public AddEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEntryFragment newInstance( ) {
        AddEntryFragment fragment = new AddEntryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_entry, container, false);

        Profile currentProfile = Profile.getCurrentProfile();
        final String name = currentProfile.getFirstName();

        fab = (FloatingActionButton) view.findViewById(R.id.btnSubmit);

        database = FirebaseDatabase.getInstance();
        placeDetailsTable = database.getReference("PlaceDetails");
        DatabaseReference imagesTable = database.getReference("Images");
        DatabaseReference imagePostsTable = database.getReference("ImagePosts");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = name + ": " + commentView.getText().toString();
                PlaceDetail pd = new PlaceDetail("4.2", "5", comment);
                placeDetailsTable.child("3").setValue(pd);
            }
        });

        //******CHANGES START HERE******
        commentView = (TextView) view.findViewById(R.id.textView4);
        photoText = (TextView) view.findViewById(R.id.textView5);
        FloatingActionButton camera = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE_GET_IMAGE);
                }
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    launchGallery();

                }

            }
        });


        return view;
    }

    //CHANGES****
    public void launchGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFilename));
        startActivityForResult(intent, REQ_CODE_GET_IMAGE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case REQ_CODE_GET_IMAGE:
                    try {


                    Uri uri = data.getData();
                    String[] filepath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(uri, filepath, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filepath[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    //File file = new File(picturePath);
                    File file = new File("" + uri);
                    String imageFilename = file.getName();
                    photoText.setText(imageFilename);
                    photoText.setTextColor(Color.BLUE);

                    //Get the image bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    break;

                default:
                    Log.d("Taylor","Request code is: " + requestCode);


            }
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
