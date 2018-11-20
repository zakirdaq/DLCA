package com.fl.dlc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fl.dlc.R;
import com.fl.dlc.util.DLConstants;
import com.fl.dlc.util.DLModel;
import com.fl.dlc.util.Suspension;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.fl.dlc.fragment.ListSuspensionsFragment.OnSuspensionClickedListener} interface
 * to handle interaction events.
 * Use the {@link ListSuspensionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSuspensionsFragment extends Fragment {

    ArrayAdapter<Suspension> adapter;
    private OnSuspensionClickedListener mListener;
    private int team;
    private List<Suspension> suspensions;

    public ListSuspensionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param team Team number.
     * @return A new instance of fragment ListSuspensionsFragment.
     */

    public static ListSuspensionsFragment newInstance(int team) {
        ListSuspensionsFragment fragment = new ListSuspensionsFragment();
        Bundle args = new Bundle();
        args.putInt(DLConstants.TEAM_ID, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team = getArguments().getInt(DLConstants.TEAM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_suspensions, container, false);

        if (team == DLConstants.TEAM_1) {
            suspensions = DLModel.getT1Suspensions();
        } else {
            suspensions = DLModel.getT2Suspensions();
        }

        TextView textView = (TextView) view.findViewById(R.id.suspensions_list_empty_text);
        ListView listView = (ListView) view.findViewById(R.id.suspensions_list);

        if (suspensions != null && suspensions.size() > 0) {
            textView.setText(getString(R.string.suspensions_list_non_empty_text));
            //listView.setVisibility(View.VISIBLE);
            //adapter = new ListSuspensionAdapter(getActivity(), suspensions);
            adapter = new ArrayAdapter<Suspension>(getActivity(), R.layout.layout_list_suspension, suspensions);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.onSuspensionClicked(position, suspensions.get(position), DLConstants.SUSPENSION_EDIT);
                }
            });

            registerForContextMenu(listView);

        } else {
            //textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textView.setText(getString(R.string.suspensions_list_empty_text));
        }

        return view;
    }


    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSuspensionClickedListener) activity;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Choose an option");
        menu.add(0, view.getId(), 0, "Edit");
        menu.add(0, view.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = adapterContextMenuInfo.position;

        if (item.getTitle() == "Edit") {

            mListener.onSuspensionClicked(position, suspensions.get(position), DLConstants.SUSPENSION_EDIT);
            return true;
        }

        if (item.getTitle() == "Delete") {
            mListener.onSuspensionClicked(position, suspensions.get(position), DLConstants.SUSPENSION_DELETE);
            return true;
        }

        return false;
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
    public interface OnSuspensionClickedListener {
        public void onSuspensionClicked(int position, Suspension suspension, int action);
    }

}
