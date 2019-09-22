package com.mecodroid.blood_bank.view.fragment.loginCycle;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.api.RetrfitClient;
import com.mecodroid.blood_bank.data.model.resetpassword.ResetPassword;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPhone;

public class ForgetPasswordFragment extends BaseFragment {


    @BindView(R.id.forget_password_fragment_et_phone)
    EditText edit_Phone_fragment_forget_password;
    @BindView(R.id.forget_password_fragment_btn_send_code)
    Button btn_SendCode_fragment_forget_password;
    Unbinder unbinder;
    ApiServer apiServer;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        StatusBarUtil.setTransparent(getActivity());
        apiServer = RetrfitClient.getClient().create(ApiServer.class);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.forget_password_fragment_btn_send_code)
    public void onViewClicked() {
        getAllforgetPasswordFields();
    }

    // get all text in all component
    private void getAllforgetPasswordFields() {
        String phoneNumber = edit_Phone_fragment_forget_password.getText().toString();
        if (!isValidPhone(phoneNumber)) {
            edit_Phone_fragment_forget_password.setError(getResources().getString(R.string.invalid_phone));
            edit_Phone_fragment_forget_password.requestFocus();
            customMassageError(getActivity(),getResources().getString(R.string.invalid_phone));
            return;
        }
        sendReturnCod(phoneNumber);
    }

    // send code
    private void sendReturnCod(final String phoneNumber) {
        showProgressDialog(getActivity(),getResources().getString(R.string.waiit));
        apiServer.resetPassword(phoneNumber)
                .enqueue(new Callback<ResetPassword>() {
                    @Override
                    public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                       dismissProgressDialog();
                        if (response.isSuccessful() && response.body().getStatus() == 1) {
                            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                            changePasswordFragment.phoneNumber = phoneNumber;
                            ReplaceFragment(getActivity().getSupportFragmentManager(),
                                    changePasswordFragment, R.id.fragmentlogin_container,
                                    null, null);

                        } else {
                            customMassageError(getActivity(), response.body().getMsg());
                        }
                    }


                    @Override
                    public void onFailure(Call<ResetPassword> call, Throwable t) {
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
