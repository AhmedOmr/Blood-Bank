package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.contact.Contact;
import com.mecodroid.blood_bank.data.model.setting.Setting;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class ContactUsFragment extends BaseFragment {

    View view;
    @BindView(R.id.contact_us_fragment_txt_phone)
    TextView contactUsFragmentTxtPhone;
    @BindView(R.id.contact_us_fragment_txt_mail)
    TextView contactUsFragmentTxtMail;
    @BindView(R.id.contact_us_fragment_et_title)
    EditText contactUsFragmentEtTitle;
    @BindView(R.id.contact_us_fragment_et_massage)
    EditText contactUsFragmentEtMassage;
    @BindView(R.id.contact_us_fragment_btn_send)
    Button contactUsFragmentBtnSend;
    Unbinder unbinder;


    private ApiServer apiServer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initFragment();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);


        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        setUpHomeActivity();
        inti();

        return view;
    }

    // initializer tools
    private void inti() {
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.contact_us));
        apiServer = getClient().create(ApiServer.class);
        getContactInfo();
    }

    private void getContactInfo() {
        if (isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));
            apiServer.getSettings(LoadStringData(getActivity(), API_TOKEN)).enqueue(new Callback<Setting>() {
                @Override
                public void onResponse(Call<Setting> call, Response<Setting> response) {
                    dismissProgressDialog();
                    try {
                        if (response.body().getStatus() == 1) {
                            contactUsFragmentTxtPhone.setText(response.body().getData().getPhone());
                            contactUsFragmentTxtMail.setText(response.body().getData().getEmail());
                        } else {
                            customMassageError(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {

                    }
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Setting> call, Throwable t) {
                    dismissProgressDialog();
                    customMassageError(getActivity(), t.getMessage());
                }
            });
            dismissProgressDialog();
        } else {

        }
    }




    @OnClick(R.id.contact_us_fragment_btn_send)
    public void onViewClicked() {
        SendMessage();
    }

    //send message
    public void SendMessage() {
        showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        // get  PaginationData governorates
        apiServer.SendContact(LoadStringData(getActivity(), API_TOKEN), contactUsFragmentEtTitle.getText().toString()
                , contactUsFragmentEtMassage.getText().toString()).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customMassageDone(getActivity(), response.body().getMsg());
                    contactUsFragmentTxtPhone.setText("");
                    contactUsFragmentTxtMail.setText("");
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    customMassageError(getActivity(), response.body().getMsg());

                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
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
