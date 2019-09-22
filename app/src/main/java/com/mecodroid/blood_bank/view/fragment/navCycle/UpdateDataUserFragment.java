package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.CityDataModel;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;
import com.mecodroid.blood_bank.data.model.profile.Profile;
import com.mecodroid.blood_bank.data.model.profileedit.ProfileEdit;
import com.mecodroid.blood_bank.helper.DateModel;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.PASSWORD;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showCalender;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;
import static com.mecodroid.blood_bank.helper.Vaildation.isIdenticalPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidBirthday;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidEmail;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidName;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPassword;
import static com.mecodroid.blood_bank.helper.Vaildation.isValidPhone;
import static com.mecodroid.blood_bank.helper.Vaildation.isValiddonationday;

public class UpdateDataUserFragment extends BaseFragment {


    @BindView(R.id.update_data_user_fragment_et_name)
    EditText updateDataUserFragmentEtName;
    @BindView(R.id.update_data_user_fragment_et_email)
    EditText updateDataUserFragmentEtEmail;
    @BindView(R.id.update_data_user_fragment_txt_birth_date)
    TextView updateDataUserFragmentTxtBirthDate;
    @BindView(R.id.update_data_user_fragment_spin_blood_type)
    Spinner updateDataUserFragmentSpinBloodType;
    @BindView(R.id.update_data_user_fragment_txt_last_don_date)
    TextView updateDataUserFragmentTxtLastDonDate;
    @BindView(R.id.update_data_user_fragment_spin_gov)
    Spinner updateDataUserFragmentSpinGov;
    @BindView(R.id.update_data_user_fragment_spin_city)
    Spinner updateDataUserFragmentSpinCity;
    @BindView(R.id.update_data_user_fragment_et_pass)
    EditText updateDataUserFragmentEtPass;
    @BindView(R.id.update_data_user_fragment_et_confirm_pass)
    EditText updateDataUserFragmentEtConfirmPass;
    @BindView(R.id.update_data_user_fragment_et_phone)
    EditText updateDataUserFragmentEtPhone;
    @BindView(R.id.update_data_user_fragment_btn_update)
    Button updateDataUserFragmentBtnUpdate;
    Unbinder unbinder;

    ApiServer apiServer;
    private View view;

    private Integer returnIcCity;
    private String blood_type_id;
    private String startCityId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_data_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        //
        inti();

        getAllGovernorate();
        getAllBloodTypes();

        getDataProfile();

