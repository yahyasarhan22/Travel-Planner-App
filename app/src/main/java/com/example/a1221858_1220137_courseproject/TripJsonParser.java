package com.example.a1221858_1220137_courseproject;

import com.example.a1221858_1220137_courseproject.Trip;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TripJsonParser {

    public static List<Trip> getTripsFromJson(String jsonString) {
        List<Trip> tripsList = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            tripsList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Trip trip = new Trip();

                trip.setId(jsonObject.getInt("id"));
                trip.setDestination(jsonObject.getString("destination"));
                trip.setDurationF(jsonObject.getString("duration"));
                trip.setPrice(jsonObject.getDouble("price"));
                trip.setDescription(jsonObject.getString("description"));

                tripsList.add(trip);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tripsList;
    }
}