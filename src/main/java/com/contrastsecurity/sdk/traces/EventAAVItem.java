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
import javax.xml.bind.annotation.XmlValue;

public class EventAAVItem {
    private String hashCode;
    private String tracked;
    private String value;

    public EventAAVItem() {
    }

    @XmlAttribute(name = "hashCode")
    public String getHashCode() {
        return this.hashCode;
    }

    @XmlAttribute(name = "tracked")
    public String getTracked() {
        return this.tracked;
    }

    @XmlValue
    public String getValue() {
        return this.value;
    }

    /**
     * Method to retrieve the Base64 decoded String of the VALUE of the XML Element.
     *
     * @return String
     */
    public String getDecodedValue() {
        return new String(Base64.decodeBase64(value));
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public void setTracked(String tracked) {
        this.tracked = tracked;
    }

    /**
     * Method to set the Base64 encoded String of the VALUE of the XML Element.
     * **Should not be set to non-Base64 encoded String value.
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder thisObj = new StringBuilder();

        thisObj.append("[hashCode:" + this.hashCode + ",tracked:" + this.tracked + "]:" + getDecodedValue());

        return thisObj.toString();
    }
}
