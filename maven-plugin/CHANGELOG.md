# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html)

## [2.13.2] - 2022-01-24
### Changed
- `install` and `verify` goals no longer require the `serverName` configuration parameter. The `serverName` configuration parameter can add stability to the `verify` goal for very active builds, but it is not strictly necessary nor desirable for most use cases.

## [2.13.1] - 2021-08-31
### Added
- Contrast Scan support

### Removed
- `profile` configuration which the Contrast server has not supported since before 3.7.7.
- support for JRE 1.7. Requires minimum JRE 1.8


## [2.12] - 2021-03-09
### Changed
- Tested with JDK 1.8, 11, and 15
- Targets JRE 1.7
- Maven version > 3.6.1 (Released April 2019) is required to build the plugin


## [2.0] - 2018-05-15
### Added
- Vulnerabilities now reconciled using an app version instead of a timestamp
- App version can be generated using `$TRAVIS_BUILD_NUMBER` or `$CIRCLE_BUILD_NUM`
- Source packaging changed to `com.contrastsecurity.maven.plugin`