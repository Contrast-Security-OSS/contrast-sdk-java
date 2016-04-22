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

import com.google.gson.annotations.SerializedName;

/**
 * A server with the Contrast agent installed.
 */
public class Server {

	/**
	 * Return the hostname of this server.
	 * 
	 * @return the hostname of this server
	 */
	public String getHostname() {
		return hostname;
	}
	private String hostname;
	
	/**
	 * Return the last time this server was restarted.
	 * 
	 * @return the epoch time of the last server restart
	 */
	public long getLastStartup() {
		return lastStartup;
	}
	@SerializedName("last-startup-message-received")
	private long lastStartup;
	
	/**
	 * Return the last time a trace was received from this server.
	 * 
	 * @return the epoch time of the last time the server received a trace
	 */
	public long getLastTraceReceived() {
		return lastTraceReceived;
	}
	@SerializedName("last-trace-received")
	private long lastTraceReceived;
	
	/**
	 * Return the last time any activity was received from this server.
	 * 
	 * @return the epoch time of the last time any activity was received from this server
	 */
	public long getLastActivity() {
		return lastActivity;
	}
	@SerializedName("last-activity")
	private long lastActivity;
	
	/**
	 * Return the path on disk of this server, e.g., /opt/tomcat6/
	 * @return the path on disk of this server, e.g., /opt/tomcat6
	 */
	public String getPath() {
		return path;
	}
	@SerializedName("server-path")
	private String path;
	
	/**
	 * Return the Contrast "server code" for the server, e.g., "jboss5".
	 * @return the code for the server, e.g., "websphere85"
	 */
	public String getType() {
		return type;
	}
	@SerializedName("server-type")
	private String type;
	
	/**
	 * Return the version of the Contrast agent that's monitoring this server.
	 * 
	 * @return the version of the Contrast agent installed on this server
	 */
	public String getAgentVersion() {
		return agentVersion;
	}
	@SerializedName("engine-version")
	private String agentVersion;
	
}
