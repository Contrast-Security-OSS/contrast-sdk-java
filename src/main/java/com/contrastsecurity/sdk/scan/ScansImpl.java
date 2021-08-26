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

import com.contrastsecurity.sdk.scan.Scan.Definition;
import java.io.IOException;
import java.util.Objects;

/** Implementation of the {@link Scans} resource collection. */
final class ScansImpl implements Scans {

  /** Implementation of {@link Scans.Factory} */
  static final class Factory implements Scans.Factory {

    private final ScanClient client;

    Factory(final ScanClient client) {
      this.client = Objects.requireNonNull(client);
    }

    @Override
    public Scans create(final String projectId) {
      return new ScansImpl(client, projectId);
    }
  }

  private final ScanClient client;
  private final String projectId;

  ScansImpl(final ScanClient client, final String projectId) {
    this.client = Objects.requireNonNull(client);
    this.projectId = Objects.requireNonNull(projectId);
  }

  @Override
  public Definition define() {
    return new ScanImpl.Definition(client, projectId);
  }

  @Override
  public Scan get(final String id) throws IOException {
    final ScanInner inner = client.get(projectId, id);
    return new ScanImpl(client, inner);
  }

  @Override
  public ScanSummary summary(final String id) throws IOException {
    final ScanSummaryInner inner = client.getSummary(projectId, id);
    return new ScanSummaryImpl(inner);
  }
}
