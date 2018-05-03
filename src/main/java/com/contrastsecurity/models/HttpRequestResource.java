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

public class HttpRequestResource {

	private String success;
	private List<String> messages;
	@SerializedName("http_request")
	private HttpRequestInfo httpRequestInfo;
	private String reason;
	
	public HttpRequestResource() {
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public HttpRequestInfo getHttpRequestInfo() {
		return httpRequestInfo;
	}

	public void setHttpRequestInfo(HttpRequestInfo httpRequestInfo) {
		this.httpRequestInfo = httpRequestInfo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
