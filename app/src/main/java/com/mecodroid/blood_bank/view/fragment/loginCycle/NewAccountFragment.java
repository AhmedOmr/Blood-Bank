package com.mecodroid.blood_bank.view.fragment.loginCycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.api.RetrfitClient;
import com.mecodroid.blood_bank.data.model.register.Register;
import com.mecodroid.blood_bank.data.model.register.RegisterData;
import com.mecodroid.blood_bank.helper.DateModel;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import java.util.Calendar;

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
import static com.mecodroid.blood_bank.helper.BloodBankConatants.GOV_NAME;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PASSWORD;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PHONE;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.REMEMBER_USER;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.USER_NAME;
import static com.mecodroid.blood_bank.helper.CommonModelMethod.getAllBloodTypes;
import static com.mecodroid.blood_bank.helper.CommonModelMethod.getAllGovern;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.isRTL;
import static com.mecodroid.blood_bank.helper.HelperMethod.showCalender;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.SaveData;
import static com.mecodroid.blood_bank.helper.Vaildation.isIdenticalPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidBirthday;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidEmail;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidName;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPhone;
import static com.mecodroid.blood_bank.helper.Vaildation.isValiddonationday;

/**
 * A simple {@link Fragment} subclass.
 */
// create New Account
public class NewAccountFragment extends BaseFragment {

    @BindView(R.id.new_account_fragment_et_name)
    EditText newAccountFragmentEtName;
    @BindView(R.id.new_account_fragment_et_email)
    EditText newAccountFragmentEtEmail;
    @BindView(R.id.new_account_fragment_txt_Birth_date)
    TextView newAccountFragmentTxtBirthDate;
    @BindView(R.id.new_account_fragment_spin_blood_type)
    Spinner newAccountFragmentSpinBloodType;
    @BindView(R.id.new_account_fragment_txt_last_date_donation)
    TextView newAccountFragmentTxtLastDateDonation;
    @BindView(R.id.new_account_fragment_spin_governorate)
    Spinner newAccountFragmentSpinGovernorate;
    @BindView(R.id.new_account_fragment_spin_city)
    Spinner newAccountFragmentSpinCity;
    @BindView(R.id.new_account_fragment_et_phone)
    EditText newAccountFragmentEtPhone;
    @BindView(R.id.new_account_fragment_et_password)
    EditText newAccountFragmentEtPassword;
    @BindView(R.id.new_account_fragment_et_confirm_password)
    EditText newAccountFragmentEtConfirmPassword;
    @BindView(R.id.new_account_fragment_btn_signup)
    Button newAccountFragmentBtnSignup;

    private String blood_type_id, startCityId;
    private ApiServer apiServer;
    private Unbinder unbinder;

    public NewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        View newAccount = inflater.inflate(R.layout.fragment_new_account, container, false);
        // status bar
        StatusBarUtil.setColor(getActivity(), getActivity().getResources().getColor(R.color.thick_blue));

        baseActivity.baseFragment = this;

        apiServer = RetrfitClient.getClient().create(ApiServer.class);
        unbinder = ButterKnife.bind(this, newAccount);

        // check Language
        if (isRTL()) {

            newAccountFragmentSpinCity.setBackground(getResources().getDrawable(R.drawable.bgspinrt));
            newAccountFragmentSpinBloodType.setBackground(getResources().getDrawable(R.drawable.bgspinrt));
            newAccountFragmentSpinGovernorate.setBackground(getResources().getDrawable(R.drawable.bgspinrt));

        } else {

            newAccountFragmentSpinCity.setBackground(getResources().getDrawable(R.drawable.bgspinlt));
            newAccountFragmentSpinBloodType.setBackground(getResources().getDrawable(R.drawable.bgspinlt));
            newAccountFragmentSpinGovernorate.setBackground(getResources().getDrawable(R.drawable.bgspinlt));
            newAccountFragmentSpinCity.setPaddingRelative(0, 0, 80, 0);
            newAccountFragmentSpinGovernorate.setPaddingRelative(0, 0, 80, 0);

        }
        getAllBloodTypes(getActivity(), apiServer, newAccountFragmentSpinBloodType);
        getAllGovern(getActivity(), apiServer, newAccountFragmentSpinGovernorate, newAccountFragmentSpinCity);

