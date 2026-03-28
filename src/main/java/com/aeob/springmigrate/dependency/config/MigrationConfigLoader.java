package com.aeob.springmigrate.dependency.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads migration configuration from JSON file.
 * Singleton pattern - config is loaded once and cached.
 */
public class MigrationConfigLoader {

    private static final String CONFIG_PATH = "/compatibility/migrations.json";
    private static MigrationConfig cachedConfig;

    private MigrationConfigLoader() {
        // Utility class
    }

    /**
     * Load migration config from resources.
     * Uses caching - loads only once.
     */
    public static MigrationConfig load() {
        if (cachedConfig != null) {
            return cachedConfig;
        }

        try (InputStream is = MigrationConfigLoader.class.getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                throw new RuntimeException("Migration config not found: " + CONFIG_PATH);
            }

            ObjectMapper mapper = new ObjectMapper();
            cachedConfig = mapper.readValue(is, MigrationConfig.class);
            return cachedConfig;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load migration config: " + e.getMessage(), e);
        }
    }

    /**
     * Get migration path for specific version transition.
     * Convenience method.
     */
    public static MigrationPath getMigrationPath(String fromVersion, String toVersion) {
        MigrationConfig config = load();
        return config.getMigrationPath(fromVersion, toVersion);
    }

    /**
     * Clear cached config (useful for testing or reloading)
     */
    public static void clearCache() {
        cachedConfig = null;
    }
}

