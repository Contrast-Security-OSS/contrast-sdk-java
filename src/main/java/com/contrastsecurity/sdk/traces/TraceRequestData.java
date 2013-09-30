/*
 * Copyright (c) 2013, Contrast Security, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.contrastsecurity.sdk.traces;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
public class TraceRequestData {
    private String method;
    private String port;
    private String protocol;
    private String version;
    private String uri;
    private String qs;
    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "h")
    private ArrayList<TraceRequestItem> headers;
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "p")
    private ArrayList<TraceRequestItem> parameters;

    public TraceRequestData() {
    }

    @XmlAttribute(name = "method")
    public String getRequestMethod() {
        return method;
    }

    @XmlAttribute(name = "port")
    public String getRequestPort() {
        return port;
    }

    @XmlAttribute(name = "protocol")
    public String getRequestProtocol() {
        return protocol;
    }

    @XmlAttribute(name = "version")
    public String getRequestVersion() {
        return version;
    }

    @XmlAttribute(name = "uri")
    public String getRequestUri() {
        return uri;
    }

    @XmlAttribute(name = "qs")
    public String getRequestQS() {
        return qs;
    }

    /**
     * Retrieve the Request Headers as an ArrayList.
     *
     * @return ArrayList&lt;TraceRequestItem&gt;
     */
    public ArrayList<TraceRequestItem> getRequestHeaders() {
        return this.headers;
    }

    /**
     * Retrieve the Request Parameters as an ArrayList.
     *
     * @return ArrayList&lt;TraceRequestItem&gt;
     */
    public ArrayList<TraceRequestItem> getRequestParameters() {
        return this.parameters;
    }

    /**
     * Retrieve the Request Headers as a HashMap.
     *
     * @return HashMap&lt;String,String&gt;
     */
    public HashMap<String, String> getRequestHeadersMap() {
        HashMap<String, String> newHeaders = new HashMap<String, String>();

        for (TraceRequestItem tri : this.headers) {
            newHeaders.put(tri.getItemName().toLowerCase(), tri.getItemValue());
        }

        return newHeaders;
    }

    /**
     * Retrieve the Request Parameters as a HashMap.
     *
     * @return HashMap&lt;String,String&gt;
     */
    public HashMap<String, String> getRequestParametersMap() {
        HashMap<String, String> newParameters = new HashMap<String, String>();

        for (TraceRequestItem tri : this.parameters) {
            newParameters.put(tri.getItemName().toLowerCase(), tri.getItemValue());
        }

        return newParameters;
    }

    /**
     * Retrieve the Request data as a properly formed HTTP Request in String format.
     *
     * @return String
     */
    public String getRequestString() {
        StringBuilder headerStr = new StringBuilder();

        headerStr.append(this.method.toUpperCase() + " " + this.uri + "?" + this.qs + " " +
                this.protocol.toUpperCase() + "/" + this.version + "\n");
        for (TraceRequestItem tri : this.headers) {
            headerStr.append(tri.getItemName() + ": " + tri.getItemValue() + "\n");
        }
        headerStr.append("\n");
        for (TraceRequestItem tri : this.parameters) {
            headerStr.append(tri.getItemName() + "=");
            headerStr.append((
                    (tri.getItemValue() == null || tri.getItemName().equals("")) ?
                            "" : tri.getItemValue()));
            headerStr.append("&");
        }
        if (headerStr.toString().endsWith("&")) {
            headerStr.deleteCharAt(headerStr.lastIndexOf("&"));
        }
        headerStr.append("\n\n");

        return headerStr.toString();
    }

    public void setRequestMethod(String method) {
        this.method = method;
    }

    public void setRequestPort(String port) {
        this.port = port;
    }

    public void setRequestProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setRequestVersion(String version) {
        this.version = version;
    }

    public void setRequestUri(String uri) {
        this.uri = uri;
    }

    public void setRequestQS(String qs) {
        this.qs = qs;
    }

    public void setRequestHeaders(ArrayList<TraceRequestItem> hList) {
        this.headers = hList;
    }

    public void setRequestParameters(ArrayList<TraceRequestItem> pList) {
        this.parameters = pList;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder thisObj = new StringBuilder();

        thisObj.append("\t[method:" + this.method + ",port:" + this.port + ",protocol:" + this.protocol +
                ",version:" + this.version + ",uri:" + this.uri + ",qs:" + this.qs + "],\n");
        thisObj.append("\theaders:{");
        for (TraceRequestItem ctri : headers) {
            thisObj.append(ctri.toString() + ",");
        }
        if (thisObj.toString().endsWith(",")) {
            thisObj.deleteCharAt(thisObj.lastIndexOf(","));
        }
        thisObj.append("\n},\n");
        thisObj.append("\tparameters:{");
        for (TraceRequestItem ctri : parameters) {
            thisObj.append(ctri.toString() + ",");
        }
        if (thisObj.toString().endsWith(",")) {
            thisObj.deleteCharAt(thisObj.lastIndexOf(","));
        }
        thisObj.append("\n}\n}");

        return thisObj.toString();
    }
}
