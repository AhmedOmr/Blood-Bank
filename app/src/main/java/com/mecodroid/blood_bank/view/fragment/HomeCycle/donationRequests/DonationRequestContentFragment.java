package com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationData;
import com.mecodroid.blood_bank.view.activity.map.DontaionRequestContentMapActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mecodroid.blood_bank.helper.BloodBankConatants.CALL;
import static com.mecodroid.blood_bank.helper.HelperMethod.callPermissions;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonationRequestContentFragment extends BaseFragment implements OnMapReadyCallback {

    public DonationData donationRequest;
    public boolean fromDonation;
    @BindView(R.id.donation_request_content_fragment_txt_name)
    TextView donationRequestContentFragmentTxtName;
    @BindView(R.id.donation_request_content_fragment_txt_age)
    TextView donationRequestContentFragmentTxtAge;
    @BindView(R.id.donation_request_content_fragment_txt_blood_type)
    TextView donationRequestContentFragmentTxtBloodType;
    @BindView(R.id.donation_request_content_fragment_txt_num_bloods_bags)
    TextView donationRequestContentFragmentTxt_Num_Bloods_Bags;
    @BindView(R.id.donation_request_content_fragment_txt_hospital_name)
    TextView donationRequestContentFragmentTxt_Hospital_Name;
    @BindView(R.id.donation_request_content_fragment_txt_hospital_address)
    TextView donationRequestContentFragmentTxtHospital_Address;
    @BindView(R.id.donation_request_content_fragment_txt_phone_number)
    TextView donationRequestContentFragmentTxtPhoneNumber;
    @BindView(R.id.donation_request_content_fragment_txt_datails)
    TextView donationRequestContentFragmentTxtDetails;
    @BindView(R.id.fragmentContentDonationMapBtn)
    ImageView fragmentContentDonationMapBtn;
    @BindView(R.id.donation_request_content_fragment_btn_call)
    Button donationRequestContentFragmentBtnCall;
    @BindView(R.id.donation_request_content_fragment_map)
    MapView donationRequestContentFragmentMap;

    Unbinder unbinder;
    private String patientName;
    private String phone;
    private String notes;
    private String bagsNum;
    private String bloodType;
    private String cityId;
    private static double getlatitude,getlongitude;
    private String hospitalName;
    private String patientAge;
    private String hospitalAddress;


    private GoogleMap map;

    private LatLng frLatLng;
    private double latitude,longitude;

    public DonationRequestContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_request_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.dont_req)+
                " " + donationRequest.getPatientName());

        getDataReturnDetails();
        donationRequestContentFragmentMap.onCreate(savedInstanceState);
        // map view
        MapViews();
        return view;
    }

    private void getDataReturnDetails() {
        // get data Donation Requests Adapter Recycler
        try {
            patientName = donationRequest.getPatientName();
            phone = donationRequest.getPhone();
            notes = donationRequest.getNotes();
            bagsNum = donationRequest.getBagsNum();
            bloodType = donationRequest.getBloodType().getName();
            cityId = donationRequest.getCityModel().getName();
            getlatitude = Double.parseDouble(donationRequest.getLatitude());
            getlongitude = Double.parseDouble(donationRequest.getLongitude());
            hospitalName = donationRequest.getHospitalName();
            patientAge = donationRequest.getPatientAge();
            hospitalAddress = donationRequest.getHospitalAddress();

            donationRequestContentFragmentTxtAge.setText(patientAge);
            donationRequestContentFragmentTxtDetails.setText(notes);
            donationRequestContentFragmentTxtHospital_Address.setText(hospitalAddress);
            donationRequestContentFragmentTxtName.setText(patientName);
            donationRequestContentFragmentTxt_Num_Bloods_Bags.setText(bagsNum);
            donationRequestContentFragmentTxtPhoneNumber.setText(phone);
            donationRequestContentFragmentTxtBloodType.setText(bloodType);
            donationRequestContentFragmentTxt_Hospital_Name.setText(hospitalName);


        } catch (Exception e){
            customMassageError(getActivity(), e.getMessage());
        }



    }

    private void showMap() {
        customMassageDone(getActivity(), "map");
        Intent map = new Intent(getContext(), DontaionRequestContentMapActivity.class);
        map.putExtra("latitudePoint", getlatitude);
        map.putExtra("longitudePoint", getlongitude);
        startActivity(map);
    }

    private void MapViews() {
        // Create class object
        donationRequestContentFragmentMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // get lat and long
        frLatLng = new LatLng(getlatitude, getlongitude);
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.addMarker(new MarkerOptions().position(frLatLng)).setTitle(getString(R.string.my_location));
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(frLatLng, 20.0f));


    }

    // call permission
    private void makeACall() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);

            } else {
                callPermissions(getActivity(), CALL);
            }

        } catch (Exception e) {
            customMassageError(getActivity(), e.getMessage());
        }
    }

    @OnClick({R.id.fragmentContentDonationMapBtn,
            R.id.donation_request_content_fragment_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragmentContentDonationMapBtn:
                showMap();

                break;
            case R.id.donation_request_content_fragment_btn_call:
                makeACall();
                break;
        }
    }

    @Override
    public void onStart() {
        donationRequestContentFragmentMap.onResume();
        super.onStart();
    }

    @Override
    public void onResume() {
        donationRequestContentFragmentMap.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        donationRequestContentFragmentMap.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
