package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2026 Contrast Security, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
