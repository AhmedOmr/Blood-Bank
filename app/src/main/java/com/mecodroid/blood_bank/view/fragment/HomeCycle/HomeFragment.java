package com.mecodroid.blood_bank.view.fragment.HomeCycle;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.viewPagerAdapter.ViewPageAdapter;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.articles.ArticlesFragment;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests.CreateDonationRequestFragment;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests.DonationRequestsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    public boolean backFromFavourites = false;
    @BindView(R.id.home_fragment_tab)
    TabLayout homeFragmentTab;
    @BindView(R.id.home_fragment_viewpager)
    ViewPager homeFragmentViewpager;
    Unbinder unbinder;
    @BindView(R.id.ArticlesAndRequests_FloatingButton)
    FloatingActionButton ArticlesAndRequestsFloatingButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeActivity.setTitle(getString(R.string.home));

        setUpViewPager();

        initFragment();

        return view;
    }

    private void setUpViewPager() {
        ViewPageAdapter vpadapter = new ViewPageAdapter(getChildFragmentManager());

        ArticlesFragment articlesFragment = new ArticlesFragment();
        articlesFragment.backFromFavourites = backFromFavourites;
        backFromFavourites = false;
        DonationRequestsFragment donationRequestsFragment = new DonationRequestsFragment();

        vpadapter.addPager(articlesFragment, getString(R.string.articles));
        vpadapter.addPager(donationRequestsFragment, getString(R.string.donation_req));

        homeFragmentViewpager.setAdapter(vpadapter);
        homeFragmentTab.setupWithViewPager(homeFragmentViewpager);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ArticlesAndRequests_FloatingButton)
    public void onViewClicked() {
        CreateDonationRequestFragment createDonationRequestsFragment = new CreateDonationRequestFragment();
        ReplaceFragment(getActivity().getSupportFragmentManager(), createDonationRequestsFragment,
                R.id.content_home_replace, null, null);

    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}