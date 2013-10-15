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

/**
 * This object represents a single library within an {@link com.contrastsecurity.sdk.application.App}
 */
public class Library {

    /**
     * The unique hash that represents this library
     * @return
     */
    @XmlElement(name = "sha1")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    private String hash;

    /**
     * The URL where the library is located within the application.
     * @return
     */
    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    /**
     * Count of lines of code within the library
     * @return
     */
    @XmlElement(name = "lines-of-code")
    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int totalLinesOfCode) {
        this.linesOfCode = totalLinesOfCode;
    }

    private int linesOfCode;

    /**
     * The internal release date of the library
     * @return
     */
    @XmlElement(name = "internal-date", required = false)
    public Long getInternalDate() {
        return internalDate;
    }

    public void setInternalDate(Long internalDate) {
        this.internalDate = internalDate;
    }

    private Long internalDate;

    /**
     * How many classes within the application have been invoked
     * @return
     */
    @XmlElement(name = "used-class-count")
    public int getUsedClassCount() {
        return usedClassCount;
    }

    public void setUsedClassCount(int usedClassCount) {
        this.usedClassCount = usedClassCount;
    }

    private int usedClassCount;

    /**
     * Number of classes present in the library
     * @return
     */
    @XmlElement(name = "class-count")
    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    private Integer classCount;

    /**
     * External release date
     * @return
     */
    @XmlElement(name = "external-date", required = false)
    public Long getExternalDate() {
        return externalDate;
    }

    public void setExternalDate(Long externalDate) {
        this.externalDate = externalDate;
    }

    private Long externalDate;

    /**
     * Manifest information about the library
     * @return
     */
    @XmlElement(name = "manifest")
    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    private String manifest;

    /**
     * File name of the library on disk
     * @return
     */
    @XmlElement(name = "file")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;

    /**
     * Current library version
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String version;
}
