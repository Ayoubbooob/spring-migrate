package com.aeob.springmigrate.dependency;

public record DependencyIssue(
    Dependency dependency,
    CompatibilityStatus status,
    String suggestion
) {}