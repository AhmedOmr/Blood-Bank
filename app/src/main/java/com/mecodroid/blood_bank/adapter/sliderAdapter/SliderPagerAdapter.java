package com.mecodroid.blood_bank.adapter.sliderAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mecodroid.blood_bank.R;

import java.util.ArrayList;
import java.util.List;

import static com.mecodroid.blood_bank.helper.HelperMethod.isRTL;

public class SliderPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    public static List<Integer> imagesList = new ArrayList<>();

    public SliderPagerAdapter(Context context) {
        this.context = context;

        imagesList.add(R.drawable.s1_480_800);
        imagesList.add(R.drawable.s2_480_800);
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = layoutInflater.inflate(R.layout.slider_images_layout, container, false);
            ImageView imageView = itemView.findViewById(R.id.imageslider1);
            imageView.setImageResource(imagesList.get(position));

            // use it to adapt the direction of image because it reverse in view pager when it arabic
            if (isRTL()) {
                // The view has RTL
                imageView.setRotationY(180);
                }

            container.addView(itemView);

            return itemView;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}
