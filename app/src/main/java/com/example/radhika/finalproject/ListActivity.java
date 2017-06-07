package com.example.radhika.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.radhika.finalproject.HomePage.map_num;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        ArrayList<String > arrayList = new ArrayList<String>();
        arrayList.add("Berlin");
        arrayList.add("Rome");
        arrayList.add("Amsterdam");
        arrayList.add("Paris");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String  itemValue    = (String) listView.getItemAtPosition(position);

                switch (position){
                    case 0:
                        map_num = 2;
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        map_num = 3;
                        Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        map_num = 4;
                        Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        map_num = 1;
                        Intent intent3 = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent3);
                        break;
                }

            }
        });

    }
}
