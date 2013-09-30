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

package com.contrastsecurity.sdk;

import com.contrastsecurity.sdk.libraries.Libraries;
import com.google.gson.Gson;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "app")
public class AppStats {
    private String coverageGrade;
    private int grade;
    private String id;
    private String letterGrade;
    private Libraries libs;
    private int methodsSeen;
    private int methodsTotal;
    private String name;
    private String securityGrade;
    private int views;
    private Vulns vulns;
    private List<String> technologies;

    @XmlElement(name = "coverageGrade")
    public String getCoverageGrade() {
        return this.coverageGrade;
    }

    @XmlElement(name = "grade")
    public int getGrade() {
        return this.grade;
    }

    @XmlElement(name = "id")
    public String getID() {
        return this.id;
    }

    @XmlElement(name = "letterGrade")
    public String getLetterGrade() {
        return this.letterGrade;
    }

    @XmlElement(name = "libraries")
    public Libraries getAppLibraries() {
        return this.libs;
    }

    @XmlElement(name = "methods-seen")
    public int getMethodsSeen() {
        return this.methodsSeen;
    }

    @XmlElement(name = "methods-total")
    public int getMethodsTotal() {
        return this.methodsTotal;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "securityGrade")
    public String getSecurityGrade() {
        return this.securityGrade;
    }

    @XmlElement(name = "views")
    public int getViews() {
        return this.views;
    }

    @XmlElement(name = "vulns")
    public Vulns getAppVulns() {
        return this.vulns;
    }

    @XmlElementWrapper(name = "techs")
    @XmlElements({
            @XmlElement(name = "t")
    })
    public List<String> getTechnologies() {
        return technologies;
    }

    public void setCoverageGrade(String coverageGrade) {
        this.coverageGrade = coverageGrade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public void setAppLibraries(Libraries libs) {
        this.libs = libs;
    }

    public void setMethodsSeen(int methodsSeen) {
        this.methodsSeen = methodsSeen;
    }

    public void setMethodsTotal(int methodsTotal) {
        this.methodsTotal = methodsTotal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setAppVulns(Vulns vulns) {
        this.vulns = vulns;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
