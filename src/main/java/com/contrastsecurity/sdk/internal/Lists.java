package com.contrastsecurity.sdk.internal;

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

  private Lists() {}
}
