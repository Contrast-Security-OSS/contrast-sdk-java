package com.contrastsecurity.http;

import com.contrastsecurity.models.JobOutcomePolicy;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;

/** Wrapper for the response object returned by Contrast */
@Getter
public class JobOutcomePolicyListResponse {

  /**
   * List of job outcome policies
   *
   * @return The list of job outcome policies
   */
  @SerializedName("policies")
  private List<JobOutcomePolicy> policies;
}
