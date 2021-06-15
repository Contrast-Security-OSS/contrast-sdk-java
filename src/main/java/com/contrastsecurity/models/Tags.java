package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tags {
  private List<String> links;
  private transient List<Tag> tagsObject;

  @SerializedName("tags")
  private List<String> tagNames;

  @SerializedName("traces_id")
  private List<String> tracesId;

  public Tags() {
    this.tagsObject = new ArrayList();
  }

  public Tags(List<String> tags) {
    this.tagsObject = new ArrayList();
    tags.forEach(
        tag -> {
          this.tagsObject.add(new Tag(tag));
        });
  }

  public Tags(List<Tag> tags, List<String> tracesId) {
    this.tagsObject = tags;
    this.tracesId = tracesId;
  }

  public List<String> getLinks() {
    return links;
  }

  public void setLinks(List<String> links) {
    this.links = links;
  }

  public List<Tag> getTags() {
    return tagsObject;
  }

  public Tags setTagNamesAndGetTagObject() {
    this.tagNames = this.tagsObject.stream().map(Tag::getName).collect(Collectors.toList());
    return this;
  }

  public void setTags(List<Tag> tags) {
    this.tagsObject = tags;
  }

  public void addTag(Tag tag) {
    this.tagsObject.add(tag);
  }

  public List<String> getTracesId() {
    return tracesId;
  }

  public void setTracesId(List<String> tracesId) {
    this.tracesId = tracesId;
  }
}
