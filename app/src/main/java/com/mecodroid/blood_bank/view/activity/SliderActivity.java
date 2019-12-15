package com.mecodroid.blood_bank.view.activity;

import android.os.Bundle;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.fragment.splashCycle.SliderFragment;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;

public class SliderActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        // replace activity by splash fragment
        ReplaceFragment(getSupportFragmentManager(), new SliderFragment(),
                R.id.slider_acticity_fr_container, null, null);
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}
