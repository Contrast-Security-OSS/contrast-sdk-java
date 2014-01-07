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

/**
 * A source code file, like a Ruby script, ColdFusion template, etc.
 */
public class CodeFile {

	/**
	 * The path to the code file within the app root. 
	 * @return the relative path of the code file
	 */
	public String getPath() {
		return path;
	}
	@SerializedName("script-path")
	private String path;
	
	/**
	 * Return the SHA1 of the file.
	 * 
	 * @return the hexified SHA1 for this file
	 */
	public String getSha1() {
		return sha1;
	}
	private String sha1;
	
	/**
	 * Return the last modified time of this file.
	 * 
	 * @return the epoch time of the last modification to this file
	 */
	public long getLastModified() {
		return lastModified;
	}
	@SerializedName("last-modified-date")
	private long lastModified;
	
	/**
	 * Return the number of lines executed in the file.
	 * 
	 * @return the number of lines executed in the file.
	 */
	public int getLinesHit() {
		return linesHit;
	}
	@SerializedName("executed-loc")
	private int linesHit;
	
	/**
	 * Return the number of hittable lines in the file.
	 * @return the number of hittable lines in the file
	 */
	public int getHittableLines() {
		return hittableLines;
	}
	@SerializedName("executable-loc")
	private int hittableLines;
	
	/**
	 * Return the number of total lines in the file, including those that aren't executable
	 * @return the number of total lines in the file
	 */
	public int getTotalLines() {
		return totalLines;
	}
	@SerializedName("total-loc")
	private int totalLines;
	
}
