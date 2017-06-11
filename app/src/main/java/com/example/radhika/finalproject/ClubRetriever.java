package com.example.radhika.finalproject;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.radhika.finalproject.HomePage.map_num;

/**
 * Created by Radhika on 6/7/17.
 */

public class ClubRetriever {

    static URLConnection connection;
    public static void GetPlaces(OnPlaceListRetrievedListener listener) {
        Log.d("HERE", "IN GET PLACES");
        new GetPlacetask(listener).execute();
    }

    public interface OnPlaceListRetrievedListener {
        void OnPlaceListRetrieved(List<Place> places);
    }

    public static class GetPlacetask extends AsyncTask<Void, Void, List<Place>> {

        private ClubRetriever.OnPlaceListRetrievedListener Listener;

        public GetPlacetask(ClubRetriever.OnPlaceListRetrievedListener listener) {
            Log.d("HERE", "IN GET PLACES TASK");

            Listener = listener;
        }

        @Override
        protected List<Place> doInBackground(Void... params) {
            try {
                // URLConnection connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.4140,-119.8489&radius=500&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();
              //  URLConnection connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.8686,2.3002&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();

                if (map_num == 1) {
                    //paris
                    connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.8686,2.3002&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();

                }
                if (map_num == 2) {
                    //berlin
                    connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.5200,13.4050&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();
                }
                if (map_num == 3){
                    //rome
                    connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=41.9028,12.4964&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();

                }
                if (map_num == 4)
                {
                    //ams
                    connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.3702,4.8952&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();
                    // URLConnection connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.4140,-119.8489&radius=500&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();
                }

                if (map_num == 5)
                {
                    connection = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +MapsActivity.lat +"," +MapsActivity.lng+ "&radius=800&types=night_club&key=AIzaSyALd_KbyBkJxy4LrayDU3_nPTSYwZDynIY").openConnection();

                }

                Log.d("HERE", "JUST OPENED URL CONNECTION" + connection);
                JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                List<Place> places = new ArrayList<>();
                reader.beginObject();
                while (!reader.nextName().equals("results")) {
                    reader.skipValue();
                }
                reader.beginArray();
                while (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                    places.add(ParsePlace(reader));

                    reader.endObject();
                }
                reader.close();
                return places;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }

        private Place ParsePlace(JsonReader reader) throws IOException {
            Place place = new Place("", "", 0.0, 0.0);
            while (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                switch (name) {
                    case "geometry":
                        Log.d("RADHIKA", "ParsePlace: In Geometry");
                        reader.beginObject();
                        break;
                    case "location":
                        Log.d("RADHIKA", "ParsePlace: In Location");
                        reader.beginObject();
                        break;
                    case "lat":
                        Log.d("RADHIKA", "ParsePlace: In Lat");
                        place.Lat = reader.nextDouble();
                        Log.d("RADHIKA", "ParsePlace: " + place.Lat);

                        break;
                    case "lng":
                        Log.d("RADHIKA", "ParsePlace: In Lng");
                        place.lng = reader.nextDouble();
                        while (reader.hasNext()) {
                            reader.skipValue();
                        }
                        reader.endObject();
                        while (reader.hasNext()) {
                            reader.skipValue();
                        }
                        reader.endObject();
                        break;
                    case "name":
                        place.name = reader.nextString();
                        Log.d("RADHIKA", "ParsePlace: " + place.name);
                        break;
                    case "place_id":
                        place.place_id = reader.nextString();
                        break;

                    default:
                        reader.skipValue();
                        break;

                }
            }
            return place;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            Listener.OnPlaceListRetrieved(places);
        }

    }
}
