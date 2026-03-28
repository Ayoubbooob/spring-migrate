# Security Policy

## Supported Versions

| Version | Supported |
|---|---|
| 1.x (latest) | ✅ |

## Reporting a Vulnerability

If you discover a security vulnerability in spring-migrate, please report it responsibly.

**Do NOT open a public GitHub issue for security vulnerabilities.**

Instead, please report via GitHub's private vulnerability reporting:

1. Go to the [Security tab](https://github.com/Ayoubbooob/spring-migrate/security) of this repository
2. Click **"Report a vulnerability"**
3. Provide details about the vulnerability

### What to include

- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if any)

### Response timeline

- **Acknowledgment**: Within 48 hours
- **Assessment**: Within 1 week
- **Fix**: As soon as possible, depending on severity

## Scope

spring-migrate is a CLI analysis tool that reads `pom.xml` files. It does not:
- Execute arbitrary code from analyzed projects
- Connect to external services
- Store or transmit user data

Security concerns are most likely related to:
- Malicious XML parsing (XXE attacks)
- Path traversal in file resolution
- Dependency vulnerabilities in the tool itself

