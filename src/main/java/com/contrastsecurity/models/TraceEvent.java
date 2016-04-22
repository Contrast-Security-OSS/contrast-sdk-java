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

/**
 * Several TraceEvents make up a vulnerability, or, "trace". They
 * represent a method invocation that Contrast monitored.
 */
public class TraceEvent {

	/**
	 * Return the timestamp of when this event occurred.
	 * @return the timestamp of when this event occurred
	 */
	public String getTimestamp() {
		return timestamp;
	}
	private String timestamp;
	
	/**
	 * Return the name of the thread on which this method invocation occurred.
	 * @return the name of the thread on which this method invocation occurred
	 */
	public String getThread() {
		return thread;
	}
    private String thread;
    
    /**
     * Return the type of event this is, e.g., Creation, P2O, Trigger, etc.
     * @return the type of event this is
     */
    public String getType() {
		return type;
	}
    private String type;
    
    /**
     * Return a proprietary bitset value for markup on any data flow evidence
     * in this event.
     * @return a proprietary bitset value for markup purposes
     */
    public String getBitset() {
		return bitset;
	}
    private String bitset;
    
    /**
     * Return the method signature of this method invocation.
     * @return the method signature of this method invocation
     */
    public String getSignature() {
		return signature;
	}
    private String signature;
    
    /**
     * The 'this' in the method invocation. In a static call, this will
     * be the string 'null'.
     * 
     * @return the String value of "this" in the event
     */
    public CodeObject getObject() {
		return object;
	}
    private CodeObject object;
    
    /**
     * The method parameters in the method invocation.
     * @return method parameters in a method invocation
     */
    public List<CodeObject> getArguments() {
		return arguments;
	}
    private List<CodeObject> arguments;
    
    /**
     * The return value of the method invocation.
     * @return
     */
    public CodeObject getReturnValue() {
		return returnValue;
	}
    private CodeObject returnValue;
    
    /**
     * The stack trace at the time of this method invocation.
     * @return stack trace at the time of this method invocation
     */
    public List<String> getStackTrace() {
		return stackTrace;
	}
    private List<String> stackTrace;
}
