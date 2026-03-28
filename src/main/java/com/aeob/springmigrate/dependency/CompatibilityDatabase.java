package com.aeob.springmigrate.dependency;

import com.aeob.springmigrate.dependency.config.DependencyRule;
import com.aeob.springmigrate.dependency.config.MigrationConfigLoader;
import com.aeob.springmigrate.dependency.config.MigrationPath;

/**
 * Checks dependency compatibility using JSON-based configuration.
 * Supports multiple migration paths (2.x→3.x, 3.x→4.x, etc.)
 */
public class CompatibilityDatabase {

    /**
     * Check compatibility of a dependency for the given migration.
     *
     * @param dep           The dependency to check
     * @param fromVersion   Source Spring Boot version
     * @param toVersion     Target Spring Boot version
     * @return Compatibility status
     */
    public CompatibilityStatus checkCompatibility(Dependency dep, String fromVersion, String toVersion) {
        MigrationPath path = MigrationConfigLoader.getMigrationPath(fromVersion, toVersion);

        if (path == null) {
            // No specific rules for this migration path - assume compatible
            return CompatibilityStatus.COMPATIBLE;
        }

        DependencyRule rule = path.getRule(dep.artifactId());

        if (rule == null) {
            // No specific rule for this dependency - assume compatible
            return CompatibilityStatus.COMPATIBLE;
        }

        return switch (rule.getStatus()) {
            case "REMOVED" -> CompatibilityStatus.REMOVED;
            case "REPLACED" -> CompatibilityStatus.REPLACED;
            case "NEEDS_UPDATE" -> checkVersionCompatibility(dep, rule);
            default -> CompatibilityStatus.COMPATIBLE;
        };
    }

    /**
     * Check if current version meets minimum required version.
     */
    private CompatibilityStatus checkVersionCompatibility(Dependency dep, DependencyRule rule) {
        String minVersion = rule.getMinVersion();
        String currentVersion = dep.version();

        if (currentVersion == null || currentVersion.isEmpty()) {
            // Version not specified - needs update to be safe
            return CompatibilityStatus.NEEDS_UPDATE;
        }

        if (minVersion == null || minVersion.isEmpty()) {
            return CompatibilityStatus.COMPATIBLE;
        }

        // Compare versions
        if (isVersionSufficient(currentVersion, minVersion)) {
            return CompatibilityStatus.COMPATIBLE;
        }

        return CompatibilityStatus.NEEDS_UPDATE;
    }

    /**
     * Simple version comparison.
     * Returns true if current >= minimum.
     */
    private boolean isVersionSufficient(String current, String minimum) {
        // Clean versions (remove qualifiers for comparison)
        String cleanCurrent = extractNumericVersion(current);
        String cleanMinimum = extractNumericVersion(minimum);

        String[] currentParts = cleanCurrent.split("\\.");
        String[] minimumParts = cleanMinimum.split("\\.");

        int maxLength = Math.max(currentParts.length, minimumParts.length);

        for (int i = 0; i < maxLength; i++) {
            int currentPart = i < currentParts.length ? parseVersionPart(currentParts[i]) : 0;
            int minimumPart = i < minimumParts.length ? parseVersionPart(minimumParts[i]) : 0;

            if (currentPart > minimumPart) {
                return true;
            }
            if (currentPart < minimumPart) {
                return false;
            }
        }

        return true; // Equal versions
    }

    /**
     * Extract numeric part of version (e.g., "1.18.30" from "1.18.30-SNAPSHOT")
     */
    private String extractNumericVersion(String version) {
        if (version == null) return "0";
        // Remove common suffixes
        return version
                .replace("-SNAPSHOT", "")
                .replace(".Final", "")
                .replace(".RELEASE", "")
                .replaceAll("-.*$", ""); // Remove anything after dash
    }

    private int parseVersionPart(String part) {
        try {
            // Extract only digits
            String digits = part.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? 0 : Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get the rule for a specific dependency.
     * Useful for getting replacement info or notes.
     */
    public DependencyRule getRule(String artifactId, String fromVersion, String toVersion) {
        MigrationPath path = MigrationConfigLoader.getMigrationPath(fromVersion, toVersion);
        if (path == null) {
            return null;
        }
        return path.getRule(artifactId);
    }
}