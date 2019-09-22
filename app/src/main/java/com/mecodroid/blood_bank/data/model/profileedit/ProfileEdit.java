
package com.mecodroid.blood_bank.data.model.profileedit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileEdit {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ProfileEditData data;

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

    public ProfileEditData getData() {
        return data;
    }

    public void setData(ProfileEditData data) {
        this.data = data;
    }

}
