package com.contrastsecurity.sdk.http;

import com.contrastsecurity.sdk.http.HttpMethod;
import com.contrastsecurity.sdk.http.RequestUrlConstants;

public enum ContrastRequest {
    APP_LIST(HttpMethod.GET, RequestUrlConstants.SERVICE_APP_LIST_URL, false),
    APP_TRACES(HttpMethod.GET, RequestUrlConstants.SERVICE_APP_TRACES_URL, true),
    TRACE_DETAIL(HttpMethod.GET, RequestUrlConstants.SERVICE_TRACE_DETAIL_URL, true),
    APP_DATA(HttpMethod.GET, RequestUrlConstants.SERVICE_APP_DATA_URL, true),
    COVERAGE(HttpMethod.GET, RequestUrlConstants.SERVICE_COVERAGE_URL, true),
    QUEUE_STATUS(HttpMethod.GET, RequestUrlConstants.SERVICE_QUEUE_STATUS_URL, true);

    private HttpMethod method;
    private String url;
    private boolean parm;

    ContrastRequest(HttpMethod method, String url, boolean parm) {
        this.method = method;
        this.url = url;
        this.parm = parm;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public boolean isParm() {
        return parm;
    }
}
