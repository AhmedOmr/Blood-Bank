
package com.mecodroid.blood_bank.data.model.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataNotify {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("donation_request_id")
    @Expose
    private String donationRequestId;
    @SerializedName("pivot")
    @Expose
    private PivotNotify pivotNotify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDonationRequestId() {
        return donationRequestId;
    }

    public void setDonationRequestId(String donationRequestId) {
        this.donationRequestId = donationRequestId;
    }

    public PivotNotify getPivotNotify() {
        return pivotNotify;
    }

    public void setPivotNotify(PivotNotify pivotNotify) {
        this.pivotNotify = pivotNotify;
    }

}
