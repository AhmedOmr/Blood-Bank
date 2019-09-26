package com.mecodroid.blood_bank.view.fragment.HomeCycle.articles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.model.posts.PostData;
import com.mecodroid.blood_bank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.onLoadImageFromUrl;

public class ArticlesDetailsFragment extends BaseFragment {

    @BindView(R.id.articles_content_fragment_img_post)
    ImageView articlesContentShowImg;
    @BindView(R.id.articlesContentTitleTxt)
    TextView articlesContentTitleTxt;
    @BindView(R.id.articlesContentFavoriteImg)
    ImageView articlesContentFavoriteImg;
    @BindView(R.id.articlesContentTxt)
    TextView articlesContentTxt;
    Unbinder unbinder;
    public PostData post;
    View view;
    private String getTitle, getContent, getThumbnailFullPath;
    private boolean getIsFavourite;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        view = inflater.inflate(R.layout.fragment_articles_details, container, false);

        unbinder = ButterKnife.bind(this, view);
        // add value tool bar
        homeActivity.setTitle(post.getCategory().getName());
        getDataReturnDetails();
        return view;
    }

    private void getDataReturnDetails() {
        // get data Donation Requests Adapter Recycler
        try {
            getTitle = post.getTitle();
            getContent = post.getContent();
            getThumbnailFullPath = post.getThumbnailFullPath();
            getIsFavourite = post.getIsFavourite();

            articlesContentTitleTxt.setText(getTitle);
            articlesContentTxt.setText(getContent);
            // if is check once or tow
            if (getIsFavourite) {
                // add icon favourite
                articlesContentFavoriteImg.setImageResource(R.drawable.fav_1);
            } else {
                // add icon un favourite
                articlesContentFavoriteImg.setImageResource(R.drawable.unfav_1);
            }
            onLoadImageFromUrl(articlesContentShowImg, getThumbnailFullPath, getActivity(), 0);

        } catch (Exception e) {
            customMassageError(getActivity(), "No data saved : " + e.getMessage());
        }
          }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
