package com.contrastsecurity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Reusable test mix-in for verifying that a class meets its {@link Object#equals(Object)} contract
 *
 * @param <T> type which must uphold the equals contract
 */
public interface EqualsContract<T> {

  /** @return a new instance of {@link T} */
  T createValue();

  /**
   * @return a new instance of {@link T} which is not equal to the instance returned by {@link
   *     #createValue()}
   */
  T createNotEqualValue();

  @Test
  default void valueEqualsItself() {
    final T value = createValue();
    assertEquals(value, value);
  }

  @Test
  default void valueEqualsDifferentReference() {
    final T value = createValue();
    final T otherValue = createValue();
    assertEquals(value, otherValue);
  }

  /**
   * Suppresses warnings because IntelliJ's static analysis is confused and believes that {@code
   * value.equals(null)} will always be false.
   */
  @SuppressWarnings({"SimplifiableJUnitAssertion", "ConstantConditions"})
  @Test
  default void valueDoesNotEqualNull() {
    final T value = createValue();
    assertFalse(value.equals(null));
  }

  @Test
  default void valueDoesNotEqualDifferentValue() {
    final T value = createValue();
    final T differentValue = createNotEqualValue();
    assertNotEquals(value, differentValue);
    assertNotEquals(differentValue, value);
  }
}
