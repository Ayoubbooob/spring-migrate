package com.aeob.springmigrate.dependency;

public enum CompatibilityStatus {
    COMPATIBLE,      // Works as-is
    NEEDS_UPDATE,    // Update version required
    REMOVED,         // Library no longer maintained
    REPLACED,        // Must use different library (javax → jakarta)
    UNKNOWN          // Not in our database
}