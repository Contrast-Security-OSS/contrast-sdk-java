package com.contrastsecurity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Reusable test mix-in for verifying that a class meets its {@link Object#equals(Object)} contract
 *
 * @param <T> type which must uphold the equals contract
 */
public interface EqualsAndHashcodeContract<T> {

  /** @return a new instance of {@link T} */
  T createValue();

  /**
   * @return a new instance of {@link T} which is not equal to the instance returned by {@link
   *     #createValue()}
   */
  T createNotEqualValue();

  @SuppressWarnings("EqualsWithItself")
  @Test
  default void value_equals_itself() {
    // does not use assertThat(value).isEqualTo(value), because it uses Objects.deepEquals which
    // short-circuits before calling equals(Object)
    final T value = createValue();
    assertThat(value.equals(value)).isTrue();
  }

  @Test
  default void value_equals_different_reference() {
    final T value = createValue();
    final T otherValue = createValue();
    assertThat(value).isEqualTo(otherValue);
  }

  @Test
  default void equal_values_have_same_hashcode() {
    final T value = createValue();
    final T otherValue = createValue();
    assertThat(value).hasSameHashCodeAs(otherValue);
  }

  /**
   * Suppresses warnings because IntelliJ's static analysis is confused and believes that {@code
   * value.equals(null)} will always be false.
   */
  @SuppressWarnings("ConstantConditions")
  @Test
  default void value_does_not_equal_null() {
    // does not use assertThat(value).isEqualTo(value), because it uses Objects.deepEquals which
    // short-circuits before calling equals(Object)
    final T value = createValue();
    assertThat(value.equals(null)).isFalse();
  }

  @Test
  default void value_does_not_equal_different_value() {
    final T value = createValue();
    final T differentValue = createNotEqualValue();
    assertThat(value).isNotEqualTo(differentValue);
    assertThat(differentValue).isNotEqualTo(value);
  }
}
