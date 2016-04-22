/*
 * Copyright (c) 2014, Contrast Security, LLC.
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
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
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
package com.contrastsecurity.models;

import java.util.List;

import com.contrastsecurity.rest.PlatformVulnerability;
import com.google.gson.annotations.SerializedName;

public class Application {
    
	/**
	 * The ID of this application, which is a long, alphanumeric token.
	 * 
	 * @return the ID of this application
	 */
	public String getId() {
        return this.id;
    }
	@SerializedName("app-id")
    private String id = null;
    
    /**
     * Return the paid license level of the application.
     * 
     * @return the license level of the applied; one of Enterprise, Business, Pro, Trial 
     */
	public String getLicense() {
        return this.license;
    }
    private String license = null;
	
    /**
     * Return the path of the web application, e.g., /AcmeApp
     * 
     * @return the base path of the application, like /AcmeApp
     */
    public String getPath() {
        return this.path;
    }
    private String path = null;
    
    /**
     * Return the human-readable name of the web application. Note that this method will
     * return "ROOT" for apps that run at the root of the app when Contrast can't find
     * a human-readable representation.
     * 
     * @return the human-readable name of the web application
     */
    public String getName() {
        return this.name;
    }
    private String name = null;
    
    public int getViews() {
    	return this.views;
    }
    private int views = 0;
    
    /**
     * Return the language of the application, e.g., Java.
     * 
     * @return the language the application is written in
     */
    public String getLanguage() {
		return language;
	}
    private String language;
    
    /**
     * Return a list of technologies the app is using, e.g., Spring, Applet, JSF, Flash, etc.
     * 
     * @return the list of technologies the app is using
     */
    public List<String> getTechnologies() {
		return technologies;
	}
    private List<String> technologies;
    
    /**
     * Return the time the application was last monitored by Contrast.
     * 
     * @return the epoch time when the last report was received for this application
     */
    public long getLastSeen() {
    	return lastSeen;
    }
    @SerializedName("last-seen")
    private long lastSeen;
    
    public String getPlatformVersion() {
		return platformVersion;
	}
    @SerializedName("platform-version")
    private String platformVersion;
    
    public List<PlatformVulnerability> getPlatformVulnerabilities() {
		return platformVulnerabilities;
	}
    @SerializedName("platform-vulnerabilities")
    private List<PlatformVulnerability> platformVulnerabilities;
}
