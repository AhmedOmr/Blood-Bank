package com.mecodroid.blood_bank.view.fragment.splashCycle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.activity.SliderActivity;
import com.mecodroid.blood_bank.view.activity.UserActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.REMEMBER_USER;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadBoolean;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.isFirstTimeLaunch;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.setFirstTimeLaunch;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends BaseFragment {

    final int SPLASH_TIME_OUT = 3000;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        View splahFragment = inflater.inflate(R.layout.fragment_splash, container, false);
        StatusBarUtil.setTransparent(getActivity());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        checkUserLogin();

                }
            }, SPLASH_TIME_OUT);

        } catch (Exception e) {
            Snackbar.make(null, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }

        return splahFragment;
    }

    private void checkUserLogin() {
        if (!isFirstTimeLaunch(getActivity())) {
            if (LoadStringData(getActivity(), API_TOKEN) != null &&
                LoadBoolean(getActivity(), REMEMBER_USER)) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        } else {
            startActivity(new Intent(getActivity(), UserActivity.class));
            getActivity().finish();
        }
        }else {
            setFirstTimeLaunch(getActivity(), false);
            startActivity(new Intent(getActivity(), SliderActivity.class));
            getActivity().finish();
        }
    }
    @Override
    public void onBack() {
        getActivity().finish();
    }
}
