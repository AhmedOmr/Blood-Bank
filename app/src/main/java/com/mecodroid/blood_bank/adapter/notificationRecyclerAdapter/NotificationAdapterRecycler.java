package com.mecodroid.blood_bank.adapter.notificationRecyclerAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.notifications.DataNotify;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.BloodBankConatants.API_TOKEN;
import static com.mecodroid.blood_bank.helper.HelperMethod.getDonation;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.LoadStringData;


public class NotificationAdapterRecycler extends RecyclerView.Adapter<NotificationAdapterRecycler.ViewHolder> {

    ArrayList<DataNotify> notificationArraylist = new ArrayList<>();


    private ApiServer apiServer;
    Activity context;

    public NotificationAdapterRecycler(Activity context, ArrayList<DataNotify> notificationArraylist) {
        this.notificationArraylist = notificationArraylist;
        this.context = context;
        apiServer = getClient().create(ApiServer.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_row_notifcation_adapter, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);
        setData(viewHolder, position);
        setAction(viewHolder, position);
    }


    private void setData(ViewHolder holder, int position) {
        try {
            holder.notificationListAdapterTvNotificationText.setText(notificationArraylist.get(position).getTitle());
            holder.notificationListAdapterTvNotificationTime.setText(notificationArraylist.get(position).getCreatedAt());

            if (notificationArraylist.get(position).getPivotNotify().getIsRead().equals("0")) {
                holder.notificationListAdapterIvNotificationImage.setImageResource(R.drawable.notify_36);
            } else {
                holder.notificationListAdapterIvNotificationImage.setImageResource(R.drawable.icon_un_notify);
            }

        } catch (Exception e) {

        }
    }

    private void setAction(ViewHolder holder, final int position) {

        try {

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDonation(context, apiServer, notificationArraylist.get(position).getDonationRequestId(),
                            LoadStringData(context, API_TOKEN), false);
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return notificationArraylist.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_list_adapter_iv_notification_image)
        ImageView notificationListAdapterIvNotificationImage;
        @BindView(R.id.notification_list_adapter_tv_notification_text)
        TextView notificationListAdapterTvNotificationText;
        @BindView(R.id.notification_list_adapter_tv_notification_Time)
        TextView notificationListAdapterTvNotificationTime;
        @BindView(R.id.notification_list_adapter_iv_notification_Image_time)
        ImageView notificationListAdapterIvNotificationImageTime;

        View view;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
