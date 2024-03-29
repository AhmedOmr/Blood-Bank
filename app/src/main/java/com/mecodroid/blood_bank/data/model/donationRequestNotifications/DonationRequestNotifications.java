
package com.mecodroid.blood_bank.data.model.donationRequestNotifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationData;

public class DonationRequestNotifications {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DonationData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DonationData getData() {
        return data;
    }

    public void setData(DonationData data) {
        this.data = data;
    }

}
