package com.contrastsecurity.models;


import java.util.ArrayList;

public class BaseResponse {

    public ArrayList<BaseResponse> list = new ArrayList<>();

    protected String clazz = "BaseResponse";

    public Integer getCount() {
        return count;
    }
    private Integer count = 0;
}
