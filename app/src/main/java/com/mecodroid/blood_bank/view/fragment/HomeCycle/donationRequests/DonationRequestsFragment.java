package com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.donationAdapter.RecyclerDonationHomeAdapter;
import com.mecodroid.blood_bank.adapter.onEndless.OnEndless;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationData;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationRequests;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;

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
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;


public class DonationRequestsFragment extends Fragment {

    @BindView(R.id.donation_requests_fragment_img_btn_search)
    ImageView donationRequestsFragmentImgBtnSearch;
    @BindView(R.id.donation_requests_fragment_spin_city)
    Spinner donationRequestsFragmentSpinCity;
    @BindView(R.id.donation_requests_fragment_spin_blood_type)
    Spinner donationRequestsFragmentSpinBloodType;
    @BindView(R.id.articles_home_adapter_card_view_2)
    CardView cardView;
    @BindView(R.id.donation_requests_fragment_lin1)
    LinearLayout donationRequestsFragmentLin1;
    @BindView(R.id.donation_requests_fragment_recycler_view)
    RecyclerView donationRequestsFragmentRecyclerView;

    Unbinder unbinder;
    ApiServer apiServer;
    private Integer govern_id;
    private ArrayList<DonationData> donationRequestArrayList;
    private RecyclerDonationHomeAdapter donationAdapterRecycler;
    private OnEndless onEndless;
    private Integer maxPage = 0;
    private boolean checkFilterPost = true;
    private int previousPage = 1;
    private Integer blood_type_id;
    private String api_token;


    public DonationRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_requests, container, false);
        unbinder = ButterKnife.bind(this, view);
        disappearKeypad(getActivity(), donationRequestsFragmentImgBtnSearch);
        api_token = LoadStringData(getActivity(), API_TOKEN);
        inti();
        getAllGovernorate();
        getAllBloodTypes();
        onEndless();

        OnClickAllTools();
        return view;
    }

    // initialize tools
    private void inti() {
        apiServer = getClient().create(ApiServer.class);
        donationRequestArrayList = new ArrayList<>();

    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        donationRequestsFragmentRecyclerView.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                showProgressDialog(getActivity(), "Loading");
                if (maxPage != 0) {
                    getdonationRequests(current_page);
                }
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        previousPage = current_page;
                        getdonationRequests(current_page);

                    } else {
                       dismissProgressDialog();
                        onEndless.current_page = previousPage;
                    }
                } else {
                   dismissProgressDialog();
                    onEndless.current_page = previousPage;
                }
            }
        };
        dismissProgressDialog();

        donationRequestsFragmentRecyclerView.addOnScrollListener(onEndless);
        donationAdapterRecycler = new RecyclerDonationHomeAdapter(donationRequestArrayList, getActivity());
        donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);

        getdonationRequests(1);
        previousPage = 1;
        onEndless.current_page = 1;
        onEndless.previousTotal = 0;

    }

    // this is method all in click
    private void OnClickAllTools() {
        //  search keyword
        donationRequestsFragmentImgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (donationRequestsFragmentSpinCity.getSelectedItemPosition() == 0 &&
                        donationRequestsFragmentSpinBloodType.getSelectedItemPosition() == 0 &&
                        !checkFilterPost) {

                    donationAdapterRecycler = new RecyclerDonationHomeAdapter(donationRequestArrayList, getActivity());
                    donationAdapterRecycler.notifyDataSetChanged();
                    donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);
                    checkFilterPost = true;
                    donationAdapterRecycler.notifyDataSetChanged();
                } else {
                    donationAdapterRecycler = new RecyclerDonationHomeAdapter(donationRequestArrayList, getActivity());
                    donationAdapterRecycler.notifyDataSetChanged();
                    donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);
                    checkFilterPost = false;
                    donationAdapterRecycler.notifyDataSetChanged();
                    dismissProgressDialog();
                    geDonationRequestsFilter(1);

                }

            }
        });

    }

    // get all  donation
    private void getdonationRequests(final int page) {
        try {
            // get  PostsPagination  post
            apiServer.getDonationRequests(LoadStringData(getActivity(), API_TOKEN), page)
                    .enqueue(new Callback<DonationRequests>() {
                        @Override
                        public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                            dismissProgressDialog();

                            if (response.body().getStatus() == 1) {

                                maxPage = response.body().getDonationPagination().getLastPage();

                                donationRequestArrayList.addAll(response.body().getDonationPagination().getDonationData());

                                donationAdapterRecycler.notifyDataSetChanged();


                            } else {


                                Toast.makeText(getContext(), "Not Donation Pagination ", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<DonationRequests> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.getMessage();
        }
    }


    // get data Governorate And Blood Type Spinner
    public void getAllBloodTypes() {
        showProgressDialog(getActivity(), "Loading");

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
                typeBlood.add(getString(R.string.allblood_type).trim());
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
                        android.R.layout.simple_spinner_item, typeBlood);
                // to specify form of spinner
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                // bind spinner with adapter
                donationRequestsFragmentSpinBloodType.setAdapter(adapter);
                // interaction with spinner
                donationRequestsFragmentSpinBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // return the item selected from spinner else postion equal zero (title)
                        blood_type_id = idBlood.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<BloodTypes> call, Throwable t) {

            }
        });

    }

    public void getAllGovernorate() {
        apiServer.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                List<GeneralModel> governoratesDatumList = response.body().getData();
                ArrayList<String> governorat = new ArrayList<>();
                final ArrayList<Integer> idGovern = new ArrayList<>();
                governorat.add(getString(R.string.all_governorate));
                idGovern.add(0);

                for (int i = 0; i < governoratesDatumList.size(); i++) {
                    String governorateName = governoratesDatumList.get(i).getName();
                    Integer governoratId = governoratesDatumList.get(i).getId();
                    governorat.add(governorateName);
                    idGovern.add(governoratId);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, governorat);

                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                donationRequestsFragmentSpinCity.setAdapter(adapter);

                donationRequestsFragmentSpinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        govern_id = idGovern.get(position);
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


    //  Donation Requests Filter
    private void geDonationRequestsFilter(int current_page) {
        showProgressDialog(getActivity(), "Loading");
        apiServer.getDonationRequestsFilter(api_token, blood_type_id, govern_id, current_page)
                .enqueue(new Callback<DonationRequests>() {
                    @Override
                    public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                        dismissProgressDialog();
                        try {
                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.body().getStatus() == 1) {

                                if (response.body().getDonationPagination().getTotal() == 0) {
                                    Toast.makeText(getContext(), "Not Pagination DataNotifyPage", Toast.LENGTH_SHORT).show();
                                }
                                // Clear all data list
                                donationRequestArrayList.clear();
                                // notify DataNotifyPage Set Changed
                                donationAdapterRecycler.notifyDataSetChanged();
                                // add All
                                donationRequestArrayList.addAll(response.body().getDonationPagination().getDonationData());
                                // notify DataNotifyPage Set Changed
                                donationAdapterRecycler.notifyDataSetChanged();

                            } else {

                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {

                            e.getMessage();
                        }


                    }

                    @Override
                    public void onFailure(Call<DonationRequests> call, Throwable t) {
                        dismissProgressDialog();
                    }
                });

    }


    @OnClick(R.id.donation_requests_fragment_img_btn_search)
    public void onViewClicked() {
        OnClickAllTools();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
