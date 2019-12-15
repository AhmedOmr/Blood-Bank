package com.mecodroid.blood_bank.view.fragment.loginCycle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.api.RetrfitClient;
import com.mecodroid.blood_bank.data.model.login.Login;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.BIRTH_DATE;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.BLOOD_TYPE;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.CITY_ID;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.DONATION_LAST_DATE;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.EMAIL;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PASSWORD;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PHONE;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.REMEMBER_USER;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.USER_NAME;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissLovelyDailog;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.setLovelyProgressDailog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadBoolean;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.SaveData;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPhone;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    @BindView(R.id.login_fragment_img_logo)
    ImageView loginFragmentImgLogo;
    @BindView(R.id.login_fragment_et_phone)
    EditText loginFragmentEtPhone;
    @BindView(R.id.login_fragment_et_password)
    EditText loginFragmentEtPassword;
    @BindView(R.id.login_fragment_ch_box_remember)
    CheckBox loginFragmentChBoxRemember;
    @BindView(R.id.login_fragment_txt_remember)
    TextView loginFragmentTxtRemember;
    @BindView(R.id.login_fragment_rl_remember_me)
    RelativeLayout loginFragmentEtRememberMe;
    @BindView(R.id.login_fragment_img_forget)
    ImageView loginFragmentImgForget;
    @BindView(R.id.login_fragment_txt_forget)
    TextView loginFragmentTxtForget;
    @BindView(R.id.login_fragment_Rl_forget_password)
    RelativeLayout loginFragmentRlForgetPassword;
    @BindView(R.id.login_fragment_et_password_setting)
    LinearLayout loginFragmentEtPasswordSetting;
    @BindView(R.id.login_fragment_btn_login)
    Button loginFragmentBtnLogin;
    @BindView(R.id.login_fragment_btn_register)
    Button loginFragmentBtnRegister;

    ApiServer apiServer;
    private Unbinder unbinder;
    private boolean Checked;

    // constructor of fragment
    public LoginFragment() {
        // Required empty public constructor
    }

    // create fragment view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        View fLoginFragment = inflater.inflate(R.layout.fragment_login, container, false);
        StatusBarUtil.setTransparent(getActivity());

        unbinder = ButterKnife.bind(this, fLoginFragment);
        apiServer = RetrfitClient.getClient().create(ApiServer.class);
        rememberUser();

        // To return status to checkbox when create view, so dont retrun default status
        loginFragmentChBoxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Checked = isChecked;
            }
        });
        return fLoginFragment;
    }

    // remember user
    private void rememberUser() {
        // check the status of check box
        if (LoadBoolean(getActivity(), REMEMBER_USER)) {
            loginFragmentChBoxRemember.setChecked(true);
            loginFragmentEtPhone.setText(LoadStringData(getActivity(), PHONE));
            loginFragmentEtPassword.setText(LoadStringData(getActivity(), PASSWORD));
        } else {
            loginFragmentChBoxRemember.setChecked(false);
            loginFragmentEtPhone.setText("");
            loginFragmentEtPassword.setText("");
        }

        // save the status of check box
        SaveData(getActivity(), REMEMBER_USER, loginFragmentChBoxRemember.isChecked());
    }

    @OnClick({R.id.login_fragment_Rl_forget_password,
            R.id.login_fragment_btn_login,
            R.id.login_fragment_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_Rl_forget_password:
                ReplaceFragment(getActivity().getSupportFragmentManager(), new ForgetPasswordFragment(),
                        R.id.fragmentlogin_container, null, null);
                break;

            case R.id.login_fragment_btn_login:
                disappearKeypad(getActivity(), loginFragmentBtnLogin);
                getAllLoginFields();
                break;

            case R.id.login_fragment_btn_register:
                disappearKeypad(getActivity(), loginFragmentBtnRegister);
                ReplaceFragment(getActivity().getSupportFragmentManager(), new NewAccountFragment(),
                        R.id.fragmentlogin_container, null, null);

                break;
        }
    }

    // get All Login Components
    private void getAllLoginFields() {
        String phone = loginFragmentEtPhone.getText().toString();
        String password = loginFragmentEtPassword.getText().toString();
        checkValidationLogin(phone, password);

    }

    // check validation of all Component
    private void checkValidationLogin(String phone, String password) {
        if (!isValidPhone(phone)) {
            loginFragmentEtPhone.setError(getResources().getString(R.string.invalid_phone));
            loginFragmentEtPhone.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.invalid_phone));
            return;
        }
        if (!isValidPassword(password)) {
            loginFragmentEtPassword.setError(getResources().getString(R.string.invalid_pass));
            loginFragmentEtPassword.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.invalid_pass));
            return;
        }
        logIn(phone, password);

    }

    // Log in
    private void logIn(String phone, final String password) {
        // check internet
        if (isConnected(getActivity())) {
            setLovelyProgressDailog(getActivity(),
                    R.drawable.icon_login24,
                    getResources().getString(R.string.logging_in),
                    Color.WHITE, R.color.thick_blue);

            apiServer.addLogin(phone, password).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dismissLovelyDailog();
                    try {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                String apiToken = response.body().getData().getApiToken();
                                String name = response.body().getData().getClient().getName();
                                String email = response.body().getData().getClient().getEmail();
                                String phone = response.body().getData().getClient().getPhone();
                                String birthDate = response.body().getData().getClient().getBirthDate();
                                String donationLastDate = response.body().getData().getClient().getDonationLastDate();
                                String bloodType = String.valueOf(response.body().getData().getClient().getBloodType());
                                String cityId = String.valueOf(response.body().getData().getClient().getCity());

                                saveData(apiToken, name, email, phone, birthDate, donationLastDate,
                                        cityId, bloodType, password);
                                SaveData(getActivity(), REMEMBER_USER, loginFragmentChBoxRemember.isChecked());

                                customMassageDone(getActivity(), response.body().getMsg());

                                Intent logIntent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(logIntent);
                                getActivity().finish();

                            } else if (response.body().getStatus() != 1) {
                                customMassageError(getActivity(), response.body().getMsg());
                            }
                        }
                    } catch (Exception e) {
                        customMassageError(getActivity(), e.getMessage());

                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    dismissLovelyDailog();
                    customMassageError(getActivity(), t.getMessage());

                }
            });
        } else {

        }
    }

    // save data of all client
    private void saveData(String apiToken, String name, String email, String phone,
                          String birthDate, String donationLastDate, String cityId,
                          String bloodType, String password) {
        SaveData(getActivity(), API_TOKEN, apiToken);
        SaveData(getActivity(), USER_NAME, name);
        SaveData(getActivity(), EMAIL, email);
        SaveData(getActivity(), PHONE, phone);
        SaveData(getActivity(), BIRTH_DATE, birthDate);
        SaveData(getActivity(), DONATION_LAST_DATE, donationLastDate);
        SaveData(getActivity(), CITY_ID, cityId);
        SaveData(getActivity(), BLOOD_TYPE, bloodType);
        SaveData(getActivity(), PASSWORD, password);
        SaveData(getActivity(), REMEMBER_USER, Checked);

    }

    @Override
    public void onStop() {
        super.onStop();
        rememberUser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rememberUser();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }

}