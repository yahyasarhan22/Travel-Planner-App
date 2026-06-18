package com.example.a1221858_1220137_courseproject;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TripJsonParser {

    private static final String TAG = "TripJsonParser";

    public static List<Trip> getTripsFromJson(String jsonString) {
        if (jsonString == null) return null;

        int startBrace = jsonString.indexOf('{');
        int startBracket = jsonString.indexOf('[');

        if (startBrace == -1 && startBracket == -1) {
            Log.e(TAG, "No JSON object or array found in response");
            return null;
        }

        boolean isArray = (startBracket != -1 && (startBrace == -1 || startBracket < startBrace));
        String cleanJson = jsonString.substring(isArray ? startBracket : startBrace).trim();

        List<Trip> tripsList = new ArrayList<>();
        try {
            if (isArray) {
                JSONArray jsonArray = new JSONArray(cleanJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    tripsList.add(parseTrip(jsonArray.getJSONObject(i)));
                }
            } else {
                JSONObject jsonObject = new JSONObject(cleanJson);
                tripsList.add(parseTrip(jsonObject));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Parsing error: " + e.getMessage());
            return null;
        }
        return tripsList;
    }

    private static Trip parseTrip(JSONObject jsonObject) {
        Trip trip = new Trip();
        trip.setId(jsonObject.optInt("id"));
        trip.setDestination(jsonObject.optString("destination"));
        trip.setCountry(jsonObject.optString("country"));
        
        if (jsonObject.has("durationDays")) {
            trip.setDurationDays(jsonObject.optInt("durationDays"));
        } else { // fallback
            trip.setDurationF(jsonObject.optString("duration"));
        }
        
        trip.setPrice(jsonObject.optDouble("price", 0.0));
        trip.setDescription(jsonObject.optString("description"));
        trip.setRating(jsonObject.optDouble("rating", 0.0));
        trip.setImageUrl(jsonObject.optString("imageUrl"));

        return trip;
    }
}