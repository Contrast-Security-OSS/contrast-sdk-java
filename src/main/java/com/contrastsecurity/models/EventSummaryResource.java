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

import java.util.List;

public class EventSummaryResource {
	private boolean success;
	private List<String> messages;
	private String risk;
	private boolean showEvidence;
	private boolean showEvents;
	private List<EventResource> events;

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getMessages() {
		return this.messages;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getRisk() {
		return this.risk;
	}

	public void setShowEvidence(boolean showEvidence) {
		this.showEvidence = showEvidence;
	}

	public boolean getShowEvidence() {
		return this.showEvidence;
	}

	public void setShowEvents(boolean showEvents) {
		this.showEvents = showEvents;
	}

	public boolean getShowEvents() {
		return this.showEvents;
	}

	public void setEvents(List<EventResource> events) {
		this.events = events;
	}

	public List<EventResource> getEvents() {
		return this.events;
	}
}
