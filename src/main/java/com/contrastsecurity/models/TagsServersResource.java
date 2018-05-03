/*******************************************************************************
 * Copyright (c) 2017 Contrast Security.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License.
 *
 * The terms of the GNU GPL version 3 which accompanies this distribution
 * and is available at https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Contributors:
 *     Contrast Security - initial API and implementation
 *******************************************************************************/
package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagsServersResource {

    private List<String> links;
    private List<String> tags;
    @SerializedName("traces_id")
    private List<String> tracesId;

    public TagsServersResource(List<String> tags, List<String> tracesId) {
        this.tags = tags;
        this.tracesId = tracesId;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTracesId() {
        return tracesId;
    }

    public void setTracesId(List<String> tracesId) {
        this.tracesId = tracesId;
    }
}
