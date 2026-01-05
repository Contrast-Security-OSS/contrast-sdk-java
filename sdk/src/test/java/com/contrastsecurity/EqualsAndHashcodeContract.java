package com.contrastsecurity;

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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Reusable test mix-in for verifying that a class meets its {@link Object#equals(Object)} contract
 *
 * @param <T> type which must uphold the equals contract
 */
public interface EqualsAndHashcodeContract<T> {

  /**
   * @return a new instance of {@link T}
   */
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
