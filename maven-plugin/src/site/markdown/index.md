## Contrast Maven Plugin

The Contrast Maven Plugin helps users include one or more Contrast Security analysis features in
their Java web application Maven projects.

### Goals Overview

* [contrast:install](install-mojo.html) includes the Contrast Java agent in integration testing to
  provide Contrast Assess runtime security analysis.
* [contrast:verify](verify-mojo.html) verifies that none of the vulnerabilities found by Contrast
  Assess during integration testing violate the project's security policy (fails the build when
  violations are detected).
* [contrast:scan](scan-mojo.html) analyzes the Maven project's artifact with Contrast Scan to find
  vulnerabilities using static analysis.


### Usage

General instructions for how to use the Contrast Maven Plugin may be found on
the [usage page](usage.html). 