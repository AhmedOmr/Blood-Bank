package com.mecodroid.blood_bank.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.fragment.splashCycle.SplashFragment;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;

public class SplashSliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_splash);

        // replace activity by splash fragment
        ReplaceFragment(getSupportFragmentManager(), new SplashFragment(),
                R.id.splash_slider_acticity_fr_lconayotaut_container, null, null);
    }
}
