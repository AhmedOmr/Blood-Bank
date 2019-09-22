package com.mecodroid.blood_bank.view.fragment.HomeCycle.articles;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.adapter.articlesAdapter.RecyclerArticlesHomeAdapter;
import com.mecodroid.blood_bank.adapter.onEndless.OnEndless;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.categories.Categories;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.posts.PostData;
import com.mecodroid.blood_bank.data.model.posts.Posts;
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
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.disappearKeypad;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.isConnected;
import static com.mecodroid.blood_bank.helper.HelperMethod.isRTL;
import static com.mecodroid.blood_bank.helper.HelperMethod.setSpinner;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends BaseFragment {

    public boolean backFromFavourites;
    public boolean favourites = false;
    @BindView(R.id.articles_fragment_im_search)
    ImageButton articlesFragmentImSearch;
    @BindView(R.id.articles_fragment_edit_search)
    EditText articlesFragmentEditSearch;
    @BindView(R.id.articles_fragment_spin_catogery)
    Spinner articlesFragmentSpinCatogery;
    @BindView(R.id.articles_fragment_rv_posts)
    RecyclerView articlesFragmentRvPosts;
    Unbinder unbinder;
    @BindView(R.id.articles_fragment_srl_articles_list_refresh)
    SwipeRefreshLayout articlesFragmentSrlArticlesListRefresh;
    @BindView(R.id.articles_fragment_txt_no_results)
    TextView articlesFragmentTxtNoResults;
    @BindView(R.id.articles_fragment_txt_no_items)
    TextView articlesFragmentTxtNoItems;
    @BindView(R.id.articles_fragment_lin_search)
    LinearLayout articlesFragmentLinSearch;
    @BindView(R.id.articles_fragment_ll_recycler)
    LinearLayout articlesFragmentLlRecycler;
    // store category  type name
    ArrayList<String> nameeCategories = new ArrayList<>();
    // store category  type id
    ArrayList<Integer> idCategories = new ArrayList<Integer>();
    @BindView(R.id.articles_fragment_cardview)
    CardView articlesFragmentCardview;
    @BindView(R.id.articles_fragment_container)
    RelativeLayout articlesFragmentContainer;
    private ApiServer apiServer;
    private ArrayList<PostData> postsArrayList;
    private ArrayList<GeneralModel> categoriesArrayList;
    private RecyclerArticlesHomeAdapter articlesAdapterRecycler;
    private int maxPage = 0;
    private OnEndless onEndless;
    private Integer category_type_id;
    private LinearLayoutManager linearLayoutManager;
    private boolean filterSearch = false;
    private String keyword = "";
    private boolean checkFilterPost= true;

    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment();
        setUpHomeActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        disappearKeypad(getActivity(), articlesFragmentEditSearch);
        unbinder = ButterKnife.bind(this, view);

        initTools();

        if (favourites) {
            articlesFragmentCardview.setVisibility(View.GONE);
            homeActivity.setTitle(getString(R.string.favorite));
        }

        initRecyclerView();
        if (!favourites) {
            if (idCategories.size() == 0) {
                getDataCategory();
            } else {
                setSpinner(getActivity(), articlesFragmentSpinCatogery, nameeCategories);
            }
        }
        articlesFragmentSrlArticlesListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndless.current_page = 1;
                onEndless.previousTotal = 0;
                onEndless.previous_page = 1;

                maxPage = 0;

                postsArrayList = new ArrayList<>();

                articlesAdapterRecycler = new RecyclerArticlesHomeAdapter(getActivity(), postsArrayList,
                        favourites, articlesFragmentTxtNoItems);
                articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);

                if (filterSearch) {
                    getPostsFilter(1);
                } else {
                    getPosts(1);
                }
            }
        });

        if (postsArrayList.size() == 0 || backFromFavourites == true) {
            backFromFavourites = false;
            getPosts(1);
        }


        return view;
    }

    // initialize tools
    private void initTools() {
        // check Language
        if (isRTL()) {
            articlesFragmentSpinCatogery.setBackground(getResources().getDrawable(R.drawable.bgspinrt));
        } else {
            articlesFragmentSpinCatogery.setBackground(getResources().getDrawable(R.drawable.bgspinlt));
            articlesFragmentSpinCatogery.setPaddingRelative(0, 0, 80, 0);
        }
        apiServer = getClient().create(ApiServer.class);
        postsArrayList = new ArrayList<>();
        categoriesArrayList = new ArrayList<>();
    }

    // listener from count items  recyclerView
    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        articlesFragmentRvPosts.setLayoutManager(linearLayoutManager);
        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndless.previous_page = current_page;
                        if (filterSearch) {
                            getPostsFilter(current_page);
                        } else {
                            getPosts(current_page);
                        }
                    } else {
                        onEndless.current_page = onEndless.previous_page;
                    }
                } else {
                    onEndless.current_page = onEndless.previous_page;
                }
            }
        };
        articlesFragmentRvPosts.addOnScrollListener(onEndless);
        articlesAdapterRecycler = new RecyclerArticlesHomeAdapter(getActivity(),
                postsArrayList, favourites, articlesFragmentTxtNoItems);
        articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);

    }

    // get all category in spinner
    private void getDataCategory() {
        apiServer.getCategories().enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                dismissProgressDialog();
                List<GeneralModel> datacategories = response.body().getData();
                // title category  type
                nameeCategories.add(getResources().getString(R.string.all_categories));
                idCategories.add(0);
                // loop all category  types and pass name types to category name list, pass the id of category  types to category  id list
                for (int i = 0; i < datacategories.size(); i++) {
                    String categoryTypNAme = datacategories.get(i).getName();
                    Integer categoryTypId = datacategories.get(i).getId();
                    nameeCategories.add(categoryTypNAme);
                    idCategories.add(categoryTypId);
                }

                // create array adapter to view list
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, nameeCategories);
                // to specify form of spinner
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                // bind spinner with adapter
                articlesFragmentSpinCatogery.setAdapter(adapter);
                // interaction with spinner
                articlesFragmentSpinCatogery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // return the item selected from spinner else postion equal zero (title)
                        category_type_id = idCategories.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                dismissProgressDialog();
                customMassageError(getActivity(), t.getMessage());
            }
        });
    }


    // this is method all in click
    private void onClickImageSearch() {
        // edit text search keyword
        articlesFragmentImSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = articlesFragmentEditSearch.getText().toString().trim();
                if (articlesFragmentSpinCatogery.getSelectedItemPosition() == 0
                        && keyword.equals("")) {
                    if (filterSearch) {
                        filterSearch = false;

                        onEndless.current_page = 1;
                        onEndless.previous_page = 1;
                        onEndless.previousTotal = 0;
                        onEndless.totalItemCount = 0;
                        maxPage = 0;

                        postsArrayList = new ArrayList<>();
                        articlesAdapterRecycler = new RecyclerArticlesHomeAdapter(getActivity(), postsArrayList,
                                favourites, articlesFragmentTxtNoItems);
                        articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);
                        getPosts(1);
                    }

                } else {
                    filterSearch = true;

                    onEndless.current_page = 1;
                    onEndless.previous_page = 1;
                    onEndless.previousTotal = 0;
                    onEndless.totalItemCount = 0;
                    maxPage = 0;
                    postsArrayList = new ArrayList<>();
                    articlesAdapterRecycler = new RecyclerArticlesHomeAdapter(getActivity(), postsArrayList,
                            favourites, articlesFragmentTxtNoItems);
                    articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);

                    getPostsFilter(1);
                }

            }
        });

    }

    private void searchCatgories(){

        // edit text search keyword
        articlesFragmentImSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articlesFragmentSpinCatogery.getSelectedItemPosition() == 0
                        && articlesFragmentEditSearch.getText().toString().isEmpty() && !checkFilterPost) {

                    articlesAdapterRecycler = new RecyclerArticlesHomeAdapter( getActivity(),postsArrayList,
                            favourites,articlesFragmentTxtNoItems);
                    articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);
                    checkFilterPost = true;

                } else {

                    articlesAdapterRecycler = new RecyclerArticlesHomeAdapter( getActivity(),postsArrayList,
                            favourites,articlesFragmentTxtNoItems);
                    articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);
                    checkFilterPost = false;

                    getPostsFilter(1);

                }

            }
        });
    }
    // get all  post
    private void getPosts(int page) {
        Call<Posts> call;

        if (!favourites) {
            call = apiServer.getPosts(LoadStringData(getActivity(), API_TOKEN), page);
        } else {
            call = apiServer.getMyFavourite(LoadStringData(getActivity(), API_TOKEN), page);
        }
        loadDataPosts(page, call);
    }

    // get  all filter Post with idCategory / KeyWord
    public void getPostsFilter(final int page) {
        keyword = articlesFragmentEditSearch.getText().toString();
        Call<Posts> call = apiServer.getPostFilter(LoadStringData(getActivity(), API_TOKEN),
                page, keyword, category_type_id);
        loadDataPosts(page, call);

    }

    // load all data posts
    private void loadDataPosts(final int page, Call<Posts> call) {
        if (isConnected(getActivity())) {
            //showProgressDialog(getActivity(), getString(R.string.waiit));
            call.enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Call<Posts> call, Response<Posts> response) {
                    dismissProgressDialog();
                    try {
                        dismissProgressDialog();
                        articlesFragmentSrlArticlesListRefresh.setRefreshing(false);
                        if (response.body().getStatus() == 1) {

                            if (page == 1) {
                                if (response.body().getPaginationPosts().getTotal() > 0) {

                                    articlesFragmentTxtNoResults.setVisibility(View.GONE);

                                } else {
                                    if (favourites) {
                                        articlesFragmentTxtNoItems.setVisibility(View.VISIBLE);
                                    } else {
                                        articlesFragmentTxtNoResults.setVisibility(View.VISIBLE);

                                    }
                                }

                                onEndless.current_page = 1;
                                onEndless.previousTotal = 0;
                                onEndless.previous_page = 1;

                                maxPage = 0;

                                postsArrayList = new ArrayList<>();
                                articlesAdapterRecycler = new RecyclerArticlesHomeAdapter(getActivity(), postsArrayList, favourites, articlesFragmentTxtNoItems);
                                articlesFragmentRvPosts.setAdapter(articlesAdapterRecycler);

                            }

                            maxPage = response.body().getPaginationPosts().getLastPage();
                            postsArrayList.addAll(response.body().getPaginationPosts().getData());
                            articlesAdapterRecycler.notifyDataSetChanged();


                        } else {
                            customMassageError(getActivity(), response.body().getMsg());
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<Posts> call, Throwable t) {
                    dismissProgressDialog();
                    customMassageError(getActivity(), t.getMessage());

                    try {
                        articlesFragmentSrlArticlesListRefresh.setRefreshing(false);
                    } catch (Exception e) {

                    }
                }
            });

        } else {
            dismissProgressDialog();
            customMassageError(getActivity(), getResources().getString(R.string.no_internet));
            try {
                articlesFragmentSrlArticlesListRefresh.setRefreshing(false);
            } catch (Exception e) {

            }
        }
    }

    @OnClick({R.id.articles_fragment_im_search, R.id.articles_fragment_container})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.articles_fragment_im_search:
               // onClickImageSearch();
                searchCatgories();
                break;
            case R.id.articles_fragment_container:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        setUpHomeActivity();
        homeActivity.homeFragment.backFromFavourites = true;
        ReplaceFragment(getActivity().getSupportFragmentManager(), homeActivity.homeFragment,
                R.id.content_home_replace, null, null);
    }

}

