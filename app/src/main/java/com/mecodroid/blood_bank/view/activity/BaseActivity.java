package com.mecodroid.blood_bank.view.activity;

import android.support.v7.app.AppCompatActivity;

import com.mecodroid.blood_bank.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {
    public BaseFragment baseFragment;

    public void superBackPressed() {
     // handle on back
        super.onBackPressed();
    }
}
