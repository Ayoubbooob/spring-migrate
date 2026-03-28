package com.aeob.springmigrate.detector;

public class SpringBootDetectionCandidate {

    private final String version;
    private final SpringBootVersionSource source;
    private final SpringBootVersionInfo.Confidence confidence;

    public SpringBootDetectionCandidate(
            String version,
            SpringBootVersionSource source,
            SpringBootVersionInfo.Confidence confidence
    ) {
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

    public SpringBootVersionInfo.Confidence getConfidence() {
        return confidence;
    }
}
