package com.aeob.springmigrate.dependency.config;

/**
 * Rule for a single dependency in migration
 */
public class DependencyRule {

    private String status;          // REMOVED, REPLACED, NEEDS_UPDATE, COMPATIBLE
    private String minVersion;      // Minimum compatible version (for NEEDS_UPDATE)
    private ReplacementInfo replacement;  // Replacement dependency (for REPLACED)
    private String notes;           // Human-readable notes

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public ReplacementInfo getReplacement() {
        return replacement;
    }

    public void setReplacement(ReplacementInfo replacement) {
        this.replacement = replacement;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRemoved() {
        return "REMOVED".equals(status);
    }

    public boolean isReplaced() {
        return "REPLACED".equals(status);
    }

    public boolean needsUpdate() {
        return "NEEDS_UPDATE".equals(status);
    }
}

