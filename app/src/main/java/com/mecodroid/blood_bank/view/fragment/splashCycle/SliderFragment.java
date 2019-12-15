package com.mecodroid.blood_bank.view.fragment.splashCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.sliderAdapter.SliderPagerAdapter;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.activity.UserActivity;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mecodroid.blood_bank.adapter.sliderAdapter.SliderPagerAdapter.imagesList;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.REMEMBER_USER;
import static com.mecodroid.blood_bank.helper.HelperMethod.isRTL;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadBoolean;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFragment extends BaseFragment {


    @BindView(R.id.slider_fragment_view_pager)
    ViewPager viewPager;
    @BindView(R.id.slider_fragment_lin_layout_Dots)
    LinearLayout layoutDots;
    @BindView(R.id.slider_fragment_btn_next)
    Button btnNext;
    @BindView(R.id.slider_fragment_btn_skip)
    Button btnSkip;
    @BindView(R.id.slider_fragment_container)
    RelativeLayout sliderFragmentContainer;
    Unbinder unbinder;
    int[] colorsActive, colorsInactive;
    private SliderPagerAdapter mySliderPagerAdapter;
    private TextView[] dots;

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == imagesList.size() - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sliderFragment = inflater.inflate(R.layout.fragment_slider, container, false);
        unbinder = ButterKnife.bind(this, sliderFragment);
        StatusBarUtil.setTransparent(getActivity());

        setupViewPager();
        // adding bottom dots
        addBottomDots(0);
        return sliderFragment;
    }

    private void setupViewPager() {
        mySliderPagerAdapter = new SliderPagerAdapter(getActivity());
        if (isRTL()) {
            // The view has RTL layout
            viewPager.setRotationY(180);
            layoutDots.setRotationY(180);
        }
        viewPager.setAdapter(mySliderPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[imagesList.size()];
        if (isRTL()) {
            colorsActive = getResources().getIntArray(R.array.array_dots_1);
            colorsInactive = getResources().getIntArray(R.array.array_dots_2);

        } else {
            colorsActive = getResources().getIntArray(R.array.array_dots_2);
            colorsInactive = getResources().getIntArray(R.array.array_dots_1);
        }

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            // draw circle via html code
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
            // set color for inactive page
            dots[i].setTextColor(colorsInactive[currentPage]);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            // set color for active page
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.slider_fragment_btn_next, R.id.slider_fragment_btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.slider_fragment_btn_next:
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < imagesList.size()) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    checkUserLogin();
                }
                break;
            case R.id.slider_fragment_btn_skip:
                checkUserLogin();
                break;
        }
    }

    private void checkUserLogin() {
        if (LoadStringData(getActivity(), API_TOKEN) != null &&
                LoadBoolean(getActivity(), REMEMBER_USER)) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        } else {
            startActivity(new Intent(getActivity(), UserActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}
