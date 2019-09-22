package com.mecodroid.blood_bank.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.view.activity.RegsteratinAndLoginActivity;
import static com.mecodroid.blood_bank.data.api.RetrfitClient.getClient;
import static com.mecodroid.blood_bank.helper.HelperMethod.removeNotificationToken;
import static com.mecodroid.blood_bank.helper.SharedPreferencesManger.clean;

public class ViewDialog {
    private ApiServer apiServices;
    private Activity activity;

    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        dialog.setCanceledOnTouchOutside(true);
        apiServices = getClient().create(ApiServer.class);
        TextView text = (TextView) dialog.findViewById(R.id.text);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialog_ok);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call
                removeNotificationToken(apiServices, activity);

                clean(activity);

                Intent i = new Intent(activity, RegsteratinAndLoginActivity.class);

                activity.startActivity(i);
                // close this activity
                activity.finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_no);
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }
}
