package com.fl.dlc.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fl.dlc.R;
import com.fl.dlc.fragment.AboutUsFragment;
import com.fl.dlc.fragment.FinalResultFragment;
import com.fl.dlc.fragment.First_InningsDetailsFragment;
import com.fl.dlc.fragment.Second_InningsDetailsFragment;
import com.fl.dlc.fragment.TypeAndFormatFragment;
import com.fl.dlc.util.DLConstants;
import com.fl.dlc.util.DLDBConstants;
import com.fl.dlc.util.DLDBHelper;
import com.fl.dlc.util.DLModel;
import com.fl.dlc.util.DLPagerAdapter;
import com.fl.dlc.util.DLUtil;
import com.fl.dlc.util.Suspension;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class DLMainActivity extends ActionBarActivity
        implements TypeAndFormatFragment.OnFragmentInteractionListener,
        FinalResultFragment.OnFragmentInteractionListener,
        First_InningsDetailsFragment.OnFragmentInteractionListener,
        Second_InningsDetailsFragment.OnFragmentInteractionListener,
        AboutUsFragment.OnFragmentInteractionListener {

    ViewPager viewPager;
    DLPagerAdapter adapter;
    DLDBHelper dbhelper;
    ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlmain);

        Intent intent = getIntent();
        boolean clearSuspension = intent.getBooleanExtra(DLConstants.CLEAR_SUSPENSIONS, false);

        if (clearSuspension) {
            DLModel.setT1Suspensions(null);
            DLModel.setT1Suspensions(null);
        }

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //final AdRequest adRequest = new AdRequest.Builder().addTestDevice("7CCFF84356925191B618A8CFD8B71F44").build();
                final AdRequest adRequest = new AdRequest.Builder().build();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adView.loadAd(adRequest);
                    }
                });
            }
        }).start();*/

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new DLPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dbhelper = new DLDBHelper(this);
        DLDBConstants.dbhelper = dbhelper;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dlmain, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(getDefaultShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    private Intent getDefaultShareIntent() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String title = "Duckworth Lewis (D/L) Calculator";
        String body = "Awesome app for cricket lovers! " +
                "Check out D/L Calculator on Android " +
                "http://play.google.com/store/app/details?id=" + getPackageName() +
                " via @dl_calc";
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {

            return true;
        }

        if (id == R.id.menu_item_rate) {

            startRateActivity();
        }

        if (id == R.id.menu_item_help) {

            viewPager.setCurrentItem(DLConstants.ABOUT_US_FRAGMENT, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(Uri uri) {
        Toast toast = Toast.makeText(this, "Wheeee!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void moveToFirst_InningsDetails(View view) {

        viewPager.setCurrentItem(DLConstants.TEAM1_DETAILS_FRAGMENT, true);
    }

    public void moveToSecond_InningsDetails(View view) {

        viewPager.setCurrentItem(DLConstants.TEAM2_DETAILS_FRAGMENT, true);
    }

    public void moveToFinalResult(View view) {

        viewPager.setCurrentItem(DLConstants.FINAL_RESULT_FRAGMENT, true);
    }

    public void calculateFinalResult(View view) {

        Spinner format_spinner = (Spinner) findViewById(R.id.format_spinner);
        int format = format_spinner.getSelectedItemPosition();
        DLModel.setFormat(format);

        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);
        int type = type_spinner.getSelectedItemPosition();

        int g = DLUtil.getG(format, type);
        DLModel.setG(g);

        EditText first_innings_overs = (EditText) findViewById(R.id.first_innings_overs_text);
        Double t1overs = DLUtil.getValidOvers(first_innings_overs.getText().toString(), DLConstants.TEAM_1);

        if (t1overs == null) {
            DLUtil.showAlertDialog(this, "Invalid Overs for First Innings", "Please enter a valid amount of overs for First Innings");
            return;
        }

        DLModel.setT1StartOvers(t1overs);

        EditText first_innings_score = (EditText) findViewById(R.id.first_innings_final_score_text);
        Integer t1score = DLUtil.getValidScore(first_innings_score.getText().toString());

        if (t1score == null) {
            DLUtil.showAlertDialog(this, "Invalid Score for First Innings", "Please enter a valid final score for First Innings");
            return;
        }

        DLModel.setT1FinalScore(t1score);

        EditText second_innings_overs = (EditText) findViewById(R.id.second_innings_overs_text);
        Double t2overs = DLUtil.getValidOvers(second_innings_overs.getText().toString(), DLConstants.TEAM_2);

        if (t2overs == null) {
            DLUtil.showAlertDialog(this, "Invalid Overs for Second Innings", "Please enter a valid amount of overs for Second Innings");
            return;
        }

        if (t2overs > t1overs) {
            DLUtil.showAlertDialog(this, "Invalid Overs for Second Innings", "Number of overs at the start for Second Innings cannot be greater than those for First Innings");
            return;
        }

        DLModel.setT2StartOvers(t2overs);

        EditText second_innings_score = (EditText) findViewById(R.id.second_innings_final_score_text);
        String t2_score = second_innings_score.getText().toString().trim();
        Integer t2score = DLUtil.getValidScore(t2_score);

        if (t2score == null && t2_score != null && !t2_score.equals("")) {
            DLUtil.showAlertDialog(this, "Invalid Score for Second Innings", "Please enter a valid score for Second Innings");
            return;
        }

        DLModel.setT2FinalScore(t2score);

        List<Suspension> t1_suspensions = DLModel.getT1Suspensions();
        //System.out.println("t1_suspensions == null ? " + t1_suspensions);
        if (t1_suspensions == null || t1_suspensions.size() == 0) {
            DLModel.setT1Suspensions(new ArrayList<Suspension>());
        } else {
            t1_suspensions = DLUtil.getValidAndNonOverlappingSuspensions(t1_suspensions, DLConstants.TEAM_1);

            if (t1_suspensions == null) {
                DLUtil.showAlertDialog(this, "Invalid Suspensions for First Innings", "Please check your suspension list for First Innings");
                return;
            }
            DLModel.setT1Suspensions(t1_suspensions);
        }

        String t2_overs = second_innings_overs.getText().toString().trim();

        if (t2_overs == null || t2_overs.equals("")) {
            Double overs_lost = DLUtil.getTotalOversLost(t1_suspensions);
            DLModel.setT2StartOvers(t2overs - overs_lost);
        }


        List<Suspension> t2_suspensions = DLModel.getT2Suspensions();

        if (t2_suspensions == null || t2_suspensions.size() == 0) {
            DLModel.setT2Suspensions(new ArrayList<Suspension>());
        } else {
            t2_suspensions = DLUtil.getValidAndNonOverlappingSuspensions(t2_suspensions, DLConstants.TEAM_2);

            if (t2_suspensions == null) {
                DLUtil.showAlertDialog(this, "Invalid Suspensions for Second Innings", "Please check your suspension list for Second Innings");
                return;
            }

            DLModel.setT2Suspensions(t2_suspensions);
        }

        DLDBTask task = new DLDBTask();
        task.execute();

        /*String result = DLUtil.calculateResult();
        System.out.println(result);

        if (result != null) {
            TextView result_status = (TextView) findViewById(R.id.final_result_status);
            result_status.setText(result);
        }*/
    }

    public void addOrEditFirst_InningsSuspensions(View view) {

        addOrEditSuspensions(view, DLConstants.TEAM_1);
    }


    public void addOrEditSecond_InningsSuspensions(View view) {

        addOrEditSuspensions(view, DLConstants.TEAM_2);
    }

    private void addOrEditSuspensions(View view, int team) {

        Intent intent = new Intent(this, SuspensionsActivity.class);
        intent.putExtra(DLConstants.TEAM_ID, team);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }

    public void setWaitText() {

        TextView textView = (TextView) findViewById(R.id.final_result_status);
        String waitText = getString(R.string.final_result_status_wait);
        textView.setText(waitText);
    }

    public void setStatusText(String result) {

        TextView textView = (TextView) findViewById(R.id.final_result_status);
        textView.setText(result);
    }

    public void startRateActivity() {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/app/details?id=" + getPackageName())));
        }
    }

    public void shareApp(View view) {

        startActivity(Intent.createChooser(getDefaultShareIntent(), "Share via"));
    }

    public void rateApp(View view) {

        startRateActivity();
    }

    private class DLDBTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //System.out.println("Pre Execute start");
            setWaitText();
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

            setStatusText(result);
            //System.out.println("Post execute finish with result " + result);
        }
    }
}
