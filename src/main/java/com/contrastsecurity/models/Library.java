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
 * An application library.
 */
public class Library {
	
	/**
	 * Return the SHA1 hash of this library.
	 * 
	 * @return return the hexified SHA1 of this library
	 */
	public String getSha1() {
		return sha1;
	}
	private String sha1;
	
	/**
	 * Return the filename for this library.
	 * 
	 * @return the simple name of the library, like 'log4j-2.1.4.jar'.
	 */
	public String getFilename() {
		return filename;
	}
	private String filename;
	
	/**
	 * Return the version of this library according to the library authority
	 * like Maven Central or NuGet.
	 * 
	 * @return the version of this library
	 */
	public String getVersion() {
		return version;
	}
	private String version;
	
	/**
	 * Return an estimate of the number of lines of code in this library.
	 * 
	 * @return an estimated line count for this library
	 */
	public long getLinesOfCode() {
		return linesOfCode;
	}
	@SerializedName("lines-of-code")
	private long linesOfCode;
	
	/**
	 * Return the last date that an entry within this file was altered.
	 * 
	 * @return the YYYY-MM-DD of the last modification to an entry in this library
	 */
	public String getInternalDateModified() {
		return internalDateModified;
	}
	@SerializedName("internal-date")
	private String internalDateModified;
	
	/**
	 * Return the last date this file was altered.
	 * 
	 * @return the YYYY-MM-DD of the last modification to this file
	 */
	public String getExternalDateModified() {
		return externalDateModified;
	}
	@SerializedName("external-date")
	private String externalDateModified;
	
	/**
	 * Return the number of classes in this library.
	 * 
	 * @return the number of classes in this library
	 */
	public int getClassCount() {
		return classCount;
	}
	@SerializedName("class-count")
	private int classCount;
	
	/**
	 * Return the number of classes used by this library. Right now, this only
	 * returns the maximum number of classes used by any one instance of the
	 * running application. In the future, this will be changed to represent
	 * the total number of distinct classes used across all instances of the
	 * running application.
	 * 
	 * @return the maximum number of classes used in any instance of this library 
	 */
	public int getUsedClassCount() {
		return usedClassCount;
	}
	@SerializedName("used-class-count")
	private int usedClassCount;
	
	/**
	 * Return the blob of MANIFEST.MF in plaintext.
	 * 
	 * @return the plaintext MANIFEST.MF file in one String
	 */
	public String getManifest() {
		return manifest;
	}
	private String manifest;
}
