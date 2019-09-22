
package com.mecodroid.blood_bank.data.model.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataNotifyPage dataNotifyPage;

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

    public DataNotifyPage getDataNotifyPage() {
        return dataNotifyPage;
    }

    public void setDataNotifyPage(DataNotifyPage dataNotifyPage) {
        this.dataNotifyPage = dataNotifyPage;
    }

}
