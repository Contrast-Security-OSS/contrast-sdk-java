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

/**
 * Represents a primitive/object in a method invocation. The parameters, "this", and return value
 * are modeled with this object.
 *
 * @deprecated because this object contains accessors for fields that can never be set. At best it's
 *     not useful and at worst it produces {@code NullPointerException}. It was drafted 7 years ago
 *     and never used.
 */
@Deprecated
public class CodeObject {

  /**
   * Return the identity hash code of this object.
   *
   * @return the identity hash code of this object
   */
  public String getHashCode() {
    return hashCode;
  }

  private String hashCode;

  /**
   * Return whether or not the object is tracked.
   *
   * @return whether or not the object is tracked
   */
  public boolean isTracked() {
    return tracked;
  }

  private boolean tracked;

  /**
   * Return the value of the object.
   *
   * @return the value of the object
   */
  public String getValue() {
    throw new NullPointerException("value is always null");
  }

  private String value;
}
