package com.mecodroid.blood_bank.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.fragment.loginCycle.LoginFragment;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;

public class RegsteratinAndLoginActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsteratin_and_login);

        // replace activity by splash fragment
        ReplaceFragment(getSupportFragmentManager(),
                new LoginFragment(), R.id.fragmentlogin_container, null, null);
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}
