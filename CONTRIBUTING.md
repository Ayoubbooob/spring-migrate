# Contributing to spring-migrate

Thanks for considering contributing to spring-migrate. Every contribution helps make Spring Boot migrations less painful for everyone.

---

## Ways to Contribute

### 1. Add Compatibility Rules (Easiest)

If you've hit a dependency issue during a Spring Boot migration, add a rule to:

```
src/main/resources/compatibility/migrations.json
```

This is the fastest way to make the tool more useful for everyone.

### 2. Report Edge Cases

Found a POM structure that breaks detection? A dependency that's misclassified? Open an issue with:

- The relevant part of your `pom.xml`
- What spring-migrate reported
- What the correct result should be

### 3. Submit Test Projects

Add a `pom.xml` to `test-projects/` that exposes an edge case. Real-world examples are the best way to improve accuracy.

### 4. Code Contributions

Bug fixes, new features, and improvements are welcome.

---

## Development Setup

### Prerequisites

- Java 17+
- Maven 3.8+

### Build & Run

```bash
git clone https://github.com/Ayoubbooob/spring-migrate.git
cd spring-migrate
mvn clean package -DskipTests

# Run directly
java -jar target/spring-migrate-1.0-SNAPSHOT.jar analyze --to 3.2.2

# Or install the CLI wrapper
chmod +x install.sh
./install.sh
spring-migrate analyze --to 3.2.2
```

### Run Tests

```bash
mvn test
```

---

## Pull Request Process

1. **Fork** the repository
2. **Create a feature branch** from `main`
   ```bash
   git checkout -b feat/your-feature-name
   ```
3. **Make your changes** — keep commits focused and atomic
4. **Test locally** — make sure `mvn clean package` passes
5. **Push** and open a Pull Request against `main`
6. **Fill in the PR template** — describe what and why
7. **Wait for review** — I'll respond as quickly as I can

### Branch Naming

| Prefix | Use for |
|---|---|
| `feat/` | New features |
| `fix/` | Bug fixes |
| `docs/` | Documentation changes |
| `refactor/` | Code improvements (no behavior change) |
| `test/` | Adding or improving tests |

### Commit Messages

Follow conventional commits:

```
feat: add Gradle support for dependency extraction
fix: handle missing parent POM gracefully
docs: update compatibility database format
```

---

## Code Guidelines

- **No Spring Boot** in the tool itself — this is a plain Java CLI
- Keep methods small and focused
- Add Javadoc for public APIs
- Write tests for new features
- Follow existing code style (no formatter wars)

---

## Adding Compatibility Rules

The compatibility database lives in `src/main/resources/compatibility/migrations.json`.

Each rule follows this structure:

```json
{
  "artifactId": {
    "status": "REPLACED | REMOVED | NEEDS_UPDATE | INCOMPATIBLE",
    "replacement": {
      "groupId": "new.group.id",
      "artifactId": "new-artifact-id",
      "version": "minimum.version"
    },
    "notes": "Why this change is needed"
  }
}
```

Before adding a rule:
- Verify the issue exists in the target Spring Boot version
- Include a clear `notes` field explaining the migration
- Test with `mvn clean package` to validate JSON syntax

---

## Questions?

Open an issue or start a discussion. No question is too small.

