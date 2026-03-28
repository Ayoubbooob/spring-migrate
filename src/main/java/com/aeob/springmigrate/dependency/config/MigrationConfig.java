package com.aeob.springmigrate.dependency.config;

import java.util.Map;

/**
 * Root configuration loaded from migrations.json
 */
public class MigrationConfig {

    private Map<String, MigrationPath> migrations;

    public Map<String, MigrationPath> getMigrations() {
        return migrations;
    }

    public void setMigrations(Map<String, MigrationPath> migrations) {
        this.migrations = migrations;
    }

    /**
     * Get migration path for given source and target versions.
     * Example: getMigrationPath("2.7.8", "3.2.2") returns "2.x-to-3.x" rules
     */
    public MigrationPath getMigrationPath(String fromVersion, String toVersion) {
        String pathKey = getMigrationPathKey(fromVersion, toVersion);
        return migrations.get(pathKey);
    }

    /**
     * Determine migration path key from versions.
     * "2.7.8" → "3.2.2" = "2.x-to-3.x"
     */
    public static String getMigrationPathKey(String fromVersion, String toVersion) {
        int fromMajor = extractMajorVersion(fromVersion);
        int toMajor = extractMajorVersion(toVersion);
        return fromMajor + ".x-to-" + toMajor + ".x";
    }

    private static int extractMajorVersion(String version) {
        if (version == null || version.isEmpty()) {
            return 0;
        }
        String[] parts = version.split("\\.");
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

