package com.example.a1221858_1220137_courseproject;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    private static final String TAG = "HttpManager";

    public static String getData(String targetUrl) {
        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(targetUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append('\n');
                }
                return stringBuilder.toString();
            } else {
                Log.e(TAG, "Server returned non-OK status: " + responseCode);
                return null;
            }

        } catch (Exception ex) {
            Log.e(TAG, "Error fetching data: " + ex.toString());
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}