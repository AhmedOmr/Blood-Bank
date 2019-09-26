package com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.donationAdapter.RecyclerDonationHomeAdapter;
import com.mecodroid.blood_bank.adapter.onEndless.OnEndless;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationData;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationRequests;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;
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
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.setSpinner;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;


public class DonationRequestsFragment extends BaseFragment {

    // store blood type name
    final ArrayList<String> typeBlood = new ArrayList<>();
    // store blood type id
    final ArrayList<Integer> idBlood = new ArrayList<Integer>();
    // store govern name
    final ArrayList<String> governorat = new ArrayList<>();
    // store govern id
    final ArrayList<Integer> idGovern = new ArrayList<>();
    @BindView(R.id.donation_requests_fragment_img_btn_search)
    ImageView donationRequestsFragmentImgBtnSearch;
    @BindView(R.id.donation_requests_fragment_spin_city)
    Spinner donationRequestsFragmentSpinCity;
    @BindView(R.id.donation_requests_fragment_spin_blood_type)
    Spinner donationRequestsFragmentSpinBloodType;
    @BindView(R.id.donation_requests_fragment_lin1)
    LinearLayout donationRequestsFragmentLin1;
    @BindView(R.id.donation_requests_fragment_recycler_view)
    RecyclerView donationRequestsFragmentRecyclerView;
    Unbinder unbinder;
    ApiServer apiServer;
    @BindView(R.id.donation_requests_fragment_srl_donations_list_refresh)
    SwipeRefreshLayout donationRequestsFragmentSrlDonationsListRefresh;
    @BindView(R.id.donation_requests_fragment_txt_no_results)
    TextView donationRequestsFragmentTxtNoResults;
    private List<GeneralModel> bloodTypesData = new ArrayList<>();
    private List<GeneralModel> governoratesData = new ArrayList<>();
    private Integer govern_id;
    private ArrayList<DonationData> donationRequestArrayList;
    private RecyclerDonationHomeAdapter donationAdapterRecycler;
    private OnEndless onEndless;
    private Integer maxPage = 0;
    private Integer blood_type_id;
    private String api_token;
    private LinearLayoutManager linearLayoutManager;
    private boolean filterSearch = false;

