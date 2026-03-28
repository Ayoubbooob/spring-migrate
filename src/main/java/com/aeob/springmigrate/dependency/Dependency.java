package com.aeob.springmigrate.dependency;

public record Dependency(
    String groupId,
    String artifactId,
    String version
) {
    public String getCoordinates() {
        return groupId + ":" + artifactId;
    }

    public boolean isSpringBoot() {
        return groupId.startsWith("org.springframework.boot");
    }

    public boolean isSpring() {
        return groupId.startsWith("org.springframework");
    }
}