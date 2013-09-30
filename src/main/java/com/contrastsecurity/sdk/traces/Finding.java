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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement(name = "finding")
public class Finding {
    private String findingPlatform;
    private String findingRuleID;
    private Events events;
    private TraceRequestData requestData;

    public Finding() {
    }

    @XmlAttribute(name = "platform")
    public String getFindingPlatform() {
        return this.findingPlatform;
    }

    @XmlAttribute(name = "ruleId")
    public String getFindingRuleID() {
        return this.findingRuleID;
    }

    @XmlElement(name = "events")
    public Events getEvents() {
        return this.events;
    }

    @XmlElement(name = "request")
    public TraceRequestData getRequestData() {
        return this.requestData;
    }

    /**
     * Retrieve the Request Headers as a HashMap.
     *
     * @return HashMap&lt;String,String&gt;
     */
    public HashMap<String, String> getRequestHeadersMap() {
        return this.requestData.getRequestHeadersMap();
    }

    /**
     * Retrieve the Request Parameters as a HashMap.
     *
     * @return HashMap&lt;String,String&gt;
     */
    public HashMap<String, String> getRequestParametersMap() {
        return this.requestData.getRequestParametersMap();
    }

    /**
     * Retrieve the Request data as a properly formed HTTP Request in String format.
     *
     * @return String
     */
    public String getRequestString() {
        return this.requestData.getRequestString();
    }


    public void setFindingPlatform(String findingPlatform) {
        this.findingPlatform = findingPlatform;
    }

    public void setFindingRuleID(String findingRuleId) {
        this.findingRuleID = findingRuleId;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public void setRequestData(TraceRequestData requestData) {
        this.requestData = requestData;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder thisObj = new StringBuilder();

        thisObj.append("{\nMethod-Event:{\n");
        for (Event te : this.events.getMethodEvents()) {
            thisObj.append("{\n" + te.toString() + "}\n");
        }
        thisObj.append("}\n");
        thisObj.append("Propagation-Event:{\n");
        for (Event te : this.events.getPropagationEvents()) {
            thisObj.append("{\n" + te.toString() + "}\n");
        }
        thisObj.append("}\n");
        thisObj.append("Tag-Event:{\n");
        for (Event te : this.events.getTagEvents()) {
            thisObj.append("{\n" + te.toString() + "}\n");
        }
        thisObj.append("}\n");
        thisObj.append("Request:{\n");
        thisObj.append(requestData.toString());
        thisObj.append("}");

        return thisObj.toString();
    }
}
