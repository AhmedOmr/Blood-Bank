package com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.CityDataModel;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.donationRequestCreate.DonationRequestsCreate;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;
import com.mecodroid.blood_bank.view.activity.map.MapsActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import java.util.ArrayList;
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
import static com.mecodroid.blood_bank.helper.HelperMethod.createSnackBar;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isNetworkConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;
import static com.mecodroid.blood_bank.view.activity.map.MapsActivity.hospital_address;
import static com.mecodroid.blood_bank.view.activity.map.MapsActivity.latitude;
import static com.mecodroid.blood_bank.view.activity.map.MapsActivity.longitude;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDonationRequestFragment extends BaseFragment {


    @BindView(R.id.create_donation_requests_fragment_et_name)
    EditText createDonationRequestsFragmentEtName;
    @BindView(R.id.create_donation_requests_fragment_et_age)
    EditText createDonationRequestsFragmentEtAge;
    @BindView(R.id.create_donation_requests_fragment_sp_blood_type)
    Spinner createDonationRequestsFragmentSpBloodType;
    @BindView(R.id.create_donation_requests_fragment_et_numbers)
    EditText createDonationRequestsFragmentEtNumbers;
    @BindView(R.id.create_donation_requests_fragment_et_hospital_name)
    EditText createDonationRequestsFragmentEtHospitalName;
    @BindView(R.id.create_donation_requests_fragment_sp_government)
    Spinner createDonationRequestsFragmentSpGovernment;
    @BindView(R.id.create_donation_requests_fragment_sp_city)
    Spinner createDonationRequestsFragmentSpCity;
    @BindView(R.id.create_donation_requests_fragment_et_hospital_address)
    EditText createDonationRequestsFragmentEtHospitalAddress;
    @BindView(R.id.create_donation_requests_fragment_et_phone)
    EditText createDonationRequestsFragmentEtPhone;
    @BindView(R.id.create_donation_requests_fragment_et_notes)
    EditText createDonationRequestsFragmentEtNotes;
    Unbinder unbinder;
    @BindView(R.id.create_donation_requests_fragment_Relative_City)
    RelativeLayout createDonationRequestsFragmentRelativeCity;

    private ApiServer apiServer;
    private String blood_type_id;
    private String startCityId;

    public CreateDonationRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_donation_requests, container, false);
        unbinder = ButterKnife.bind(this, view);

        setUpHomeActivity();

        apiServer = getClient().create(ApiServer.class);

        getAllGovernorate();
        getAllBloodTypes();

        createDonationRequestsFragmentEtHospitalAddress.setText("");

        return view;

    }
    // get all blood Types
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
                createDonationRequestsFragmentSpBloodType.setAdapter(adapter);
                // interaction with spinner
                createDonationRequestsFragmentSpBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                createDonationRequestsFragmentSpGovernment.setAdapter(adapter);

                createDonationRequestsFragmentSpGovernment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }

        });
    }

    // get all city
    private void getAllCity(Integer gavernoratesId) {
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

                        createDonationRequestsFragmentSpCity.setAdapter(adapter);

                        createDonationRequestsFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
                        dismissProgressDialog();


                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.create_donation_requests_fragment_btn_create_request,
            R.id.create_donation_requests_fragment_Iv_open_map, R.id.create_donation_requests_fragment_rl_sub_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_donation_requests_fragment_btn_create_request:
                sendRequest();
                break;
            case R.id.create_donation_requests_fragment_Iv_open_map:

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

                break;
            case R.id.create_donation_requests_fragment_rl_sub_view:
                disappearKeypad(getActivity(), view);
                break;

        }
    }

    private void sendRequest() {
        String patient_name = createDonationRequestsFragmentEtName.getText().toString().trim();
        String patient_age = createDonationRequestsFragmentEtAge.getText().toString().trim();
        String bags_num = createDonationRequestsFragmentEtNumbers.getText().toString().trim();
        String hospital_name = createDonationRequestsFragmentEtHospitalName.getText().toString().trim();
        String phone = createDonationRequestsFragmentEtPhone.getText().toString().trim();
        String notes = createDonationRequestsFragmentEtNotes.getText().toString().trim();
        String hospitalAddress = createDonationRequestsFragmentEtHospitalAddress.getText().toString().trim();

        if (hospitalAddress.isEmpty()) {
            createDonationRequestsFragmentEtHospitalAddress.setText(hospital_address);
            createDonationRequestsFragmentEtHospitalAddress.requestFocus();

        }
        if (patient_name.isEmpty()) {
            createDonationRequestsFragmentEtName.requestFocus();

            customMassageError(getActivity(), getResources().getString(R.string.enterPatientName));
            return;
        }
        if (patient_age.isEmpty()) {
            createDonationRequestsFragmentEtAge.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.enterPatientAge));
            return;
        }
        if (blood_type_id.equalsIgnoreCase(null)) {
            customMassageError(getActivity(), getResources().getString(R.string.select_blood));

            return;
        }
        if (bags_num.isEmpty()) {
            createDonationRequestsFragmentEtNumbers.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.enterNumBags));


            return;
        }
        if (hospital_name.isEmpty()) {
            createDonationRequestsFragmentEtHospitalName.requestFocus();

            customMassageError(getActivity(), getResources().getString(R.string.enterHospitalName));
            return;
        }

        if (createDonationRequestsFragmentSpGovernment.getSelectedItemPosition() == 0) {
            customMassageError(getActivity(), getResources().getString(R.string.select_govern));
            return;
        }
        if (startCityId.equalsIgnoreCase(null)) {
            customMassageError(getActivity(), getResources().getString(R.string.select_city));
            return;
        }

        if (hospitalAddress.isEmpty()) {
            customMassageError(getActivity(), getResources().getString(R.string.enterHospitalAddress));

        }

        if (phone.isEmpty()) {
            createDonationRequestsFragmentEtPhone.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.enterPhone));
            return;
        }
        if (phone.length() != 11) {
            createDonationRequestsFragmentEtPhone.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.phoneLength));
            return;
        }

        if (notes.isEmpty()) {

            createDonationRequestsFragmentEtNotes.requestFocus();
            customMassageError(getActivity(), getResources().getString(R.string.enterNotes));
            return;
        }
        boolean check_network = isNetworkConnected(getActivity(), getView());
        if (check_network == true) {

            showProgressDialog(getActivity(), getResources().getString(R.string.loading));
            apiServer.CreateDonationRequests(LoadStringData(getActivity(), API_TOKEN),
                    patient_name, patient_age, blood_type_id, bags_num, hospital_name,
                    hospitalAddress, startCityId, phone, notes, latitude, longitude).
                    enqueue(new Callback<DonationRequestsCreate>() {
                @Override
                public void onResponse(Call<DonationRequestsCreate> call, Response<DonationRequestsCreate> response) {
                    dismissProgressDialog();

                    try {
                        if (response.body().getStatus() == 1) {
                            customMassageDone(getActivity(), response.body().getMsg());


                        } else {
                            dismissProgressDialog();
                            customMassageError(getActivity(), response.body().getMsg());
                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<DonationRequestsCreate> call, Throwable t) {
                    dismissProgressDialog();

                    customMassageError(getActivity(), t.getMessage());
                }
            });


        } else {
            dismissProgressDialog();
            customMassageError(getActivity(), getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        createDonationRequestsFragmentEtHospitalAddress.setText(hospital_address);
    }

}

