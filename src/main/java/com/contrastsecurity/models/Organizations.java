package com.contrastsecurity.models;

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

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Organizations {
  public Long getCount() {
    return count;
  }

  private Long count = null;

  public List<Organization> getOrganizations() {
    return organizations;
  }

  private List<Organization> organizations;

  public Organization getOrganization() {
    return organization;
  }

  private Organization organization;

  public List<Organization> getOrgDisabled() {
    return orgDisabled;
  }

  @SerializedName("org_disabled")
  private List<Organization> orgDisabled;
}
