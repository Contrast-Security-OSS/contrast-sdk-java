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

import java.util.Map;

public class Recommendation {

    private String text;
    private String formattedText;
    private Map<String, String> formattedTextVariables;

    public Recommendation() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setFormattedText(String formattedText) {
        this.formattedText = formattedText;
    }

    public Map<String, String> getFormattedTextVariables() {
        return formattedTextVariables;
    }

    public void setFormattedTextVariables(Map<String, String> formattedTextVariables) {
        this.formattedTextVariables = formattedTextVariables;
    }
}
