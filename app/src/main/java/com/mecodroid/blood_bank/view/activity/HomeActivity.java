package com.mecodroid.blood_bank.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.notifications_count.NotificationsCount;
import com.mecodroid.blood_bank.helper.ViewDialog;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.HomeFragment;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.articles.ArticlesFragment;
import com.mecodroid.blood_bank.view.fragment.navCycle.AboutFragment;
import com.mecodroid.blood_bank.view.fragment.navCycle.ContactUsFragment;
import com.mecodroid.blood_bank.view.fragment.navCycle.NotificationsFragment;
import com.mecodroid.blood_bank.view.fragment.navCycle.SettingNotificationFragment;
import com.mecodroid.blood_bank.view.fragment.navCycle.UpdateDataUserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.HelperMethod.ReplaceFragment;
import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public HomeFragment homeFragment;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.content_home_replace)
    FrameLayout contentHomeReplace;
    @BindView(R.id.replaceframe)
    FrameLayout ReplaceContentFramre;
    @BindView(R.id.nav_menu)
    ImageView menu;
    @BindView(R.id.App_Bar_TextViewChange)
    TextView AppBarTextViewChange;
    @BindView(R.id.home_navigation_activity_iv_notification)
    ImageView homeNavigationActivityIvNotification;
    @BindView(R.id.home_navigation_activity_tv_notificationCount)
    TextView homeNavigationActivityTvNotificationCount;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    ApiServer apiServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        StatusBarUtil.setColorForDrawerLayout(HomeActivity.this, drawer, getResources().getColor(R.color.thick_blue));
        homeFragment = new HomeFragment();
        apiServer = getClient().create(ApiServer.class);
        ReplaceFragment(getSupportFragmentManager(), homeFragment
                , R.id.content_home_replace, null, null);

        ImageView imageView = (ImageView) findViewById(R.id.nav_menu);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getNotificationsCount();

    }

    // get Notifications Count
    public void getNotificationsCount() {
        try {
            // get  PaginationData  post
            apiServer.getNotificationsCount(LoadStringData(HomeActivity.this, API_TOKEN))
                    .enqueue(new Callback<NotificationsCount>() {

                        @Override
                        public void onResponse(Call<NotificationsCount> call,
                                               Response<NotificationsCount> response) {
                            if (response.body().getStatus() == 1) {
                                if
                                (response.body().getData().getNotificationsCount() == 0) {
                                    homeNavigationActivityTvNotificationCount.setVisibility(View.GONE);
                                } else if (response.body().getData().getNotificationsCount() > 0) {
                                    homeNavigationActivityTvNotificationCount.setVisibility(View.VISIBLE);
                                    homeNavigationActivityTvNotificationCount.setText(
                                            String.valueOf(response.body().getData().getNotificationsCount()));
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationsCount> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            customMassageError(HomeActivity.this, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            baseFragment.onBack();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home_menu_home) {
            ReplaceFragment(getSupportFragmentManager(), homeFragment
                    , R.id.content_home_replace, null, null);
        } else if (id == R.id.home_menu_favorite) {
            ArticlesFragment favourites = new ArticlesFragment();
            favourites.favourites = true;
            AppBarTextViewChange.setText(R.string.favorite);

            ReplaceFragment(getSupportFragmentManager(), favourites
                    , R.id.content_home_replace, null, null);
        } else if (id == R.id.home_menu_profile) {
            ReplaceFragment(getSupportFragmentManager(), new UpdateDataUserFragment()
                    , R.id.content_home_replace, null, null);

        } else if (id == R.id.home_menu_notify_setting) {
            ReplaceFragment(getSupportFragmentManager(), new SettingNotificationFragment()
                    , R.id.content_home_replace, null, null);


        } else if (id == R.id.home_menu_instructions) {


        } else if (id == R.id.home_menu_about_app) {
            ReplaceFragment(getSupportFragmentManager(), new AboutFragment()
                    , R.id.content_home_replace, null, null);


        } else if (id == R.id.home_menu_contact_us) {
            ReplaceFragment(getSupportFragmentManager(), new ContactUsFragment()
                    , R.id.content_home_replace, null, null);


        } else if (id == R.id.home_menu_logout) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void setVisibility(int visibility) {
        ReplaceContentFramre.setVisibility(visibility);
    }

    @OnClick({R.id.nav_menu, R.id.App_Bar_TextViewChange, R.id.toolbar,
            R.id.home_navigation_activity_iv_notification,
            R.id.home_navigation_activity_tv_notificationCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;

            case R.id.nav_menu:

                break;

            case R.id.App_Bar_TextViewChange:

                break;

            case R.id.home_navigation_activity_iv_notification:

                ReplaceFragment(getSupportFragmentManager(), new NotificationsFragment(),
                        R.id.content_home_replace, null, null);
                AppBarTextViewChange.setVisibility(View.VISIBLE);

                break;

            case R.id.home_navigation_activity_tv_notificationCount:

                break;
        }
    }

    public void setTitle(String title) {
        AppBarTextViewChange.setText(title);
    }
}
