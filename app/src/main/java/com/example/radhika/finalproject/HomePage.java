package com.example.radhika.finalproject;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    static int map_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button mapButton = (Button) findViewById(R.id.map_button);
        Button locationButton = (Button) findViewById(R.id.location_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_num = 1;
                openMap(v);

            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openList(v);
            }
        });

    }

    public void openMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void openList(View view){
        //Intent intent = new Intent(this, ListActivity.class);
        startActivity(new Intent(this, com.example.radhika.finalproject.ListActivity.class));
    }
}
