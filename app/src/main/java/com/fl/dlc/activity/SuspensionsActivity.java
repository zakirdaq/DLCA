package com.fl.dlc.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fl.dlc.R;
import com.fl.dlc.fragment.AddEditSuspensionFragment;
import com.fl.dlc.fragment.ListSuspensionsFragment;
import com.fl.dlc.util.DLConstants;
import com.fl.dlc.util.DLModel;
import com.fl.dlc.util.DLUtil;
import com.fl.dlc.util.Suspension;

import java.util.ArrayList;
import java.util.List;

public class SuspensionsActivity extends ActionBarActivity
        implements ListSuspensionsFragment.OnSuspensionClickedListener,
        AddEditSuspensionFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListSuspensionsFragment listSuspensionsFragment;
    private Suspension currentSuspension;
    private int currentSuspensionPosition;
    private int team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspensions);

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Intent intent = getIntent();
            team = intent.getIntExtra(DLConstants.TEAM_ID, DLConstants.TEAM_1);
            listSuspensionsFragment = ListSuspensionsFragment.newInstance(team);
            fragmentTransaction.add(R.id.suspensions_container, listSuspensionsFragment);
            fragmentTransaction.commit();
        }

       /* fragmentManager.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction
                    }
                }
        );*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_suspensions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showAddSuspensionFragment(View view) {

        showAddEditSuspensionFragment(null);
    }

    public void showAddEditSuspensionFragment(Suspension suspension) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.suspensions_container, AddEditSuspensionFragment.newInstance(suspension));
        fragmentTransaction.addToBackStack("listSuspensions");
        fragmentTransaction.commit();
    }

    public void addSuspension(View view) {

        currentSuspension = generateValidSuspension(view);
        List<Suspension> suspensions;

        if (currentSuspension != null) {

            if (team == DLConstants.TEAM_1) {
                suspensions = DLModel.getT1Suspensions();
            } else {
                suspensions = DLModel.getT2Suspensions();
            }

            if (suspensions == null) {
                suspensions = new ArrayList<Suspension>();
            }

            suspensions.add(currentSuspension);

            if (team == DLConstants.TEAM_1) {
                DLModel.setT1Suspensions(suspensions);
            } else {
                DLModel.setT2Suspensions(suspensions);
            }

            moveToListSuspensions();
        }
    }

    public void resetFields(View view) {

        LinearLayout layout = (LinearLayout) view.getParent().getParent();

        EditText score_text = (EditText) layout.findViewById(R.id.suspension_score_text);
        EditText wickets_text = (EditText) layout.findViewById(R.id.suspension_wickets_text);
        EditText overs_before_text = (EditText) layout.findViewById(R.id.suspension_overs_before_text);
        EditText overs_after_text = (EditText) layout.findViewById(R.id.suspension_overs_after_text);

        score_text.setText("");
        wickets_text.setText("");
        overs_before_text.setText("");
        overs_after_text.setText("");

    }

    public void saveEdits(View view) {

        currentSuspension = generateValidSuspension(view);

        List<Suspension> suspensions;

        if (currentSuspension != null) {

            if (team == DLConstants.TEAM_1) {
                suspensions = DLModel.getT1Suspensions();
            } else {
                suspensions = DLModel.getT2Suspensions();
            }

            /*if (suspensions == null) {
                suspensions = new ArrayList<Suspension>();
            }*/

            suspensions.set(currentSuspensionPosition, currentSuspension);

            if (team == DLConstants.TEAM_1) {
                DLModel.setT1Suspensions(suspensions);
            } else {
                DLModel.setT2Suspensions(suspensions);
            }

            moveToListSuspensions();
        }
    }

    public void deleteSuspension(View view) {

        deleteSuspension();
        moveToListSuspensions();
    }

    public Suspension generateValidSuspension(View view) {

        Suspension s = new Suspension();

        LinearLayout layout = (LinearLayout) view.getParent().getParent();

        EditText score_text = (EditText) layout.findViewById(R.id.suspension_score_text);
        EditText wickets_text = (EditText) layout.findViewById(R.id.suspension_wickets_text);
        EditText overs_before_text = (EditText) layout.findViewById(R.id.suspension_overs_before_text);
        EditText overs_after_text = (EditText) layout.findViewById(R.id.suspension_overs_after_text);

        Integer score = DLUtil.getValidScore(score_text.getText().toString());

        if (score == null) {
            DLUtil.showAlertDialog(this, "Invalid Score Value", "Please enter a valid value for score");
            return null;
        }

        s.setScore(score);

        Integer wickets = DLUtil.getValidWickets(wickets_text.getText().toString());

        if (wickets == null) {
            DLUtil.showAlertDialog(this, "Invalid Wickets Value", "Please enter a valid value for wickets");
            return null;
        }

        s.setWickets(wickets);

        Double overs_before = DLUtil.getValidOvers(overs_before_text.getText().toString(), team);

        if (overs_before == null) {
            DLUtil.showAlertDialog(this, "Invalid Overs Value", "Please enter a valid value for overs before suspension");
            return null;
        }

        if (overs_before > DLUtil.getStartOvers(team)) {
            DLUtil.showAlertDialog(this, "Invalid Overs Value", "Overs before suspension cannot be greater than initial allocated overs");
            return null;
        }

        s.setStartOvers(overs_before);

        Double overs_after = DLUtil.getValidOvers(overs_after_text.getText().toString(), team);

        if (overs_after == null) {
            DLUtil.showAlertDialog(this, "Invalid Overs Value", "Please enter a valid value for overs after suspension");
            return null;
        }

        if (overs_after > DLUtil.getStartOvers(team)) {
            DLUtil.showAlertDialog(this, "Invalid Overs Value", "Overs after suspension cannot be greater than initial allocated overs");
            return null;
        }

        if (overs_after > overs_before) {
            DLUtil.showAlertDialog(this, "Invalid Overs Value", "Overs after suspension cannot be greater than overs before suspension");
            return null;
        }

        s.setEndOvers(overs_after);

        return s;
    }

    @Override
    public void onSuspensionClicked(int position, Suspension suspension, int action) {

        currentSuspensionPosition = position;

        if (action == DLConstants.SUSPENSION_EDIT) {
            showAddEditSuspensionFragment(suspension);
        } else {
            deleteSuspension(null);
        }


    }

    public void moveToListSuspensions() {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack("listSuspensions", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.suspensions_container, ListSuspensionsFragment.newInstance(team));
        fragmentTransaction.commit();

        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

    }

    public void deleteSuspension() {

        List<Suspension> suspensions;

        if (team == DLConstants.TEAM_1) {
            suspensions = DLModel.getT1Suspensions();
        } else {
            suspensions = DLModel.getT2Suspensions();
        }

        suspensions.remove(currentSuspensionPosition);

        if (team == DLConstants.TEAM_1) {
            DLModel.setT1Suspensions(suspensions);
        } else {
            DLModel.setT2Suspensions(suspensions);
        }
    }
}
