package com.contrastsecurity.sdk.scan;

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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/** Custom assertions for {@link Scan} */
final class ScanAssert extends AbstractAssert<ScanAssert, Scan> {

  /**
   * @param scan object to make assertions on
   * @return new {@link ScanAssert}
   */
  static ScanAssert assertThat(final Scan scan) {
    return new ScanAssert(scan);
  }

  private ScanAssert(final Scan scan) {
    super(scan, ScanAssert.class);
  }

  /**
   * Verifies that this scan has the same values as its internal representation.
   *
   * @param inner internal representation of a scan
   * @return this
   */
  public ScanAssert hasSameValuesAsInner(final ScanInner inner) {
    Assertions.assertThat(actual.id()).isEqualTo(inner.id());
    Assertions.assertThat(actual.projectId()).isEqualTo(inner.projectId());
    Assertions.assertThat(actual.organizationId()).isEqualTo(inner.organizationId());
    Assertions.assertThat(actual.status()).isEqualTo(inner.status());
    Assertions.assertThat(actual.errorMessage()).isEqualTo(inner.errorMessage());
    return this;
  }
}
