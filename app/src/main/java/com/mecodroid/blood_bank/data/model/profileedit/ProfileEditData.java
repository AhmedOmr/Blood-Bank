
package com.mecodroid.blood_bank.data.model.profileedit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mecodroid.blood_bank.data.model.login.Client;

public class ProfileEditData {

    @SerializedName("client")
    @Expose
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
