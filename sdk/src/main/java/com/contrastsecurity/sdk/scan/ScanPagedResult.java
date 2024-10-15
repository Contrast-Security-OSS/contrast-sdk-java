package com.contrastsecurity.sdk.scan;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2022 - 2024 Contrast Security, Inc.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Describes a page of results from the Scan API.
 *
 * <p>This class is package-private, because this package does not yet support paging through
 * results from the Contrast Scan API; therefore, this remains an implementation detail of this
 * package.
 *
 * <p>We may add public support for paging through scan results in a future version of the Contrast
 * Java SDK.
 *
 * @param <T> the type of item in the page
 */
final class ScanPagedResult<T> {

  private final List<T> content;
  private final int totalElements;

  /**
   * @param content page contents
   * @param totalElements total number of elements matching the query (not the number of elements in
   *     this page)
   */
  ScanPagedResult(final List<T> content, final int totalElements) {
    this.content = Collections.unmodifiableList(new ArrayList<>(content));
    this.totalElements = totalElements;
  }

  /**
   * @return page contents
   */
  List<T> getContent() {
    return content;
  }

  /**
   * @return total number of elements matching the query (not the number of elements in this page)
   */
  int getTotalElements() {
    return totalElements;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ScanPagedResult<?> that = (ScanPagedResult<?>) o;
    return totalElements == that.totalElements && content.equals(that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, totalElements);
  }

  @Override
  public String toString() {
    return "ScanPagedResult{" + "content=" + content + ", totalElements=" + totalElements + '}';
  }
}
