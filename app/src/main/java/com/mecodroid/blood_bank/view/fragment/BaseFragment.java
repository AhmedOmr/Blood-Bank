package com.mecodroid.blood_bank.view.fragment;

import android.support.v4.app.Fragment;

import com.mecodroid.blood_bank.view.activity.BaseActivity;
import com.mecodroid.blood_bank.view.activity.HomeActivity;

public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;
    public HomeActivity homeActivity;

    public void initFragment() {
        baseActivity = (BaseActivity) getActivity(); // casting to access any object in  Activity
        baseActivity.baseFragment = this; // assign to opening Fragment
    }

    public void setUpHomeActivity() {
        homeActivity = (HomeActivity) getActivity();
    }

    public void onBack() {
        baseActivity.superBackPressed();
    }

}
