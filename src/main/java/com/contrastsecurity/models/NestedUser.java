package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

public class NestedUser {

    /**
     * Return the user object
     *
     * @return a user
     */
    public User getUser() {
        return user;
    }
    @SerializedName("user")
    private User user;
    
}
