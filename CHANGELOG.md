# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2026-03-28

### Added

- **CLI**: `analyze` command with `--to` flag for target version
- **CLI**: Structured terminal output with color coding
- **CLI**: CI-friendly exit codes (0 = clean, 1 = error, 2 = issues found)
- **Detection**: Spring Boot version detection from parent POM, BOM imports, and properties
- **Detection**: Property placeholder resolution
- **Detection**: Confidence scoring (HIGH, MEDIUM, LOW)
- **Detection**: Version conflict detection across sources
- **Analysis**: Dependency extraction from `pom.xml`
- **Analysis**: Compatibility classification (COMPATIBLE, NEEDS_UPDATE, REPLACED, REMOVED, INCOMPATIBLE, UNKNOWN)
- **Analysis**: JSON-based compatibility rule database
- **Analysis**: Replacement suggestions with target artifact and version
- **Tooling**: CLI wrapper script (`spring-migrate`) for direct terminal usage
- **Tooling**: Install script for symlink setup
- **Testing**: Test projects covering parent POM, BOM, complex, and problematic dependency scenarios

