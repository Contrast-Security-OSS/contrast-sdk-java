package com.contrastsecurity.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class ResolveAgentTask extends DefaultTask {

    @TaskAction
    void resolveAgent() {
        System.out.println("Resolved Agent");
    }
}