        return newAccount;
    }

    @OnClick({R.id.new_account_fragment_txt_Birth_date,
            R.id.new_account_fragment_txt_last_date_donation,
            R.id.new_account_fragment_btn_signup})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        try {
            switch (view.getId()) {
                case R.id.new_account_fragment_txt_Birth_date:
                    setDateCalendar(newAccountFragmentTxtBirthDate);
                    break;

                case R.id.new_account_fragment_txt_last_date_donation:
                    setDateCalendar(newAccountFragmentTxtLastDateDonation);
                    break;

                case R.id.new_account_fragment_btn_signup:
                    getAllRegisterFields();
                    break;
                default:
                    customMassageError(getActivity(), "Error");
            }
        } catch (Exception e) {
            customMassageError(getActivity(), e.getMessage());

        }


    }

    // get all text in component
    private void getAllRegisterFields() {
        String name = newAccountFragmentEtName.getText().toString();
        String email = newAccountFragmentEtEmail.getText().toString();
        String birth_date = newAccountFragmentTxtBirthDate.getText().toString();
        String donation_last_date = newAccountFragmentTxtLastDateDonation.getText().toString();
        String phone = newAccountFragmentEtPhone.getText().toString();
        String password = newAccountFragmentEtPassword.getText().toString();
        String password_confirmation = newAccountFragmentEtConfirmPassword.getText().toString();
        checkValidationRegisteration(name, email, birth_date, phone,
                donation_last_date, password, password_confirmation);
    }

    // return date
    public void setDateCalendar(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        String yy = String.valueOf(calendar.get(Calendar.YEAR));
        String mm = String.valueOf(calendar.get(Calendar.MONTH));
        String dd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        DateModel dateModel = new DateModel(dd, mm, yy, yy + "-" + mm + "-" + dd);
        showCalender(getActivity(), null, textView, dateModel);
    }

    // check fields validation
    private void checkValidationRegisteration(String name, String email, String birth_date, String phone,
                                              String donation_last_date, String password, String password_confirmation) {
        if (!isValidName(name)) {
            newAccountFragmentEtName.setError("Invalid name");
            newAccountFragmentEtName.requestFocus();
            customMassageError(getActivity(), "Invalid name");
            return;
        }
        if (!isValidEmail(email)) {
            newAccountFragmentEtEmail.setError("Invalid Email");
            newAccountFragmentEtEmail.requestFocus();
            customMassageError(getActivity(), "Invalid Email");
            return;
        }
        if (!isValidBirthday(birth_date)) {
            newAccountFragmentTxtBirthDate.setError("Invalid Birth date  or less/ more than allowed");
            newAccountFragmentTxtBirthDate.requestFocus();
            customMassageError(getActivity(), "Invalid Birth date  or less/ more than allowed");
            return;
        }

        if (!isValiddonationday(donation_last_date)) {
            newAccountFragmentTxtLastDateDonation.setError("Invalid donation  date or more than allowed");
            newAccountFragmentTxtLastDateDonation.requestFocus();
            customMassageError(getActivity(), "Invalid donation  date or more than allowed");
            return;
        }

        if (!isValidPhone(phone)) {
            newAccountFragmentEtPhone.setError("Invalid phone");
            newAccountFragmentEtPhone.requestFocus();
            customMassageError(getActivity(), "Invalid phone");
            return;
        }

        if (!isValidPassword(password)) {
            newAccountFragmentEtPassword.setError("Invalid password");
            newAccountFragmentEtPassword.requestFocus();
            customMassageError(getActivity(), "Invalid password ");
            return;
        }

        if (!isIdenticalPassword(password, password_confirmation)) {
            newAccountFragmentEtConfirmPassword.setError("Password is not identical");
            newAccountFragmentEtConfirmPassword.requestFocus();
            customMassageError(getActivity(), "Password is not identical");
            return;
        }

        signUp(name, email, birth_date, phone, donation_last_date, password, password_confirmation);
    }

    // create new Account
    private void signUp(String name, String email, String birth_date, String phone,
                        String donation_last_date, final String password, String password_confirmation) {
        if (isConnected(getActivity())) {
            showProgressDialog(getActivity(), getResources().getString(R.string.creating_account));
            apiServer.
                    addNewAcount(name, email, birth_date, startCityId, phone, donation_last_date, password, password_confirmation, blood_type_id)
                    .enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Call<Register> call, Response<Register> response) {
                            dismissProgressDialog();

                            try {
                                if (response.body().getStatus() == 1) {
                                    RegisterData dataRegister = response.body().getRegisterData();
                                    String apiToken = dataRegister.getApiToken();
                                    String name = dataRegister.getClient().getName();
                                    String email = dataRegister.getClient().getEmail();
                                    String phone = dataRegister.getClient().getPhone();
                                    String birthDate = dataRegister.getClient().getBirthDate();
                                    String donationLastDate = dataRegister.getClient().getDonationLastDate();
                                    String cityId = String.valueOf(dataRegister.getClient().getCityId());
                                    String governorateId = dataRegister.getClient().getCity().getGovernorateId();

                                    SaveData(getActivity(), API_TOKEN, apiToken);
                                    SaveData(getActivity(), USER_NAME, name);
                                    SaveData(getActivity(), EMAIL, email);
                                    SaveData(getActivity(), PHONE, phone);
                                    SaveData(getActivity(), BIRTH_DATE, birthDate);
                                    SaveData(getActivity(), DONATION_LAST_DATE, donationLastDate);
                                    SaveData(getActivity(), CITY_ID, cityId);
                                    SaveData(getActivity(), GOV_NAME, governorateId);

                                    SaveData(getActivity(), BLOOD_TYPE, blood_type_id);
                                    SaveData(getActivity(), PASSWORD, password);

                                    SaveData(getActivity(), REMEMBER_USER, true);

                                    customMassageDone(getActivity(), response.body().getMsg());

                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();

                                } else {
                                    customMassageError(getActivity(), response.body().getMsg());
                                }

                            } catch (Exception e) {
                                customMassageError(getActivity(), e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<Register> call, Throwable t) {
                            dismissProgressDialog();
                            customMassageError(getActivity(), t.getMessage());

                        }
                    });
        } else {
            customMassageError(getActivity(), getResources().getString(R.string.no_internet));
        }
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

