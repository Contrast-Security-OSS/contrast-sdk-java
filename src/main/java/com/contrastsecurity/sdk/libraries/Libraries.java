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

package com.contrastsecurity.sdk.libraries;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Object representing the high level statistics and information about the {@link Library}
 * in use by an {@link com.contrastsecurity.sdk.application.App}
 */
@XmlRootElement(name = "libraries")
public class Libraries {
    private int total;
    private int stale;
    private int unknown;
    private List<Library> libraries;

    public Libraries() {
    }

    /**
     * Total number of libraries in use by the application.
     * @return
     */
    @XmlElement(name = "total")
    public int getTotal() {
        return this.total;
    }

    /**
     * Count of libraries considered stale (out of date or behind in versions)
     * @return
     */
    @XmlElement(name = "stale")
    public int getStale() {
        return this.stale;
    }

    /**
     * Count of libraries that Contrast does not recognize as third-party or open source.
     * @return
     */
    @XmlElement(name = "unknown")
    public int getUnknown() {
        return this.unknown;
    }

    /**
     * A list of {@link Library} objects representing meta-data about each of the libraries
     * in use by the application.
     * @return
     */
    @XmlElement(name = "lib")
    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setStale(int stale) {
        this.stale = stale;
    }

    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder thisObj = new StringBuilder();

        thisObj.append("{total:" + String.valueOf(this.total) +
                ",stale:" + String.valueOf(this.stale) +
                ",unknown:" + String.valueOf(this.unknown) + "}\n");

        return thisObj.toString();
    }
}
