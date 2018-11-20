package com.fl.dlc.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fl.dlc.R;
import com.fl.dlc.util.DLConstants;
import com.fl.dlc.util.Suspension;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEditSuspensionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEditSuspensionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditSuspensionFragment extends Fragment {

    private Suspension suspension;
    private OnFragmentInteractionListener mListener;

    public AddEditSuspensionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param suspension Suspension
     * @return A new instance of fragment AddEditSuspensionFragment.
     */

    public static AddEditSuspensionFragment newInstance(Suspension suspension) {
        AddEditSuspensionFragment fragment = new AddEditSuspensionFragment();
        Bundle args = new Bundle();
        args.putSerializable(DLConstants.SUSPENSION_KEY, suspension);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            suspension = (Suspension) getArguments().getSerializable(DLConstants.SUSPENSION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_suspension, container, false);

        if (suspension == null) {
            //add suspension requested
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.edit_suspension_buttons_layout);
            layout.setVisibility(View.GONE);
            TextView textView = (TextView) view.findViewById(R.id.add_edit_suspension_header);
            textView.setText(getString(R.string.add_suspension_header));
        } else {
            //edit suspension requested
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.add_suspensions_button_layout);
            layout.setVisibility(View.GONE);
            TextView textView = (TextView) view.findViewById(R.id.add_edit_suspension_header);
            textView.setText(getString(R.string.edit_suspension_header));

            EditText score_text = (EditText) view.findViewById(R.id.suspension_score_text);
            score_text.setText(suspension.getScore() + "");

            EditText wickets_text = (EditText) view.findViewById(R.id.suspension_wickets_text);
            wickets_text.setText(suspension.getWickets() + "");

            EditText overs_before_text = (EditText) view.findViewById(R.id.suspension_overs_before_text);
            overs_before_text.setText(suspension.getStartOvers() + "");

            EditText overs_after_text = (EditText) view.findViewById(R.id.suspension_overs_after_text);
            overs_after_text.setText(suspension.getEndOvers() + "");
        }
        return view;
    }

/*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
