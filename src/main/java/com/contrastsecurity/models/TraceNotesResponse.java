package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TraceNotesResponse {

  public List<TraceNoteResource> getNotes() {
    return notes;
  }

  @SerializedName("notes")
  private List<TraceNoteResource> notes;
}
