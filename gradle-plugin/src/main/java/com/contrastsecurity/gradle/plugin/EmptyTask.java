package com.contrastsecurity.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/** A placeholder task for demonstrating plugin registration */
public class EmptyTask extends DefaultTask {
  @TaskAction
  void doNothing() {
    //
  }
}
