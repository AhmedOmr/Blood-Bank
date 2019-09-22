
package com.mecodroid.blood_bank.data.model.donationRequests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DonationRequests {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DonationPagination donationPagination;

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

    public DonationPagination getDonationPagination() {
        return donationPagination;
    }

    public void setDonationPagination(DonationPagination donationPagination) {
        this.donationPagination = donationPagination;
    }

}

