package com.fl.dlc.util;

import android.os.AsyncTask;
import android.widget.TextView;

public class DLDBTask extends AsyncTask<Void, Void, String> {

    private TextView textView;
    private String waitText;

    public DLDBTask(TextView textView, String waitText) {

        this.textView = textView;
        this.waitText = waitText;
        //System.out.println("DB Task Created");
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        //System.out.println("Pre Execute start");
        textView.setText(waitText);
        //System.out.println("Pre Execute finish");
    }

    @Override
    protected String doInBackground(Void... params) {

        //System.out.println("Doing Background work");
        return DLUtil.calculateResult();
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        //System.out.println("Post execute start");

        if (result == null) {
            result = "";
        }

        textView.setText(result);
        //System.out.println("Post execute finish with result " + result);
    }
}
