
package com.mecodroid.blood_bank.data.model.notificationsSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsSettings {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private NotificationsSettingsData notificationsSettingsData;

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

    public NotificationsSettingsData getNotificationsSettingsData() {
        return notificationsSettingsData;
    }

    public void setNotificationsSettingsData(NotificationsSettingsData notificationsSettingsData) {
        this.notificationsSettingsData = notificationsSettingsData;
    }

}
