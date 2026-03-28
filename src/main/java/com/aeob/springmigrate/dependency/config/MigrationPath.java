package com.aeob.springmigrate.dependency.config;

import java.util.Map;

/**
 * Represents a single migration path (e.g., 2.x-to-3.x)
 */
public class MigrationPath {

    private String description;
    private String javaMinVersion;
    private Map<String, DependencyRule> dependencies;
    private Map<String, String> javaxToJakarta;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJavaMinVersion() {
        return javaMinVersion;
    }

    public void setJavaMinVersion(String javaMinVersion) {
        this.javaMinVersion = javaMinVersion;
    }

    public Map<String, DependencyRule> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, DependencyRule> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getJavaxToJakarta() {
        return javaxToJakarta;
    }

    public void setJavaxToJakarta(Map<String, String> javaxToJakarta) {
        this.javaxToJakarta = javaxToJakarta;
    }

    /**
     * Get dependency rule by artifactId
     */
    public DependencyRule getRule(String artifactId) {
        if (dependencies == null) {
            return null;
        }
        return dependencies.get(artifactId);
    }
}