        return view;
    }

    private void inti() {
        apiServer = getClient().create(ApiServer.class);
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.modify_the_data));

    }


    @OnClick(R.id.update_data_user_fragment_btn_update)
    public void onViewClicked() {
        getAllDataFields();
    }

    private void getAllDataFields() {
        String name = updateDataUserFragmentEtName.getText().toString();
        String email = updateDataUserFragmentEtEmail.getText().toString();
        String birth_date = updateDataUserFragmentTxtBirthDate.getText().toString();
        String donation_last_date = updateDataUserFragmentTxtLastDonDate.getText().toString();
        String phone = updateDataUserFragmentEtPhone.getText().toString();
        String password = updateDataUserFragmentEtPass.getText().toString();
        String password_confirmation = updateDataUserFragmentEtConfirmPass.getText().toString();
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
            updateDataUserFragmentEtName.setError("Invalid name");
            customMassageError(getActivity(), "Invalid name");

            return;
        }
        if (!isValidEmail(email)) {
            updateDataUserFragmentEtEmail.setError("Invalid Email");
            customMassageError(getActivity(), "Invalid Email");
            return;
        }
        if (!isValidBirthday(birth_date)) {
            updateDataUserFragmentTxtBirthDate.setError("Invalid Birth date  or less/ more than allowed");
            customMassageError(getActivity(), "Invalid Birth date  or less/ more than allowed");
            return;
        }

        if (!isValiddonationday(donation_last_date)) {
            updateDataUserFragmentTxtLastDonDate.setError("Invalid donation  date or more than allowed");
            customMassageError(getActivity(), "Invalid donation  date or more than allowed");
            return;
        }

        if (!isValidPhone(phone)) {
            updateDataUserFragmentEtPhone.setError("Invalid phone");
            customMassageError(getActivity(), "Invalid phone");
            return;
        }

        if (!isValidPassword(password)) {
            updateDataUserFragmentEtPass.setError("Invalid password");
            customMassageError(getActivity(), "Invalid password ");
            return;
        }

        if (!isIdenticalPassword(password, password_confirmation)) {
            updateDataUserFragmentEtConfirmPass.setError("Password is not identical");
            customMassageError(getActivity(), "Password is not identical");
            return;
        }

        updateData(name, email, birth_date, phone, donation_last_date, password, password_confirmation);
    }

    private void updateData(String name, String email, String birth_date, String phone, String donation_last_date, String password, String password_confirmation) {
        apiServer.onUpdate(name, email, birth_date, startCityId, phone, donation_last_date, password, password_confirmation
                , blood_type_id, LoadStringData(getActivity(), API_TOKEN)).enqueue(new Callback<ProfileEdit>() {
            @Override
            public void onResponse(Call<ProfileEdit> call, Response<ProfileEdit> response) {

                if (response.body().getStatus() == 1) {

                    showProgressDialog(getActivity(), getResources().getString(R.string.loading));

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ProfileEdit> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }


    public void getDataProfile() {
        apiServer.getProfile(LoadStringData(getActivity(), API_TOKEN)).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.body().getStatus() == 1) {
                    dismissProgressDialog();
                    try {

                        updateDataUserFragmentEtName.setText(response.body().getProfileData().getClient().getName());
                        updateDataUserFragmentEtEmail.setText(response.body().getProfileData().getClient().getEmail());
                        updateDataUserFragmentTxtBirthDate.setText(response.body().getProfileData().getClient().getBirthDate());
                        updateDataUserFragmentTxtLastDonDate.setText(response.body().getProfileData().getClient().getDonationLastDate());
                        updateDataUserFragmentEtPhone.setText(response.body().getProfileData().getClient().getPhone());
                        updateDataUserFragmentEtPass.setText(LoadStringData(getActivity(), PASSWORD));
                        updateDataUserFragmentEtConfirmPass.setText(LoadStringData(getActivity(), PASSWORD));

                        updateDataUserFragmentSpinBloodType.setSelection((response.body().getProfileData().getClient().getBloodType().getId()));
                        updateDataUserFragmentSpinGov.setSelection((response.body().getProfileData().getClient().getCity().getGovernorate().getId()));
                        returnIcCity = response.body().getProfileData().getClient().getCity().getId();


                    } catch (Exception e) {
                        dismissProgressDialog();
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }


    // glet all blood Types
    public void getAllBloodTypes() {
        showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        apiServer.getBloodTypes().enqueue(new Callback<BloodTypes>() {
            @Override
            public void onResponse(Call<BloodTypes> call, Response<BloodTypes> response) {
                dismissProgressDialog();
                List<GeneralModel> bloodTypesData = response.body().getData();
                // store blood type name
                final ArrayList<String> typeBlood = new ArrayList<>();
                // store blood type id
                final ArrayList<Integer> idBlood = new ArrayList<Integer>();
                // title blood type
                typeBlood.add(getString(R.string.blood_type).trim());
                idBlood.add(0);
                // loop all blood types and pass name types to blood name list, pass the id of blood types to blood id list
                for (int i = 0; i < bloodTypesData.size(); i++) {
                    String bloodTypNAme = bloodTypesData.get(i).getName();
                    Integer bloodTypId = bloodTypesData.get(i).getId();
                    typeBlood.add(bloodTypNAme);
                    idBlood.add(bloodTypId);
                }

                // create array adapter to view list
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, typeBlood) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                // to specify form of spinner
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                // bind spinner with adapter
                updateDataUserFragmentSpinBloodType.setAdapter(adapter);


                // interaction with spinner
                updateDataUserFragmentSpinBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // return the item selected from spinner else postion equal zero (title)
                        blood_type_id = String.valueOf(idBlood.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<BloodTypes> call, Throwable t) {
                dismissProgressDialog();
            }
        });

    }

    // get all Governratrate
    public void getAllGovernorate() {
        apiServer.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                List<GeneralModel> governoratesDatumList = response.body().getData();
                ArrayList<String> governorat = new ArrayList<>();
                final ArrayList<Integer> idGovern = new ArrayList<>();
                governorat.add(getString(R.string.choosegovernorate));
                idGovern.add(0);

                for (int i = 0; i < governoratesDatumList.size(); i++) {
                    String governorateName = governoratesDatumList.get(i).getName();
                    Integer governoratId = governoratesDatumList.get(i).getId();
                    governorat.add(governorateName);
                    idGovern.add(governoratId);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, governorat) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                updateDataUserFragmentSpinGov.setAdapter(adapter);

                updateDataUserFragmentSpinGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            getAllCity((idGovern.get(position)));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                dismissProgressDialog();
            }

        });
    }

    // get all city
    public void getAllCity(Integer gavernoratesId) {
        showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        apiServer.getCities(gavernoratesId)
                .enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        dismissProgressDialog();
                        List<GeneralModel> citiesDatumList = response.body().getData();
                        ArrayList<String> cities = new ArrayList<>();
                        final ArrayList<Integer> citiesId = new ArrayList<>();

                        cities.add(getString(R.string.choosecity));
                        citiesId.add(0);

                        for (int i = 0; i < citiesDatumList.size(); i++) {
                            String cityName = citiesDatumList.get(i).getName();
                            Integer cityId = citiesDatumList.get(i).getId();

                            cities.add(cityName);
                            citiesId.add(cityId);
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, cities) {
                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint

                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

                        updateDataUserFragmentSpinCity.setAdapter(adapter);

                        updateDataUserFragmentSpinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    startCityId = String.valueOf(citiesId.get(position));
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        updateDataUserFragmentSpinCity.setSelection(returnIcCity);
                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
                        dismissProgressDialog();


                    }
                });
    }


    @OnClick({R.id.update_data_user_fragment_txt_birth_date, R.id.update_data_user_fragment_txt_last_don_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_data_user_fragment_txt_birth_date:
                setDateCalendar(updateDataUserFragmentTxtBirthDate);
                break;
            case R.id.update_data_user_fragment_txt_last_don_date:
                setDateCalendar(updateDataUserFragmentTxtLastDonDate);
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}