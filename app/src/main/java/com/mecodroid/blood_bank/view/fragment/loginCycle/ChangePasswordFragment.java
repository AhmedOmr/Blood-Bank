package com.mecodroid.blood_bank.view.fragment.loginCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.RetrfitClient;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.newpassword.NewPassword;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.helper.BloodBankConatants.PASSWORD;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PHONE;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.SaveData;
import static com.mecodroid.blood_bank.helper.Vaildation.isIdenticalPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPassword;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends BaseFragment {


    public String phoneNumber;
    @BindView(R.id.change_password_fragment_et_pin_code)
    EditText fragment_change_password_et_pinCode;
    @BindView(R.id.change_password_fragment_et_new_pasword)
    EditText fragment_change_password_et_Newpassword;
    @BindView(R.id.change_password_fragment_et_confirm_password)
    EditText fragment_change_password_confirmPasssword;
    @BindView(R.id.change_password_fragment_btn_change_password)
    Button fragment_change_password_btn_submitChange;
    Unbinder unbinder;

    private ApiServer apiServer;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        StatusBarUtil.setTransparent(getActivity());
        apiServer = RetrfitClient.getClient().create(ApiServer.class);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }




    @OnClick(R.id.change_password_fragment_btn_change_password)
    public void onViewClicked() {
      getAllChangePasswordFields();
    }

    //get all text in all component
    private void getAllChangePasswordFields() {
        String pinCode = fragment_change_password_et_pinCode.getText().toString();
        String password = fragment_change_password_et_Newpassword.getText().toString();
        String confirmPassword = fragment_change_password_confirmPasssword.getText().toString();
        checkValidation(pinCode, password, confirmPassword);
    }

    // check validation
    private void checkValidation(String pinCode, String password, String confirmPassword) {
        if (!isValidPassword(password)) {
            fragment_change_password_et_Newpassword.setError(getString(R.string.invalid_pass));
            fragment_change_password_et_Newpassword.requestFocus();
            customMassageError(getActivity(),getResources().getString(R.string.invalid_pass));
            return;
        }
        if (!isIdenticalPassword(password, confirmPassword)) {
            fragment_change_password_confirmPasssword.setError(getResources().getString(R.string.pass_notidentical));
            fragment_change_password_confirmPasssword.requestFocus();
            customMassageError(getActivity(),getResources().getString(R.string.pass_notidentical));
            return;
        }
        createNewPassword(password, confirmPassword, pinCode,phoneNumber);
    }

    // create new password
    public void createNewPassword(String pinCode, final String password, String confirmPassword, final String phone) {
        showProgressDialog(getActivity(),getResources().getString(R.string.code_was_sent));
        apiServer.inputNewPassword(pinCode, password, confirmPassword, phone)
                .enqueue(new Callback<NewPassword>() {
                    public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                       dismissProgressDialog();
                       try {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 1) {

                                    SaveData(getActivity(), PASSWORD, password);
                                    SaveData(getActivity(), PHONE, phone);


                                    ReplaceFragment(getActivity().getSupportFragmentManager(), new LoginFragment(),
                                            R.id.fragmentlogin_container, null, null);
                                    customMassageDone(getActivity(), response.body().getMsg());

                                }

                            }else{
                                customMassageError(getActivity(), response.body().getMsg());

                            }
                        } catch (Exception e) {
                            customMassageError(getActivity(), e.getMessage());
                        }

                    }


                    @Override
                    public void onFailure(Call<NewPassword> call, Throwable t) {
                        dismissProgressDialog();
                       customMassageError(getActivity(), t.getMessage());

                    }
                });

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