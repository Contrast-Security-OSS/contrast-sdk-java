package com.contrastsecurity.models;

import java.util.ArrayList;
import java.util.List;

public class EventResource extends EventModel {
  public static final String RED = "RED";
  public static final String CONTENT = "CONTENT";
  public static final String CODE = "CODE";
  public static final String BOLD = "BOLD";
  public static final String CUSTOM_CODE = "CUSTOM_CODE";
  public static final String CUSTOM_RED = "CUSTOM_RED";

  // JSON Fields
  private String id;
  private boolean important;
  private String type;
  private String description;
  private int dupes;
  private String extraDetails;

  private List<EventResource> collapsedEvents;

  // Internal use
  private Event event;
  private EventItem[] items;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean getImportant() {
    return this.important;
  }

  public void setImportant(boolean important) {
    this.important = important;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getDupes() {
    return dupes;
  }

  public void setDupes(int dupes) {
    this.dupes = dupes;
  }

  public String getExtraDetails() {
    return extraDetails;
  }

  public void setExtraDetails(String extraDetails) {
    this.extraDetails = extraDetails;
  }

  public List<EventResource> getCollapsedEvents() {
    return collapsedEvents;
  }

  public void setCollapsedEvents(List<EventResource> collapsedEvents) {
    this.collapsedEvents = collapsedEvents;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    EventResource other = (EventResource) obj;
    if (id != other.id) return false;
    return true;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public EventItem[] getItems() {
    if (items == null) {
      if (event != null) {
        List<EventItem> eventItems = new ArrayList<>();
        EventItem eventItem = new EventItem(this, BOLD, "Class.Method", false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, CONTENT, event.getClazz() + '.' + event.getMethod(), false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, BOLD, "Object", false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, CONTENT, event.getfObject(), false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, BOLD, "Return", false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, CONTENT, event.getfReturn(), false);
        eventItems.add(eventItem);
        eventItem = new EventItem(this, BOLD, "Parameters", false);
        eventItems.add(eventItem);
        if (event.getParameters() != null) {
          for (Parameter paremeter : event.getParameters())
            if (paremeter.getParameter() != null) {
              eventItem = new EventItem(this, CONTENT, paremeter.getParameter(), false);
              eventItems.add(eventItem);
            }
        }
        eventItem = new EventItem(this, BOLD, "Stack Trace", false);
        eventItems.add(eventItem);
        if (event.getStacktraces() != null) {
          boolean first = true;
          for (Stacktrace stacktrace : event.getStacktraces()) {
            if (first) {
              if (stacktrace.getType().equals("custom")) {
                eventItem = new EventItem(this, CUSTOM_RED, stacktrace.getDescription(), true);
              } else {
                eventItem = new EventItem(this, RED, stacktrace.getDescription(), true);
              }
              eventItems.add(eventItem);
              first = false;
            } else {
              if (stacktrace.getType().equals("custom")) {
                eventItem = new EventItem(this, CUSTOM_CODE, stacktrace.getDescription(), true);
              } else {
                eventItem = new EventItem(this, CODE, stacktrace.getDescription(), true);
              }
              eventItems.add(eventItem);
              first = false;
            }
          }
        }
        items = eventItems.toArray(new EventItem[0]);
      }
    }
    return items;
  }

  @Override
  public String toString() {
    return description;
  }
}
