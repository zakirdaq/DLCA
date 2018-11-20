package com.fl.dlc.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.fl.dlc.fragment.AboutUsFragment;
import com.fl.dlc.fragment.FinalResultFragment;
import com.fl.dlc.fragment.First_InningsDetailsFragment;
import com.fl.dlc.fragment.Second_InningsDetailsFragment;
import com.fl.dlc.fragment.TypeAndFormatFragment;


public class DLPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    public DLPagerAdapter(FragmentManager fm) {

        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case DLConstants.TYPE_AND_FORMAT_FRAGMENT:
                return TypeAndFormatFragment.newInstance();
            case DLConstants.TEAM1_DETAILS_FRAGMENT:
                return First_InningsDetailsFragment.newInstance();
            case DLConstants.TEAM2_DETAILS_FRAGMENT:
                return Second_InningsDetailsFragment.newInstance();
            case DLConstants.FINAL_RESULT_FRAGMENT:
                return FinalResultFragment.newInstance();
            case DLConstants.ABOUT_US_FRAGMENT:
                return AboutUsFragment.newInstance();
            default:
                return First_InningsDetailsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return DLConstants.NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case DLConstants.TYPE_AND_FORMAT_FRAGMENT:
                return "Type and Format";
            case DLConstants.TEAM1_DETAILS_FRAGMENT:
                return "First Innings Details";
            case DLConstants.TEAM2_DETAILS_FRAGMENT:
                return "Second Innings Details";
            case DLConstants.FINAL_RESULT_FRAGMENT:
                return "Final Result";
            case DLConstants.ABOUT_US_FRAGMENT:
                return "About DLC";
            default:
                return "First Innings Details";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        if (mTransaction == null) {
            mTransaction = mFragmentManager.beginTransaction();
        }

        mTransaction.hide((Fragment) object);
    }

}