    public DonationRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_requests, container, false);
        unbinder = ButterKnife.bind(this, view);

        inti();
        initRecyclerView();

        if (bloodTypesData.size() == 0) {
            getAllBloodTypes();
        } else {
            setSpinner(getActivity(), donationRequestsFragmentSpinBloodType, typeBlood);
        }

        if (governoratesData.size() == 0) {
            getAllGovernorate();
        } else {
            setSpinner(getActivity(), donationRequestsFragmentSpinCity, governorat);
        }

        donationRequestsFragmentSrlDonationsListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndless.current_page = 1;
                onEndless.previousTotal = 0;
                onEndless.previous_page = 1;

                maxPage = 0;

                donationRequestArrayList = new ArrayList<>();

                donationAdapterRecycler = new RecyclerDonationHomeAdapter(getActivity(), donationRequestArrayList);
                donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);

                if (filterSearch) {
                    getDonationFilter(1);
                } else {
                    getDonations(1);
                }
            }
        });

        if (donationRequestArrayList.size() == 0) {
            getDonations(1);
        }
        return view;
    }

    // initialize tools
    private void inti() {
        apiServer = getClient().create(ApiServer.class);
        donationRequestArrayList = new ArrayList<>();
        disappearKeypad(getActivity(), donationRequestsFragmentImgBtnSearch);
        api_token = LoadStringData(getActivity(), API_TOKEN);
        onClickImageSearch();
    }

    // get data Governorate And Blood Type Spinner
    public void getAllBloodTypes() {
        apiServer.getBloodTypes().enqueue(new Callback<BloodTypes>() {
            @Override
            public void onResponse(Call<BloodTypes> call, Response<BloodTypes> response) {
                dismissProgressDialog();
                bloodTypesData = new ArrayList<>();
                bloodTypesData = response.body().getData();
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
                        R.layout.spinner_layout2, typeBlood);
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
                dismissProgressDialog();
            }
        });

    }

    public void getAllGovernorate() {
        apiServer.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                try {
                    dismissProgressDialog();
                    governoratesData = new ArrayList<>();
                    governoratesData = response.body().getData();
                    governorat.add(getString(R.string.all_governorate));
                    idGovern.add(0);

                    for (int i = 0; i < governoratesData.size(); i++) {
                        String governorateName = governoratesData.get(i).getName();
                        Integer governoratId = governoratesData.get(i).getId();
                        governorat.add(governorateName);
                        idGovern.add(governoratId);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            R.layout.spinner_layout2, governorat);

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
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                dismissProgressDialog();


            }

        });
    }

    // listener from count items  recyclerView
    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        donationRequestsFragmentRecyclerView.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndless.previous_page = current_page;

                        if (filterSearch) {
                            getDonationFilter(current_page);
                        } else {
                            getDonations(current_page);
                        }

                    } else {
                        onEndless.current_page = onEndless.previous_page;
                    }
                } else {
                    onEndless.current_page = onEndless.previous_page;
                }
            }
        };
        donationRequestsFragmentRecyclerView.addOnScrollListener(onEndless);

        donationAdapterRecycler = new RecyclerDonationHomeAdapter(getActivity(), donationRequestArrayList);
        donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);
    }

    // this is method all in click
    private void onClickImageSearch() {
        //  search keyword
        donationRequestsFragmentImgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEndless.current_page = 1;
                onEndless.previous_page = 1;
                onEndless.previousTotal = 0;
                onEndless.totalItemCount = 0;
                maxPage = 0;

                donationRequestArrayList = new ArrayList<>();
                donationAdapterRecycler = new RecyclerDonationHomeAdapter(getActivity(), donationRequestArrayList);
                donationRequestsFragmentRecyclerView.setAdapter(donationAdapterRecycler);

                showProgressDialog(getActivity(), getString(R.string.waiit));
                if (donationRequestsFragmentSpinBloodType.getSelectedItemPosition() == 0
                        && donationRequestsFragmentSpinCity.getSelectedItemPosition() == 0) {

                    if (filterSearch) {
                        filterSearch = false;
                        getDonations(1);
                    }

                } else {
                    filterSearch = true;
                    getDonationFilter(1);
                }

            }
        });

    }

    @OnClick(R.id.donation_requests_fragment_img_btn_search)
    public void onViewClicked() {
        onClickImageSearch();
    }
    // get all  donation
    private void getDonations(int page) {
        if (isConnected(getActivity())) {
            callLoadData(page, apiServer.getDonationRequests(api_token, page));
        } else {
            dismissProgressDialog();
            customMassageError(getActivity(), getResources().getString(R.string.no_internet));
            donationRequestsFragmentSrlDonationsListRefresh.setRefreshing(false);
        }

    }

    //  get all  donation filter Search
    private void getDonationFilter(int page) {
        if (isConnected(getActivity())) {
            callLoadData(page, apiServer
                    .getDonationRequestsFilter(api_token, blood_type_id, govern_id, page));
        } else {
            dismissProgressDialog();
            customMassageError(getActivity(), getResources().getString(R.string.no_internet));
            donationRequestsFragmentSrlDonationsListRefresh.setRefreshing(false);
        }


    }

    // load all donation req
    private void callLoadData(int page, Call<DonationRequests> donationRequestsCall) {
        if (page > 1) {
            showProgressDialog(getActivity(), getString(R.string.waiit));
        }

        donationRequestsCall.enqueue(new Callback<DonationRequests>() {
            @Override
            public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                try {
                    dismissProgressDialog();
                    donationRequestsFragmentSrlDonationsListRefresh.setRefreshing(false);
                    if (response.body().getStatus() == 1) {
                        if (response.body().getDonationPagination().getTotal() > 0) {
                            donationRequestsFragmentTxtNoResults.setVisibility(View.GONE);
                        } else {
                            donationRequestsFragmentTxtNoResults.setVisibility(View.VISIBLE);

                        }
                        maxPage = response.body().getDonationPagination().getLastPage();
                        donationRequestArrayList.addAll(response.body().getDonationPagination().getDonationData());
                        donationAdapterRecycler.notifyDataSetChanged();

                    } else {
                        customMassageError(getActivity(), response.body().getMsg());
                    }
                } catch (Exception e) {
                    customMassageError(getActivity(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DonationRequests> call, Throwable t) {
                dismissProgressDialog();
                customMassageError(getActivity(), t.getMessage());

                try {
                    donationRequestsFragmentSrlDonationsListRefresh.setRefreshing(false);
                } catch (Exception e) {

                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
