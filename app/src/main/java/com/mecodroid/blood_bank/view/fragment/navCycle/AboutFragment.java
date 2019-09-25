package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;



public class AboutFragment extends BaseFragment {

    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about, container, false);

        unbinder = ButterKnife.bind(this, view);

        // add value tool bar
        homeActivity.setTitle( getResources().getString(R.string.about));


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onBack() {
        super.onBack();
    }
}
