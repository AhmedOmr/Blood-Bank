package com.mecodroid.blood_bank.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.donationRequestNotifications.DonationRequestNotifications;
import com.mecodroid.blood_bank.data.model.generalResponse.GeneralResponse;
import com.mecodroid.blood_bank.data.model.notifications_count.NotificationsCount;
import com.mecodroid.blood_bank.view.activity.HomeActivity;
import com.mecodroid.blood_bank.view.fragment.HomeCycle.donationRequests.DonationRequestContentFragment;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;

public class HelperMethod {

    public static AlertDialog alertDialog;
    public static Snackbar snackbar;
    public static Toast toast;
    private static ProgressDialog checkDialog;
    private static String getNotificationsCount;
    private static ConnectivityManager cm;
    private static LovelyProgressDialog lovelyDailog;

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);

            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);

            return Imagebody;
        } else {
            return null;
        }
    }


    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), part);
                return requestBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context, int drId) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void createSnackBar(View view, String message, Context context) {
        final Snackbar snackbar = Snackbar.make(view, message, 50000);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        })
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))

                .show();
    }

    public static void createSnackBar(View view, String message, Context context, View.OnClickListener action, String Title) {
        snackbar = Snackbar.make(view, message, 50000);
        snackbar.setAction(Title, action)
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    //Calender
    public static void showCalender(Context context, String title, final TextView text_view_data, final DateModel data1) {

        DatePickerDialog mDatePicker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                String data = selectedYear + "-" + String.format(new Locale("en"), mFormat.format(Double.valueOf((selectedMonth + 1)))) + "-"
                        + mFormat.format(Double.valueOf(selectedDay));
                data1.setDate_txt(data);
                data1.setDay(mFormat.format(Double.valueOf(selectedDay)));
                data1.setMonth(mFormat.format(Double.valueOf(selectedMonth + 1)));
                data1.setYear(String.valueOf(selectedYear));
                if (text_view_data != null) {
                    text_view_data.setText(data);
                }
            }
        }, Integer.parseInt(data1.getYear()), Integer.parseInt(data1.getMonth()) - 1, Integer.parseInt(data1.getDay()));
        mDatePicker.setTitle(title);
        mDatePicker.show();
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog(activity);
            checkDialog.setMessage(title);
            checkDialog.setIndeterminate(false);
            checkDialog.setCancelable(false);
            checkDialog.show();

        } catch (Exception e) {

        }
    }

    public static void dismissProgressDialog() {
        try {
            if (checkDialog != null && checkDialog.isShowing()) {
                checkDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public static void ReplaceFragment(FragmentManager supportFragmentManager, Fragment fragment, int container_id
            , TextView toolbarTitle, String title) {

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        transaction.replace(container_id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }

    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    public static void showToastMassage(Activity activity, String title) {
        try {
            toast = Toast.makeText(activity,
                    title,
                    Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {

        }
    }

    public static void customMassageError(Activity activity, String title) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastLayout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) activity.findViewById(R.id.toast_root_view));

        toastLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_toast_error));

        TextView header = toastLayout.findViewById(R.id.toast_header);
        header.setText(title);

        ImageView body = toastLayout.findViewById(R.id.toast_body);
        body.setImageResource(R.drawable.failed_icon24);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    public static void customMassageDone(Activity activity, String title) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastLayout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) activity.findViewById(R.id.toast_root_view));

        toastLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_toast_done));
        //toastLayout.setBackgroundColor(getColorWithAlpha(Color.RED, 0.7f));

        TextView header = toastLayout.findViewById(R.id.toast_header);
        header.setText(title);

        ImageView body = toastLayout.findViewById(R.id.toast_body);
        body.setImageResource(R.drawable.done_icon24);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    // set color with alpha
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    // Check Language
    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    private static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    // check internet
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager  cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            return isConnected;
        } catch (NullPointerException e) {

        }

        return false;
    }
