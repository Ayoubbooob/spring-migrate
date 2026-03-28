package com.aeob.springmigrate.dependency;

import java.util.List;

public record AnalysisResult(
    List<Dependency> allDependencies,
    List<DependencyIssue> issues
) {
    public boolean hasIssues() {
        return !issues.isEmpty();
    }
}