package com.mecodroid.blood_bank.adapter.donationAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationData;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests.DonationRequestContentFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.CALL;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.callPermissions;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.isRTL;

public class RecyclerDonationHomeAdapter extends RecyclerView.Adapter<RecyclerDonationHomeAdapter.ViewHolder> {

    ArrayList<DonationData> donationRequestArrayList;
    ViewHolder viewHolder;
    Context context;
    ApiServer apiServer;

    public RecyclerDonationHomeAdapter(Context context, ArrayList<DonationData> donationRequestArrayList) {
        this.donationRequestArrayList = donationRequestArrayList;
        this.context = context;
        apiServer = getClient().create(ApiServer.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item_row_donation_requests_adapter, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // check Language
        if (isRTL()) {
            viewHolder.donationRequestAdapterRl.setBackground(context.getResources().getDrawable(R.drawable.shapedonationrequest_rt));

        } else {
            viewHolder.donationRequestAdapterRl.setBackground(context.getResources().getDrawable(R.drawable.shapedonationrequest_lt));
        }
        viewHolder.donationRequestAdapterTxtPatientName.setText(donationRequestArrayList.get(position).getPatientName());
        viewHolder.donationRequestAdapterTxtHospital.setText(donationRequestArrayList.get(position).getHospitalName());
        viewHolder.donationRequestAdapterTxtCity.setText(donationRequestArrayList.get(position).getCityModel().getName());
        viewHolder.donationRequestAdapterTxtBloodType.setText(donationRequestArrayList.get(position).getBloodType().getName());
        viewHolder.donationRequestAdapterBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + donationRequestArrayList.get(position).getPhone()));
                        context.startActivity(callIntent);

                    } else {
                        callPermissions((Activity)context, CALL);
                    }

                } catch (Exception e) {
                    customMassageError((Activity) context, e.getMessage());
                }
            }
        });


        viewHolder.donationRequestAdapterBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonationRequestContentFragment donationRequestContentFragment = new DonationRequestContentFragment();

                //pass object from data to retrive data in Articles Content Fragment
                donationRequestContentFragment.donationRequest = donationRequestArrayList.get(position);

                ReplaceFragment(((FragmentActivity) v.getContext()).getSupportFragmentManager(),
                        donationRequestContentFragment, R.id.content_home_replace
                        , null, null);

            }
        });


    }

    @Override
    public int getItemCount() {
        return donationRequestArrayList.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.donation_request_adapter_btn_details)
        Button donationRequestAdapterBtnDetails;
        @BindView(R.id.donation_request_adapter_btn_call)
        Button donationRequestAdapterBtnCall;
        @BindView(R.id.donation_request_adapter_txt_patient_name)
        TextView donationRequestAdapterTxtPatientName;
        @BindView(R.id.donation_request_adapter_txt_hospital)
        TextView donationRequestAdapterTxtHospital;
        @BindView(R.id.donation_request_adapter_txt_city)
        TextView donationRequestAdapterTxtCity;
        @BindView(R.id.donation_request_adapter_txt_blood_type)
        TextView donationRequestAdapterTxtBloodType;
        @BindView(R.id.donation_request_adapter_rl)
        RelativeLayout donationRequestAdapterRl;
        View view;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
