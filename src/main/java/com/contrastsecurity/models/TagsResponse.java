package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagsResponse {

    public List<String> getTags() {
        return tags;
    }
    @SerializedName("tags")
    private List<String> tags;
}

