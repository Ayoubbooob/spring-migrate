# Roadmap

> Last updated: March 2026

This roadmap tracks the development of **spring-migrate** — a CLI tool for safe, informed Spring Boot migration.

Progress is organized by phase. Each phase builds on the previous one.

---

## Philosophy

Migration tools should diagnose before they operate. spring-migrate prioritizes accurate analysis and clear reporting over aggressive automation. Trust is earned by showing developers exactly what will break — before touching any code.

---

## Phase 1 — Detection Engine

> Accurately determine the current state of a Spring Boot project.

- [x] Parse `pom.xml` for Spring Boot version
- [x] Detect version from `<parent>` block (spring-boot-starter-parent)
- [x] Detect version from BOM import (spring-boot-dependencies)
- [x] Detect version from `<properties>` (spring-boot.version)
- [x] Resolve property placeholders (`${spring-boot.version}`)
- [x] Assign confidence score to detection result
- [x] Detect and warn on version conflicts across sources
- [ ] Java version detection from `maven-compiler-plugin` and `<java.version>`
- [ ] Validate Java version against target (e.g., Spring Boot 3 requires Java 17+)
- [ ] Detect build tool type (Maven vs Gradle)

---

## Phase 2 — Dependency Analysis

> Classify every dependency by migration risk against a target Spring Boot version.

- [x] Extract dependencies from `pom.xml`
- [x] Load compatibility rules from JSON database
- [x] Match dependencies against rules (groupId + artifactId)
- [x] Classify status: `COMPATIBLE`, `NEEDS_UPDATE`, `REMOVED`, `INCOMPATIBLE`, `UNKNOWN`
- [x] Report replacement suggestions for removed/incompatible dependencies
- [x] Structured console output with categorized findings
- [ ] Risk scoring system (aggregate project-level migration difficulty)
- [ ] Flag transitive dependency conflicts
- [ ] Expand rule coverage for common libraries

---

## Phase 3 — CLI & Reporting

> Make the output useful, scannable, and CI-friendly.

- [x] `analyze` command with `--to` flag for target version
- [x] Colored, structured terminal output
- [x] Exit codes for CI/CD integration (0 = clean, 1 = issues found, 2 = error)
- [ ] JSON report export (`--format json`)
- [ ] Summary-only mode (`--summary`)
- [ ] Verbose mode (`--verbose`) for full dependency details

---

## Phase 4 — Automated Migration Engine

> Apply safe, targeted code transformations using OpenRewrite.

- [ ] OpenRewrite integration as transformation backend
- [ ] `migrate` command with interactive confirmation
- [ ] Dry-run mode (`--dry-run`) — show changes without applying
- [ ] Diff preview before applying changes
- [ ] `javax.*` → `jakarta.*` namespace migration
- [ ] Deprecated API replacements
- [ ] Configuration property renames (`application.properties` / `application.yml`)
- [ ] Automatic backup before modification
- [ ] Rollback support

---

## Phase 5 — Gradle & Multi-Module Support

> Extend coverage beyond single-module Maven projects.

- [ ] Gradle build file parsing (`build.gradle`, `build.gradle.kts`)
- [ ] Gradle version catalog support
- [ ] Multi-module project detection
- [ ] Per-module analysis with aggregated report
- [ ] Cross-module version conflict detection

---

## Phase 6 — Ecosystem & Distribution

> Make the tool easy to install and extend.

- [ ] Homebrew formula
- [ ] SDKMAN support
- [ ] GraalVM native image for instant startup
- [ ] Plugin system for custom rules
- [ ] Community-contributed rule database

---

## Long-Term Vision

spring-migrate aims to become the standard pre-migration diagnostic for Spring Boot projects — the tool teams run _before_ committing to a migration timeline.

Long-term goals:

- Support migration paths across multiple major versions (2.x → 3.x, 3.x → 4.x)
- Integrate with CI pipelines as a migration readiness gate
- Provide a web dashboard for multi-service migration tracking
- Serve as the orchestration layer for OpenRewrite in Spring Boot contexts
- Maintain a living, community-driven compatibility database

---

## Contributing

The compatibility rule database (`migrations.json`) is the easiest entry point for contributions. If you've encountered a dependency that broke during a Spring Boot upgrade, consider adding a rule.
