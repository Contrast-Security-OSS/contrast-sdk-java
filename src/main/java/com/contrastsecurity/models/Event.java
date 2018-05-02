/*******************************************************************************
 * Copyright (c) 2017 Contrast Security.
 * All rights reserved. 
 *
 * This program and the accompanying materials are made available under 
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License.
 *
 * The terms of the GNU GPL version 3 which accompanies this distribution
 * and is available at https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Contributors:
 *     Contrast Security - initial API and implementation
 *******************************************************************************/
package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    @SerializedName("object")
    private String fObject;
    private String method;
    private List<Parameter> parameters;
    private List<Stacktrace> stacktraces;
    @SerializedName("class")
    private String clazz;
    @SerializedName("object_tracked")
    private boolean objectTracked;
    @SerializedName("return")
    private String fReturn;
    @SerializedName("return_tracked")
    private boolean returnTracked;
    @SerializedName("last_custom_frame")
    private int lastCustomFrame;

    public String getfObject() {
        return fObject;
    }

    public void setfObject(String fObject) {
        this.fObject = fObject;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Stacktrace> getStacktraces() {
        return stacktraces;
    }

    public void setStacktraces(List<Stacktrace> stacktraces) {
        this.stacktraces = stacktraces;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public boolean isObjectTracked() {
        return objectTracked;
    }

    public void setObjectTracked(boolean objectTracked) {
        this.objectTracked = objectTracked;
    }

    public String getfReturn() {
        return fReturn;
    }

    public void setfReturn(String fReturn) {
        this.fReturn = fReturn;
    }

    public boolean isReturnTracked() {
        return returnTracked;
    }

    public void setReturnTracked(boolean returnTracked) {
        this.returnTracked = returnTracked;
    }

    public int getLastCustomFrame() {
        return lastCustomFrame;
    }

    public void setLastCustomFrame(int lastCustomFrame) {
        this.lastCustomFrame = lastCustomFrame;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
