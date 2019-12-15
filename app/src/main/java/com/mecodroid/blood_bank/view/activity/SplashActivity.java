package com.mecodroid.blood_bank.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.fragment.splashCycle.SplashFragment;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // replace activity by splash fragment
        ReplaceFragment(getSupportFragmentManager(), new SplashFragment(),
                R.id.splash_acticity_fr_container, null, null);
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

}
