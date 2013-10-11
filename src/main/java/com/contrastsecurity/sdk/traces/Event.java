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

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Represents an event within a trace. An event can be a propagation of data, a trigger,
 */
@XmlRootElement(name = "event")
public class Event {
    private String eventTime;
    private String eventThread;
    private String eventType;
    private String bitset;
    private String signature;
    private EventAAVItem object;
    private List<EventAAVItem> args;
    private EventAAVItem returnElement;
    private List<String> stackTrace;

    public Event() {
    }

    @XmlAttribute(name = "time")
    public String getEventTime() {
        return this.eventTime;
    }

    @XmlAttribute(name = "thread")
    public String getEventThread() {
        return this.eventThread;
    }

    @XmlAttribute(name = "type")
    public String getEventType() {
        return this.eventType;
    }

    @XmlElement(name = "bitset")
    public String getBitset() {
        return this.bitset;
    }

    @XmlElement(name = "signature")
    public String getSignature() {
        return this.signature;
    }

    @XmlElement(name = "object")
    public EventAAVItem getObject() {
        return this.object;
    }

    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<EventAAVItem> getArgs() {
        return this.args;
    }

    @XmlElement(name = "return")
    public EventAAVItem getReturn() {
        return this.returnElement;
    }

    @XmlElementWrapper(name = "stack")
    @XmlElement(name = "frame")
    public List<String> getStackTrace() {
        return this.stackTrace;
    }

    /**
     * Method to retrieve the decoded BitSet that is returned by the XML.
     *
     * @return java.util.BitSet
     */
    public BitSet getBitsetDecoded() {
        BitSet newObj = null;
        try {
            ObjectInputStream myOIS = new ObjectInputStream(
                    new ByteArrayInputStream(
                            Base64.decodeBase64(this.bitset)));
            newObj = (BitSet) myOIS.readObject();
        } catch (Exception e) {
            System.err.println(e);
        }
        return newObj;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventThread(String eventThread) {
        this.eventThread = eventThread;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setBitset(String bitset) {
        this.bitset = bitset;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setObject(EventAAVItem object) {
        this.object = object;
    }

    public void setArgs(ArrayList<EventAAVItem> args) {
        this.args = args;
    }

    public void setReturn(EventAAVItem returnElement) {
        this.returnElement = returnElement;
    }

    public void setStackTrace(ArrayList<String> stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder thisObj = new StringBuilder();

        thisObj.append("\t\t[time:" + this.eventTime + ",thread:" + this.eventThread + ",type=" + this.eventType + "]\n");
        thisObj.append("\t\tbitset:" + getBitsetDecoded().toString() + ",\n");
        thisObj.append("\t\tsignature:" + this.signature + ",\n");
        thisObj.append("\t\tObject" + this.object.toString() + ",\n");
        thisObj.append("\t\tArgs:{\n");
        for (EventAAVItem teai : this.args) {
            thisObj.append("\t\t\tArg" + teai.toString() + ",");
        }
        if (thisObj.toString().endsWith(",")) {
            thisObj.deleteCharAt(thisObj.lastIndexOf(","));
            thisObj.append("\n");
        }
        thisObj.append("\t\t},\n");
        thisObj.append("\t\tReturn" + this.returnElement.toString() + ",\n");
        thisObj.append("\t\tStack-Trace: {\n");
        for (String str : this.stackTrace) {
            thisObj.append("\t\t\t" + str + "\n");
        }
        thisObj.append("\t\t}\n");

        return thisObj.toString();
    }
}
