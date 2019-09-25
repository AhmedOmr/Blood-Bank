/*
package com.mecodroid.blood_bank.view.fragment.navCycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.articlesAdapter.RecyclerArticlesAdapter;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.posts.PostData;
import com.mecodroid.blood_bank.data.model.posts.Posts;
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
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class ArticlesFavouriteFragment extends BaseFragment {

    @BindView(R.id.articles_favourite_fragment_rv_posts)
    RecyclerView articlesFavouriteFragmentRvPosts;

    Unbinder unbinder;
    private RecyclerArticlesAdapter articlesAdapterRecycler;

    private ApiServer apiServer;
    private View view;
    private ArrayList<PostData> postsArrayList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        view = inflater.inflate(R.layout.fragment_articles_favourite, container, false);

        unbinder = ButterKnife.bind(this, view);
        // add value tool bar
        homeActivity.setTitle(getResources().getString(R.string.favorite));
        inti();

        getPosts();

        return view;

    }

    // initialize tools
    private void inti() {

        postsArrayList = new ArrayList<>();
        apiServer = getClient().create(ApiServer.class);

    }


    // get all  post
    private void getPosts() {
        try {
            // get  data  post
            showProgressDialog(getActivity(), getResources().getString(R.string.loading));
            apiServer.getMyFavourite(LoadStringData(getActivity(), API_TOKEN)).enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Call<Posts> call, Response<Posts> response) {
                    dismissProgressDialog();
                    try {
                        if (response.body().getStatus() == 1) {

                            postsArrayList.addAll(response.body().getPaginationPosts().getData());

                            articlesFavouriteFragmentRvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));

                            articlesAdapterRecycler = new RecyclerArticlesAdapter(postsArrayList, getActivity());
                            articlesFavouriteFragmentRvPosts.setAdapter(articlesAdapterRecycler);
                            articlesAdapterRecycler.notifyDataSetChanged();

                        } else {
                            dismissProgressDialog();
                            customMassageError(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {
                        dismissProgressDialog();
                        customMassageError(getActivity(), e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<Posts> call, Throwable t) {
                    dismissProgressDialog();
                    customMassageError(getActivity(), t.getMessage());
                }
            });

        } catch (Exception e) {
            customMassageError(getActivity(), e.getMessage());
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
        articlesAdapterRecycler.notifyDataSetChanged();
    }


}
*/
