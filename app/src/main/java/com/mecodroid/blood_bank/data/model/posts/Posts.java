
package com.mecodroid.blood_bank.data.model.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private PostsPagination paginationPosts;

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

    public PostsPagination getPaginationPosts() {
        return paginationPosts;
    }

    public void setPaginationPosts(PostsPagination paginationPosts) {
        this.paginationPosts = paginationPosts;
    }

}
