package com.mecodroid.blood_bank.adapter.articlesAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.favourite.FavouriteModel;
import com.mecodroid.blood_bank.data.model.posts.PostData;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.articles.ArticlesDetailsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageDone;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.onLoadImageFromUrl;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class RecyclerArticlesAdapter extends RecyclerView.Adapter<RecyclerArticlesAdapter.ViewHolder> {

    ArrayList<PostData> postsArrayList;
    ViewHolder viewHolder;
    Context context;
    private ApiServer apiServer;
    private TextView articlesFragmentTxtNoItems;
    private boolean favourites;

    public RecyclerArticlesAdapter(Context context, ArrayList<PostData> postsArrayList,
                                   boolean favourites, TextView articlesFragmentTxtNoItems) {
        this.postsArrayList = postsArrayList;
        this.context = context;
        apiServer = getClient().create(ApiServer.class);
        this.favourites = favourites;
        this.articlesFragmentTxtNoItems = articlesFragmentTxtNoItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item_row_articles_adapter, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.setIsRecyclable(false);
        setData(viewHolder, position);
        setAction(viewHolder, position);
    }

    // set data to items
    private void setData(ViewHolder viewHolder, int position) {
        try {
            // set title
            viewHolder.articlesHomeAdapterTxtTitle.setText(postsArrayList.get(position).getTitle());
            // set image post
            onLoadImageFromUrl(viewHolder.articlesHomeAdapterImgPost, postsArrayList.get(position).getThumbnailFullPath(), context, 0);
            // set icon favourite
            viewHolder.articlesHomeAdapterCheckBoxFavorite.setChecked(postsArrayList.get(position).getIsFavourite());
        } catch (Exception e) {
            customMassageError((Activity) context, e.getMessage());
        }
    }

    // on click action
    private void setAction(final ViewHolder viewHolder, final int position) {
        // on click icon favourite if choose true or false
        viewHolder.articlesHomeAdapterCheckBoxFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteApi(viewHolder, position);
                notifyDataSetChanged();
            }
        });

        viewHolder.articlesHomeAdapterImgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticlesDetailsFragment contentArticlesFragment = new ArticlesDetailsFragment();
                //pass object from data to retrive data in Articles Content Fragment
                contentArticlesFragment.post = postsArrayList.get(position);
                ReplaceFragment(((FragmentActivity) v.getContext()).getSupportFragmentManager(),
                        contentArticlesFragment, R.id.content_home_replace
                        , null, null);

            }
        });
    }

    // set  favourite data
    private void setFavoriteApi(final ViewHolder viewHolder, final int position) {
        final PostData postsData = postsArrayList.get(position);

        postsArrayList.get(position).setIsFavourite(!postsArrayList.get(position).getIsFavourite());
        if (postsArrayList.get(position).getIsFavourite()) {
            viewHolder.articlesHomeAdapterCheckBoxFavorite
                    .setChecked(postsArrayList.get(position).getIsFavourite());
            customMassageDone((Activity) context,
                    context.getResources().getString(R.string.add_to_favourite));

        } else {
            viewHolder.articlesHomeAdapterCheckBoxFavorite
                    .setChecked(!postsArrayList.get(position).getIsFavourite());
            customMassageDone((Activity)
                    context, context.getResources().getString(R.string.remove_from_favourite));
            if (favourites) {
                postsArrayList.remove(position);
                notifyDataSetChanged();
                if (postsArrayList.size() == 0) {
                    articlesFragmentTxtNoItems.setVisibility(View.VISIBLE);
                }
            }
        }

        apiServer.setFavourite(postsData.getId(), LoadStringData((Activity) context, API_TOKEN))
                .enqueue(new Callback<FavouriteModel>() {
                    @Override
                    public void onResponse(Call<FavouriteModel> call, Response<FavouriteModel> response) {
                        showProgressDialog((Activity) context, context.getResources().getString(R.string.waiit));
                        try {
                            if (response.body().getStatus() == 1) {
                                dismissProgressDialog();
                            } else {
                                dismissProgressDialog();
                                postsArrayList.get(position).setIsFavourite(!postsArrayList.get(position).getIsFavourite());
                                if (postsArrayList.get(position).getIsFavourite()) {
                                    viewHolder.articlesHomeAdapterCheckBoxFavorite.setChecked(postsArrayList.get(position).getIsFavourite());
                                    if (favourites) {
                                        postsArrayList.add(position, postsData);
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    viewHolder.articlesHomeAdapterCheckBoxFavorite.setChecked(!postsArrayList.get(position).getIsFavourite());
                                }
                            }

                        } catch (Exception e) {
                            dismissProgressDialog();
                            customMassageError((Activity)
                                    context, e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<FavouriteModel> call, Throwable t) {
                        dismissProgressDialog();
                        try {
                            postsArrayList.get(position).setIsFavourite(!postsArrayList.get(position).getIsFavourite());
                            if (postsArrayList.get(position).getIsFavourite()) {
                                viewHolder.articlesHomeAdapterCheckBoxFavorite.setChecked(postsArrayList.get(position).getIsFavourite());
                                if (favourites) {
                                    postsArrayList.add(position, postsData);
                                    notifyDataSetChanged();
                                }
                            } else {
                                viewHolder.articlesHomeAdapterCheckBoxFavorite.setChecked(!postsArrayList.get(position).getIsFavourite());
                            }
                        } catch (Exception e) {
                            customMassageError((Activity) context, e.getMessage());

                        }
                    }
                });

    }


    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        notifyDataSetChanged();
    }

    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.articles_home_adapter_img_post)
        ImageView articlesHomeAdapterImgPost;
        @BindView(R.id.articles_home_adapter_txt_title)
        TextView articlesHomeAdapterTxtTitle;
        @BindView(R.id.articles_home_adapter_check_box_favorite)
        CheckBox articlesHomeAdapterCheckBoxFavorite;
        View view;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }


}