/*
// tool bar
    public static void ToolBar(final FragmentManager supportFragmentManager, final Activity activity, Toolbar toolbar, String title) {
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.black));
        toolbar.setTitle(title);
        toolbar.setTitleMargin(50, 5, 50, 5);
        toolbar.setNavigationIcon(R.drawable.icon_back_menu);
        toolbar.inflateMenu(R.menu.tab_menu);
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_notify);
        View actionView = MenuItemCompat.getActionView(menuItem);
        TextView textCartItemCount = actionView.findViewById(R.id.cart_badge);
        if (getNotificationsCount(activity) != null) {
            if (!getNotificationsCount(activity).equals("null")) {
                textCartItemCount.setText(getNotificationsCount(activity));

            }
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();

            }
        });
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(supportFragmentManager, new NotificationsFragment(), R.id.drawer_layout,
                        null, null);

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_notify:
                        ReplaceFragment(supportFragmentManager, new NotificationsFragment(), R.id.drawer_layout,
                                null, null);
                        break;

                }
                return false;
            }
        });
    }
    */

    // get Notifications Count
    public static String getNotificationsCount(final Activity activity) {
        try {
            ApiServer apiServer = getClient().create(ApiServer.class);
            // get  PaginationData  post
            apiServer.getNotificationsCount(LoadStringData(activity, API_TOKEN)).enqueue(new Callback<NotificationsCount>() {

                @Override
                public void onResponse(Call<NotificationsCount> call, Response<NotificationsCount> response) {

                    if (response.body().getStatus() == 1) {
                        getNotificationsCount = String.valueOf(response.body().getData().getNotificationsCount());

                    } else {
                        Toast.makeText(activity, "Not PaginationData ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsCount> call, Throwable t) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
        return getNotificationsCount;
    }

    public static void getDonation(final Activity activity, ApiServer apiServices, String donationRequestId, String ApiToken, final boolean fromDonation) {
        showProgressDialog(activity, activity.getString(R.string.wait));
        apiServices.getDonationDetails(ApiToken, donationRequestId).enqueue(new Callback<DonationRequestNotifications>() {
            @Override
            public void onResponse(Call<DonationRequestNotifications> call, Response<DonationRequestNotifications> response) {
                try {
                    dismissProgressDialog();
                    if (response.body().getStatus() == 1) {

                        HomeActivity navigationActivity = (HomeActivity) activity;
                        navigationActivity.getNotificationsCount();
                        navigationActivity.setTitle(activity.getString(R.string.donation_req) +
                                response.body().getData().getPatientName());

                        DonationRequestContentFragment donationDetails = new DonationRequestContentFragment();
                        donationDetails.donationRequest = response.body().getData();
                        donationDetails.fromDonation = fromDonation;

                        ReplaceFragment(navigationActivity.getSupportFragmentManager(),
                                donationDetails, R.id.content_home_replace, null, null);
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<DonationRequestNotifications> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    // permission calls
    public static void callPermissions(Activity activity, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            }
        } else {

            customMassageError(activity, "" + Manifest.permission.CALL_PHONE + " is already granted.");
        }
    }

    public static void registerNotificationToken(ApiServer apiServer, Activity activity) {
        apiServer.getRegisterNotificationToken(FirebaseInstanceId.getInstance().getToken(),
                LoadStringData(activity, API_TOKEN), "android")
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
    }

    public static void removeNotificationToken(ApiServer apiServices, Activity activity) {
        apiServices.RemoveToken(FirebaseInstanceId.getInstance().getToken(),
                LoadStringData(activity, API_TOKEN))
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    }
                });
    }

    public static void setTitle(String title, TextView textView) {
        textView.setText(title);
    }

    public static void setSpinner(Activity activity, Spinner spinner, List<String> names) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                R.layout.spinner_layout2, names);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        dismissProgressDialog();
    }

    public static void setSpinnerBold(Activity activity, Spinner spinner, List<String> names) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                R.layout.spinner_layout, names);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
    }

    public static void setLovelyProgressDailog(Activity activity, int icon,
                                               String topTitle, int titleColor, int topColor) {
        lovelyDailog = new LovelyProgressDialog(activity);
        lovelyDailog.setIcon(icon)
                .setTitle(R.string.wait)
                .setTitleGravity(Gravity.CENTER)
                .setTopTitle(topTitle)
                .setTopTitleColor(titleColor)
                .setTopColorRes(topColor)
                .show();
    }

    public static void dismissLovelyDailog() {
        lovelyDailog.dismiss();
    }

}
