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
package com.contrastsecurity.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A vulnerability identified by Contrast.
 */
public class Trace {

	/**
	 * Return the title for this vulnerability, e.g.:
	 * "XSS on /foo.jsp in 'bar' parameter"
	 * 
	 * @return the title of this vulnerability
	 */
	public String getTitle() {
		return title;
	}
	private String title;
	
	/**
	 * Return any arbitrarily-formatted 'evidence' for this trace. Many rules
	 * won't cause any evidence to be created.
	 * 
	 * @return an arbitrarily-formatted 'evidence' for this trace, or null if none present
	 */
	public String getEvidence() {
		return evidence;
	}
	private String evidence;
	
	/**
	 * Return the language of application this trace was discovered in.
	 * @return the language of application this trace was discovered in
	 */
	public String getLanguage() {
		return language;
	}
	private String language;
	
	/**
	 * Return the status of this trace, like "Reported", "Verified", "Suspicious", etc.
	 * @return the status of this trace, like "Reported", "Verified", "Suspicious", etc
	 */
	public String getStatus() {
		return status;
	}
	private String status;
	
	/**
	 * Return the simple, numeric hash of this trace
	 * @return numeric hash of this trace
	 */
	public String getHash() {
		return hash;
	}
    private String hash;
    
    /**
     * Return the UUID for this trace.
     * @return the UUID for this trace
     */
    public String getUuid() {
		return uuid;
	}
    private String uuid;

    /**
     * Return the name of the rule that caused this trace.
     * @return the name of the rule that caused this trace
     */
    public String getRule() {
		return rule;
	}
    private String rule;
    
    /**
     * Return the HTTP request that caused this trace to occur.
     * @return the causing HTTP request
     */
    public HttpRequest getRequest() {
		return request;
	}
    private HttpRequest request;
    
    /**
     * The events that make up the vulnerability. Some traces
     * will only have an evidence field and no events.
     * 
     * @return
     */
    public List<TraceEvent> getEvents() {
		return events;
	}
    private List<TraceEvent> events;

    /**
     * Return the overall severity of this trace.
     * @return the severity
     */
    public String getSeverity() { return this.severity; }
    private String severity;

    /**
     * Return the likelihood of this trace
     * @return the likelihood
     */
    public String getLikelihood() { return this.likelihood; }
    private String likelihood;

    /**
     * Return the impact of this trace
     * @return the impact
     */
    public String getImpact() { return this.impact; }
    private String impact;

    /**
     * Return the confidence rating for this trace
     * @return the confidence
     */
    public String getConfidence() { return this.confidence; }
    private String confidence;

    /**
     * Return the First time this Trace was seen
     * @return Time this trace was first seen
     */
    public Long getFirstTimeSeen() { return this.firstTimeSeen; }
    @SerializedName("first-time-seen")
    private Long firstTimeSeen;

    /**
     * Return the Last time this Trace was seen
     * @return the time this trace was last seen
     */
    public Long getLastTimeSeen() { return this.lastTimeSeen; }
    @SerializedName("last-time-seen")
    private Long lastTimeSeen;
}
