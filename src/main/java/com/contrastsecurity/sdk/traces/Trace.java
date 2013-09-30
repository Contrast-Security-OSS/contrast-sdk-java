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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class containing an individual Contrast tracked application's traces from Contrast.
 * (C) Aspect Security 2013
 *
 * @author Kevin Decker, Aspect Security
 * @version 1.0
 */
@XmlRootElement(name = "trace")
@XmlAccessorType(XmlAccessType.FIELD)
public class Trace {
    @XmlElement(name = "hash")
    private String hash;
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "rule")
    private String rule;

    /**
     * Instantiation for iterative setup.
     */
    public Trace() {
    }

    /**
     * Instantiation for setup when all three required variables are known at once.
     *
     * @param hash String Hash value
     * @param id   String ID value
     * @param rule String Rule value
     */
    public Trace(String hash, String id, String rule) {
        setHash(hash);
        setId(id);
        setRule(rule);
    }

    public String getHash() {
        return this.hash;
    }

    public String getId() {
        return this.id;
    }

    public String getRule() {
        return this.rule;
    }

    /**
     * Method to set the Hash value of this Trace object.
     *
     * @param hash String Hash value
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Method to set the ID value of this Trace object.
     *
     * @param id String ID value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method to set the Rule value of this Trace object.
     *
     * @param rule String Rule value
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * Method to determine if the Trace object is sufficiently populated to represent an
     * application Trace object.
     *
     * @return boolean
     */
    public boolean isValidTrace() {
        boolean valid = true;
        if ((this.hash == null || this.hash.equals("")) ||
                (this.id == null || this.id.equals("")) ||
                (this.rule == null || this.rule.equals(""))) {
            valid = false;
        }
        return valid;
    }

    /**
     * Customized toString method to allow proper printing of an application's Trace object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "{id:" + this.id + ",hash:" + this.hash + ",rule:" + this.rule + "}";
    }
}