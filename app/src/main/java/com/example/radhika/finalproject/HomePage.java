package com.example.radhika.finalproject;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {
    static int map_num = 0;
    static int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //Button mapButton = (Button) findViewById(R.id.map_button);
        //Button locationButton = (Button) findViewById(R.id.location_button);

        ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);
        ImageButton locationButton = (ImageButton) findViewById(R.id.location_button);

        TextView mapText = (TextView) findViewById(R.id.mapText);
        TextView locationText = (TextView) findViewById(R.id.locationText);
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_num = 5;
                openMap(v);
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                openList(v);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_num = 5;
                openMap(v);

            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
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
