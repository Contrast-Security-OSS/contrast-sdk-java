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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Entry point for using the Contrast REST API SDK. Make an instance of this
 * class and call methods. Easy!
 */
public class ContrastConnection {
	
	private String apiKey;
	private String serviceKey;
	private String user;
	private String restApiURL;

	/**
	 * Create a ContrastConnection object that will attempt to use the Contrast installation
	 * in the 
     *
     * @param user  Username (e.g., joe@acme.com)
     * @param serviceKey User service key
     * @param apiKey API Key
     * @param restApiURL the base Contrast API URL
	 * @throws IllegalArgumentException if the API URL is malformed
	 */
	public ContrastConnection(String user, String serviceKey, String apiKey, String restApiURL) throws IllegalArgumentException {
		this.user = user;
		this.serviceKey = serviceKey;
		this.apiKey = apiKey;
		this.restApiURL = restApiURL;
		
		validateUrl();
	}
	
	/**
	 * Create a ContrastConnection object that attempts to use the Contrast SaaS service.
	 */
	public ContrastConnection(String user, String serviceKey, String apiKey) {
		this.user = user;
		this.serviceKey = serviceKey;
		this.apiKey = apiKey;
		this.restApiURL = DEFAULT_API_URL;
	}
	
	/**
	 * Get summary information about a single app.
	 * @param appId the ID of the application
	 * @return an ApplicationSummary representing the object whose ID was passed in
	 * @throws UnauthorizedException if the Contrast account failed to authorize
	 * @throws IOException if there was a communication problem
	 */
	public Application getApplication(String appId) throws IOException, UnauthorizedException, ResourceNotFoundException {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			is = makeSimpleRequest("GET", APPLICATIONS_URL + "/" + appId);
			reader = new InputStreamReader(is);
			Application app = new Gson().fromJson(reader, Application.class);
			return app;
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * Get the list of applications being monitored by Contrast.
	 * @return a List of Application objects that are being monitored
	 * @throws UnauthorizedException if the Contrast account failed to authorize
	 * @throws IOException if there was a communication problem
	 */
	public List<Application> getApplications() throws UnauthorizedException, IOException {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			is = makeSimpleRequest("GET", APPLICATIONS_URL);
			reader = new InputStreamReader(is);
			
			Type type = new TypeToken<List<Application>>(){}.getType();
			List<Application> apps = new Gson().fromJson(reader, type);
			return apps;
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
		}
	}
	
	public Coverage getCoverage(String appId) throws IOException, UnauthorizedException {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			Type classesType = new TypeToken<List<CodeClass>>(){}.getType();
			is = makeSimpleRequest("GET", COVERAGE_URL + "/" + appId + "/classes?expand=methods");
			reader = new InputStreamReader(is);
			Coverage coverage = new Coverage();
			coverage.classes = new Gson().fromJson(reader, classesType);
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
			
			Type scriptsType = new TypeToken<List<CodeFile>>(){}.getType();
			is = makeSimpleRequest("GET", COVERAGE_URL + "/" + appId + "/scripts");
			reader = new InputStreamReader(is);
			coverage.codeFiles = new Gson().fromJson(reader, scriptsType);
			return coverage;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(reader);
		}
	}
	
	public List<Library> getLibraries(String appId) throws IOException, UnauthorizedException {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			Type libsType = new TypeToken<List<Library>>(){}.getType();
			is = makeSimpleRequest("GET", APPLICATIONS_URL + "/" + appId + "/libraries?expand=manifest,servers,cve");
			reader = new InputStreamReader(is);
			List<Library> libraries = new Gson().fromJson(reader, libsType);
			return libraries;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(reader);
		}
	}
	
	public List<Trace> getTraces(String appId) throws IOException, UnauthorizedException {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			Type libsType = new TypeToken<List<Trace>>(){}.getType();
			is = makeSimpleRequest("GET", TRACES_URL + "/" + appId);
			reader = new InputStreamReader(is);
			List<Trace> traces = new Gson().fromJson(reader, libsType);
			return traces;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(reader);
		}
	}
	
	private InputStream makeSimpleRequest(String method, String path) throws MalformedURLException, IOException, UnauthorizedException {
		String url = restApiURL + path; 
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Authorization", makeAuthorizationToken());
		connection.setRequestProperty("API-Key", apiKey);
		InputStream is = connection.getInputStream();
		int rc = connection.getResponseCode();
		if(rc >= 400 && rc < 500) {
			IOUtils.closeQuietly(is);
			throw new UnauthorizedException(rc);
		}
		return is;
	}

	private String makeAuthorizationToken() {
		String token = user + ":" + serviceKey;
		return Base64.encodeBase64String(token.getBytes());
	}

	private void validateUrl() throws IllegalArgumentException {
		URL u;
		try {
			u = new URL(restApiURL);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL");
		}
		if(!u.getProtocol().startsWith("http")) {
			throw new IllegalArgumentException("Invalid protocol");
		}
	}
	
	public static void main(String[] args) throws UnauthorizedException, IOException {
		ContrastConnection conn = new ContrastConnection("contrast_admin", "demo", "demo", "http://localhost:19080/Contrast/api");
		Gson gson = new Gson();
		System.out.println(gson.toJson(conn.getApplications()));
		System.out.println(gson.toJson(conn.getCoverage("d3efa3fb-1ef8-4a12-a904-c4abce81d08e")));
		System.out.println(gson.toJson(conn.getLibraries("d3efa3fb-1ef8-4a12-a904-c4abce81d08e")));
		System.out.println(gson.toJson(conn.getTraces("d3efa3fb-1ef8-4a12-a904-c4abce81d08e")));
	}

	private static final String TRACES_URL = "/traces";
	private static final String COVERAGE_URL = "/coverage";
	private static final String APPLICATIONS_URL = "/applications";
	private static final String DEFAULT_API_URL = "https://www.contrastsecurity.com/Contrast/api";
}
