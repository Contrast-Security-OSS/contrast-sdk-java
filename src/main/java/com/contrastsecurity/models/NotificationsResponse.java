package com.contrastsecurity.models;

/*-
 * #%L
 * Contrast Java SDK
 * %%
 * Copyright (C) 2014 - 2022 Contrast Security, Inc.
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

public class NotificationsResponse {

  @SerializedName("success")
  protected String success;

  public String getSuccess() {
    return success;
  }
  ;

  public List<String> getMessages() {
    return messages;
  }

  @SerializedName("messages")
  private List<String> messages;

  public List<NotificationResource> getNotifications() {
    return notifications;
  }

  @SerializedName("notifications")
  private List<NotificationResource> notifications;
}
