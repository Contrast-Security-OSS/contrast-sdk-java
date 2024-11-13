## Troubleshooting: Artifact Not Set

This error occurs when there is no project artifact available for the `scan` goal to analyze. This
typically indicates that the `scan` goal has been:

1. included in a module that does not produce an artifact (e.g. a module of type `pom`).
2. configured to run before the project's artifact has been built.


### Only Include in Modules that Produce Artifacts

The `scan` goal should only be included in modules that produce a build artifact (e.g. a module that
produces a `jar` or `war` file).

When configuring a [multi-module](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
build, users may erroneously include the `scan` goal in the build of a parent pom, and parent poms
do not produce build artifacts. In a multi-module project, verify that the `scan` goal is only
included in projects that produce a `war` or `jar` artifact. Reference
the [multi-module example](../examples/multi-module-projects.html).


### Configure Scan to Run After the Build Produces an Artifact

Maven typically generates an artifact during the `package` phase. By default, the `scan` goal runs
after the `package` phase during the `verify` phase.

You may have overridden the plugin's default phase so that the `scan` goal runs during an earlier
phase before the artifact has been built (e.g. the `test` phase). In this case, the `scan` goal will
not be able to find an artifact to scan. Make sure to attach the scan goal to a later phase (such as
the default `verify` phase).
