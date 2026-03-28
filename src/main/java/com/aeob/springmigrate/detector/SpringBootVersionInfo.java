package com.aeob.springmigrate.detector;

public class SpringBootVersionInfo {
    private final String version;
    private final SpringBootVersionSource source;
    private final Confidence confidence;

    public SpringBootVersionInfo(String version,
                                 SpringBootVersionSource source,
                                 Confidence confidence) {
        this.version = version;
        this.source = source;
        this.confidence = confidence;
    }

    public String getVersion() {
        return version;
    }

    public SpringBootVersionSource getSource() {
        return source;
    }

    public Confidence getConfidence() {
        return confidence;
    }

    public enum Confidence {
        HIGH,
        MEDIUM,
        LOW
    }
}
