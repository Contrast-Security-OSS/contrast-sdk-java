package com.contrastsecurity.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.CacheableTask;

/**
 * Task for verifying test results for its test. There is one of these tasks created for each Test
 * task in the project. This task is meant to take in it's corresponding Test task's output, use the
 * SDK to parse the TS results for the given task, then output a file corresponding to the results
 * found from TS.
 *
 * <p>TODO: <a href="https://contrast.atlassian.net/browse/JAVA-3779">Implementation Ticket</a>
 */
@CacheableTask
public abstract class ContrastVerifyTestTask extends DefaultTask {}
