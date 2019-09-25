package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.gridViewAdapter.AdapterGridView;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;
import com.mecodroid.blood_bank.data.model.notificationsSettings.NotificationsSettings;
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
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class SettingNotificationFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.setting_notification_fragment_grid_view_blood_type)
    GridView settingNotificationFragmentBloodTypeGrid;
    @BindView(R.id.setting_notification_fragment_grid_view_gov)
    GridView settingNotificationFragmentGovernortesGrid;
    @BindView(R.id.setting_notification_fragment_btn_save)
    Button settingNotificationFragmentSaveBtn;


    private View view;
    private ApiServer apiServer;
    // var adapter grid view governortares
    private AdapterGridView adapterGovernortGridView;
    private AdapterGridView adapterBloodTypeGridView;
    private List<GeneralModel> governortareGeneratedModelArrayList = new ArrayList<>();

    // var adapter grid view  blood Type
    private List<GeneralModel> bloodTypeGeneratedModelArrayList = new ArrayList<>();
    private List<Integer> idBloodType;
    private List<Integer> idGovernorates;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        setUpHomeActivity();
        // initializer tools
        inti();

        getDataBloodTypeAndGovernorates();

        return view;
    }

    // initializer tools
    private void inti() {

        apiServer = getClient().create(ApiServer.class);
        governortareGeneratedModelArrayList = new ArrayList<>();

        idBloodType = new ArrayList<>();
        idGovernorates = new ArrayList<>();
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.notification_setting));

    }

    //get DataNotifyPage and Governorates
    public void getDataBloodTypeAndGovernorates() {
        // get  PaginationData governorates
        showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        apiServer.getNotificationsSettings(LoadStringData(getActivity(), API_TOKEN))
                .enqueue(new Callback<NotificationsSettings>() {
                    @Override
                    public void onResponse(Call<NotificationsSettings> call, Response<NotificationsSettings> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                dismissProgressDialog();
                                for (int i = 0; i < response.body().getNotificationsSettingsData().getBloodTypes().size(); i++) {
                                    idBloodType.add(Integer.valueOf(response.body().getNotificationsSettingsData().getBloodTypes().get(i)));
                                }
                                for (int i = 0; i < response.body().getNotificationsSettingsData().getGovernorates().size(); i++) {
                                    idGovernorates.add(Integer.valueOf(response.body().getNotificationsSettingsData().getGovernorates().get(i)));
                                }
                                // get data Blood Types
                                BloodTypes();
                                // get data Governorate
                                Governorate();
                                dismissProgressDialog();
                            }

                        } catch (Exception e) {
                            dismissProgressDialog();
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationsSettings> call, Throwable t) {
                        dismissProgressDialog();
                    }
                });

    }

    // get data Blood_types
    private void BloodTypes() {
        //showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        // get data Blood_types
        apiServer.getBloodTypes().enqueue(new Callback<BloodTypes>() {
            @Override
            public void onResponse(Call<BloodTypes> call, Response<BloodTypes> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {

                        bloodTypeGeneratedModelArrayList.addAll(response.body().getData());
                        adapterBloodTypeGridView = new AdapterGridView(getActivity(), bloodTypeGeneratedModelArrayList, idBloodType);
                        settingNotificationFragmentBloodTypeGrid.setAdapter(adapterBloodTypeGridView);
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<BloodTypes> call, Throwable t) {
                dismissProgressDialog();
            }
        });

    }

    // get data governorates
    private void Governorate() {
        //showProgressDialog(getActivity(), getResources().getString(R.string.loading));
        // get data governorates
        apiServer.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {

                        governortareGeneratedModelArrayList.addAll(response.body().getData());
                        adapterGovernortGridView = new AdapterGridView(getActivity(), governortareGeneratedModelArrayList, idGovernorates);
                        settingNotificationFragmentGovernortesGrid.setAdapter(adapterGovernortGridView);
                    }

                } catch (Exception e) {
                    dismissProgressDialog();
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    @OnClick(R.id.setting_notification_fragment_btn_save)
    public void onViewClicked() {
        //showProgressDialog(getActivity(), getResources().getString(R.string.loading));

        // get data governorates
        apiServer.ChangeNotificationsSettings(LoadStringData(getActivity(), API_TOKEN)
                , adapterGovernortGridView.numCheck, adapterBloodTypeGridView.numCheck)
                .enqueue(new Callback<NotificationsSettings>() {
                    @Override
                    public void onResponse(Call<NotificationsSettings> call, Response<NotificationsSettings> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                customMassageDone(getActivity(), response.body().getMsg());
                            } else {
                                dismissProgressDialog();
                                customMassageError(getActivity(), response.body().getMsg());
                            }
                        } catch (Exception e) {
                            dismissProgressDialog();
                            e.getMessage();
                        }

                    }

                    @Override
                    public void onFailure(Call<NotificationsSettings> call, Throwable t) {
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onBack() {
        setUpHomeActivity();
        ReplaceFragment(getActivity().getSupportFragmentManager(), homeActivity.homeFragment
                , R.id.content_home_replace, null, null);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
