package com.example.project3pets;

import android.widget.Toast;

public class DownloadTask_TP extends DownloadTask {
    MainActivity myActivity;

    DownloadTask_TP(MainActivity activity){
        super();
        attach(activity);
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (myActivity != null) {
            myActivity.processJSon(result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    void detach() {
        myActivity = null;
    }
    void attach(MainActivity activity) {
        this.myActivity = activity;
    }

}
