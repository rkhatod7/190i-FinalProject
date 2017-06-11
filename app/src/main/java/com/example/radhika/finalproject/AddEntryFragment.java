package com.example.radhika.finalproject;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.radhika.finalproject.ImageAdapter.imageList;
import static com.example.radhika.finalproject.MapsActivity.mAdapter;


public class AddEntryFragment extends DialogFragment {

    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_PLACE_ID = "PLACE_ID";
    DatabaseReference placeDetailsTable;
    DatabaseReference imagesTable;
    DatabaseReference imagePostsTable;
    FirebaseDatabase database;
    FloatingActionButton fab;
    String place_id;
    String comment;
    String imageKey;
    int temp_count;
    float temp_rating;
    FirebaseStorage storage;
    String name;
    RatingBar ratingBar;
    Float rating;

    //CHANGES
    private static final int REQ_CODE_GET_IMAGE = 0;
    TextView photoText;
    Bitmap bitmap;
    TextView commentView;
    TextView textViewTitle;

    public AddEntryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddEntryFragment newInstance(String title, String place_id) {
        AddEntryFragment fragment = new AddEntryFragment();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_entry, container, false);

        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            name = currentProfile.getFirstName();
        }
        else {
            name = "Anonymous";
        }

        fab = (FloatingActionButton) view.findViewById(R.id.btnSubmit);
        textViewTitle = (TextView) view.findViewById(R.id.tvTitleAddEntry);
        commentView = (TextView) view.findViewById(R.id.editTextComment);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        comment = "";

        // for pin details
        database = FirebaseDatabase.getInstance();
        placeDetailsTable = database.getReference("PlaceDetails");
        imagesTable = database.getReference("Images");

        // for images of places
        storage = FirebaseStorage.getInstance();


        // Set title
        textViewTitle.setText(getArguments().getString(ARG_TITLE));
        place_id = getArguments().getString(ARG_PLACE_ID);
        Log.d("DREW", place_id);

        placeDetailsTable.child(place_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PlaceDetail value = dataSnapshot.getValue(PlaceDetail.class);
                if (value != null) {
                    try {
                        Log.d("DREW", "attempting to update comments...");
                        temp_count = Integer.parseInt(value.count);
                        temp_rating = Float.parseFloat(value.rating);
                        if (value.comment.length() != 0) {
                            comment = value.comment;
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DREW", "Failed to read value.", error.toException());
            }
        });

        // PlaceDetail(rating, count, comment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DREW", "Click " + Integer.toString(commentView.getText().length()));
                if (commentView.getText().length() > 0 && comment.length() > 0) {
                    comment += "\n\n";
                    comment += commentView.getText().toString() + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t - " + name;
                    comment += "\n______________________________________________________________________";
                }
                else if (commentView.getText().length() > 0 && comment.length() == 0) {
                    comment = "______________________________________________________________________\n\n" + commentView.getText().toString()+ "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t - " + name; ;
                    comment += "\n______________________________________________________________________";
                }

                Log.d("Drew", comment);
                if (ratingBar.getRating() != 0) {
                    Log.d("TAYLOR", "rating is bigger than 0");
                    rating = ratingBar.getRating();
                    Log.d("TAYLOR", Float.toString(rating));
                    Log.d("TAYLOR", "rating is " + Float.toString(temp_rating));
                    float total = temp_count * temp_rating;
                    Log.d("TAYLOr", Float.toString(total));
                    temp_count = temp_count + 1;
                    total = total + rating;
                    temp_rating = total / temp_count;
                    Log.d("TAYLOR", "new rating is " + Float.toString(temp_rating));
                }
                Log.d("TAYLOR", "current rating is " + Float.toString(temp_rating));
                PlaceDetail pd = new PlaceDetail(Float.toString(temp_rating), Integer.toString(temp_count), comment);
                placeDetailsTable.child(place_id).setValue(pd);
                Log.d("DREW", "hit this");

                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storage.getReference().child(place_id).child(imageKey).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d("DREW", "screenshot captured");
                            imagesTable.child(place_id).child(imageKey).setValue(downloadUrl.toString());
                        }
                    });
                }
                Toast.makeText(getContext(),getActivity().getString(R.string.entry),Toast.LENGTH_SHORT).show();
                closeFragment();
            }
        });

        //******CHANGES START HERE******
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
                        photoText.setPaintFlags(photoText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        photoText.setTextColor(Color.BLUE);

                        //Get the image bitmap
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        //imageList.add(bitmap);
                        //mAdapter.notifyDataSetChanged();
                        // necessary for uploading to firebase
                        imageKey = imageFilename;

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

    public void closeFragment(){
        getDialog().dismiss();
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
