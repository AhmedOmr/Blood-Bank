package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.notificationRecyclerAdapter.NotificationAdapterRecycler;
import com.mecodroid.blood_bank.adapter.onEndless.OnEndless;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.notifications.DataNotify;
import com.mecodroid.blood_bank.data.model.notifications.Notifications;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isNetworkConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class NotificationsFragment extends BaseFragment {

    @BindView(R.id.notificationsFragmentShowPostRecyclerView)
    RecyclerView notificationsFragmentShowPostRecyclerView;
    @BindView(R.id.notification_fragment_tv_no_results)
    TextView notificationFragmentTvNoResults;
    Unbinder unbinder;

    private NotificationAdapterRecycler notificationAdapterRecycler;

    private ApiServer apiServer;
    private View view;
    private ArrayList<DataNotify> notificationsArrayList;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndless onEndLess;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        initTools();

        if (notificationsArrayList.size() == 0) {
            getNotificationList(1);
        }
        return view;

    }
    // initialize tools
    private void initTools() {
        notificationsArrayList = new ArrayList<>();
        apiServer = getClient().create(ApiServer.class);
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.notification));

        linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationsFragmentShowPostRecyclerView.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getNotificationList(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }

            }
        };

        notificationsFragmentShowPostRecyclerView.addOnScrollListener(onEndLess);
        notificationAdapterRecycler = new NotificationAdapterRecycler(getActivity(), notificationsArrayList);
        notificationsFragmentShowPostRecyclerView.setAdapter(notificationAdapterRecycler);

    }

    private void getNotificationList(final int page) {
        boolean check_network = isNetworkConnected(getActivity(), getView());
        if (check_network == false) {
            dismissProgressDialog();
            return;
        }
        showProgressDialog(getActivity(), getString(R.string.waiit));
        apiServer.getNotificationsList(LoadStringData(getActivity(), API_TOKEN), page)
                .enqueue(new Callback<Notifications>() {
                    @Override
                    public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                        try {
                            dismissProgressDialog();

                            if (response.body().getStatus() == 1) {
                                if (page == 1) {
                                    if (response.body().getDataNotifyPage().getTotal() > 0) {
                                        notificationFragmentTvNoResults.setVisibility(View.GONE);

                                    } else {
                                        notificationFragmentTvNoResults.setVisibility(View.VISIBLE);

                                    }

                                    onEndLess.current_page = 1;
                                    onEndLess.previousTotal = 0;
                                    onEndLess.previous_page = 1;

                                    max = 0;

                                    max = response.body().getDataNotifyPage().getLastPage();
                                    notificationsArrayList.addAll(response.body().getDataNotifyPage().getData());
                                    notificationAdapterRecycler.notifyDataSetChanged();

                                }


                            } else {
                                customMassageError(getActivity(), response.body().getMsg());

                            }
                        } catch (Exception e) {
                            customMassageError(getActivity(), e.getMessage());

                        }


                    }

                    @Override
                    public void onFailure(Call<Notifications> call, Throwable t) {
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
        setUpHomeActivity();
       ReplaceFragment(getActivity().getSupportFragmentManager(), homeActivity.homeFragment,
               R.id.content_home_replace, null, null);
    }
}
