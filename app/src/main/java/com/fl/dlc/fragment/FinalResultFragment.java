package com.fl.dlc.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fl.dlc.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinalResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinalResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinalResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FinalResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FinalResultFragment.
     */

    public static FinalResultFragment newInstance() {
        FinalResultFragment fragment = new FinalResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_final_result, container, false);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                final AdView adView = (AdView) view.findViewById(R.id.adView);
                final AdRequest adRequest = new AdRequest.Builder().build();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adView.loadAd(adRequest);
                    }
                });
            }
        }).start();*/

        return view;
    }

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
