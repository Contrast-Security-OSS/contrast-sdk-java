package com.contrastsecurity.sdk.internal;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Static utility methods for operating on {@link List}. */
public final class Lists {

  /**
   * Static helper for creating an immutable copy of the given collection.
   *
   * @param original collection to copy
   * @param <T> type of elements in the collection
   * @return new, immutable copy
   */
  public static <T> List<T> copy(final Collection<? extends T> original) {
    Objects.requireNonNull(original);
    if (original.isEmpty()) {
      return Collections.emptyList();
    }
    if (original.size() == 1) {
      final T item = original.iterator().next();
      return Collections.singletonList(item);
    }
    final List<T> copy = new ArrayList<>(original);
    return Collections.unmodifiableList(copy);
  }

  /** static members only */
  private Lists() {}
}
