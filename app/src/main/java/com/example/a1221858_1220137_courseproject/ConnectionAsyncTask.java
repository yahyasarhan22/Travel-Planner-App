package com.example.a1221858_1220137_courseproject;

import android.app.Activity;
import android.os.AsyncTask;
import com.example.a1221858_1220137_courseproject.IntroductionActivity;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

    private final Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity instanceof IntroductionActivity) {
            ((IntroductionActivity) activity).setButtonText("Connecting...");
            ((IntroductionActivity) activity).setProgressVisibility(true);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (activity instanceof IntroductionActivity) {
            IntroductionActivity introActivity = (IntroductionActivity) activity;
            introActivity.setProgressVisibility(false);

            if (result != null) {
                introActivity.setButtonText("Connected");
                introActivity.onDataFetchSuccess(result);
            } else {
                introActivity.setButtonText("Connect");
                introActivity.onDataFetchFailure();
            }
        }
    }
}