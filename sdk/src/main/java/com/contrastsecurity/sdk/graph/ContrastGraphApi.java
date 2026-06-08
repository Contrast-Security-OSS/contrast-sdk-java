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

package com.contrastsecurity.sdk.graph;

import java.io.IOException;

public interface ContrastGraphApi {

  ContrastGraphResponse searchGraph(String organizationId, ContrastGraphRequest request)
      throws IOException;

  ContrastGraphResponse getIncidentGraph(String organizationId, String incidentId)
      throws IOException;

  FacetsResponse getFacets(String organizationId, String filterName, RequestFilters filters)
      throws IOException;

  ApplicationLibrariesResponse getApplicationLibraries(
      String organizationId,
      String applicationId,
      String agentReportingInstanceId,
      ApplicationLibrariesRequest request)
      throws IOException;

  LibraryDetailsResponse getApplicationLibraryDetails(
      String organizationId, String applicationId, String libraryHash) throws IOException;
}
