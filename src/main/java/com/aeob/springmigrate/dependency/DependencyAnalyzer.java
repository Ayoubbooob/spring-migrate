package com.aeob.springmigrate.dependency;

import com.aeob.springmigrate.dependency.config.DependencyRule;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DependencyAnalyzer {

    private final DependencyExtractor extractor = new DependencyExtractor();
    private final CompatibilityDatabase database = new CompatibilityDatabase();

    /**
     * Analyze dependencies for migration compatibility.
     *
     * @param pomPath       Path to pom.xml
     * @param fromVersion   Current Spring Boot version
     * @param targetVersion Target Spring Boot version
     * @return Analysis result with all dependencies and issues
     */
    public AnalysisResult analyze(Path pomPath, String fromVersion, String targetVersion) {
        List<Dependency> dependencies = extractor.extractDependencies(pomPath);
        List<DependencyIssue> issues = new ArrayList<>();

        for (Dependency dep : dependencies) {
            CompatibilityStatus status = database.checkCompatibility(dep, fromVersion, targetVersion);

            if (status != CompatibilityStatus.COMPATIBLE) {
                String suggestion = getSuggestion(dep, status, fromVersion, targetVersion);
                issues.add(new DependencyIssue(dep, status, suggestion));
            }
        }

        return new AnalysisResult(dependencies, issues);
    }

    /**
     * Generate helpful suggestion based on the dependency rule.
     */
    private String getSuggestion(Dependency dep, CompatibilityStatus status,
                                  String fromVersion, String targetVersion) {
        DependencyRule rule = database.getRule(dep.artifactId(), fromVersion, targetVersion);

        if (rule != null && rule.getNotes() != null) {
            // Use notes from JSON if available
            String suggestion = rule.getNotes();

            // Add replacement info if available
            if (rule.getReplacement() != null) {
                suggestion += " Replace with: " + rule.getReplacement().getCoordinates();
            }

            // Add min version info if available
            if (rule.getMinVersion() != null && status == CompatibilityStatus.NEEDS_UPDATE) {
                suggestion += " Minimum version: " + rule.getMinVersion();
            }

            return suggestion;
        }

        // Fallback to generic suggestions
        return switch (status) {
            case REMOVED -> "This library is no longer maintained. Find an alternative.";
            case REPLACED -> "This dependency has been replaced. Check migration guide.";
            case NEEDS_UPDATE -> "Update to a compatible version.";
            default -> "Review manually.";
        };
    }
}