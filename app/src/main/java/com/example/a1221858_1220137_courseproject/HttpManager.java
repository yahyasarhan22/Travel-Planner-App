package com.example.a1221858_1220137_courseproject;


import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String getData(String targetUrl) {
        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(targetUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();

        } catch (Exception ex) {
            Log.d("HttpURLConnection", "Error fetching data: " + ex.toString());
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
