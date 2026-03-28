package com.aeob.springmigrate.detector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class SpringBootVersionDetector {

    // We use detectAll instead, to resolve conflicts
    public SpringBootVersionInfo detect(Path pomPath) {
        Document document = parsePom(pomPath);

        // Step 1: try parent
        SpringBootVersionInfo fromParent = detectFromParent(document);
        if (fromParent != null) {
            return fromParent;
        }

        // property
        //TODO -- fix later
//        SpringBootVersionInfo fromProperty = detectFromProperty(document);
//        if(fromProperty != null){
//            return fromProperty;
//        }

        // BOM

        SpringBootVersionInfo fromBom = detectFromBom(document);
        if(fromBom != null){
            return fromBom;
        }
        return new SpringBootVersionInfo(
                null,
                SpringBootVersionSource.UNKNOWN,
                SpringBootVersionInfo.Confidence.LOW
        );
    }


    private SpringBootVersionInfo detectFromParent(Document document) {
        NodeList parents = document.getElementsByTagName("parent");

        if (parents.getLength() == 0) {
            return null;
        }

        Element parent = (Element) parents.item(0);

        String groupId = getChildText(parent, "groupId");
        String artifactId = getChildText(parent, "artifactId");
        String version = getChildText(parent, "version");

        if ("org.springframework.boot".equals(groupId)
                && "spring-boot-starter-parent".equals(artifactId)
                && version != null && (!version.startsWith("${") && !version.endsWith("}"))){

            return new SpringBootVersionInfo(
                    version,
                    SpringBootVersionSource.PARENT,
                    SpringBootVersionInfo.Confidence.HIGH
            );
        }

        return null;
    }

    private SpringBootVersionInfo detectFromBom(Document document) {
        NodeList dependencyManagementList =
                document.getElementsByTagName("dependencyManagement");

        if (dependencyManagementList.getLength() == 0) return null;

        Element dependencyManagement =
                (Element) dependencyManagementList.item(0);

        NodeList dependencies =
                dependencyManagement.getElementsByTagName("dependency");

        for (int i = 0; i < dependencies.getLength(); i++) {
            Element dependency = (Element) dependencies.item(i);

            String groupId = getChildText(dependency, "groupId");
            String artifactId = getChildText(dependency, "artifactId");
            String version = getChildText(dependency, "version");
            String type = getChildText(dependency, "type");
            String scope = getChildText(dependency, "scope");

            if (!"org.springframework.boot".equals(groupId)) continue;
            if (!"spring-boot-dependencies".equals(artifactId)) continue;
            if (!"pom".equals(type)) continue;
            if (!"import".equals(scope)) continue;
            if (version == null) continue;

            version = version.trim();

            String extractedVersion = extractVersion(version, document);

            return new SpringBootVersionInfo(
                    extractedVersion,
                    SpringBootVersionSource.BOM,
                    SpringBootVersionInfo.Confidence.HIGH
            );

        }

        return null;
    }



    private SpringBootVersionInfo detectFromDependencies(Document document) {
        NodeList dependenciesList = document.getElementsByTagName("dependency");
        if (dependenciesList.getLength() == 0) return null;

        for (int i = 0; i < dependenciesList.getLength(); i++) {
            Element dependency = (Element) dependenciesList.item(i);

            String groupId = getChildText(dependency, "groupId");
            String artifactId = getChildText(dependency, "artifactId");
            String version = getChildText(dependency, "version");

            if (groupId == null || artifactId == null) continue;

            groupId = groupId.trim();
            artifactId = artifactId.trim();

            if (!"org.springframework.boot".equals(groupId)) continue;
            if (!artifactId.startsWith("spring-boot-starter")) continue;

            if (version == null) continue;

            version = version.trim();

            String extractedVersion = extractVersion(version, document);

            return new SpringBootVersionInfo(
                    extractedVersion,
                    SpringBootVersionSource.DEPENDENCY,
                    SpringBootVersionInfo.Confidence.LOW
            );
        }

        return null;
    }

    private SpringBootVersionInfo detectFromProperty(Document document) {
        NodeList parents = document.getElementsByTagName("parent");
        if (parents.getLength() == 0) return null;

        Element parent = (Element) parents.item(0);
        String groupId = getChildText(parent, "groupId");
        String artifactId = getChildText(parent, "artifactId");
        String version = getChildText(parent, "version");

        if (groupId == null || artifactId == null || version == null) return null;

        groupId = groupId.trim();
        artifactId = artifactId.trim();
        version = version.trim();

        // Only handle Spring Boot starter parent with property version
        if ("org.springframework.boot".equals(groupId)
                && "spring-boot-starter-parent".equals(artifactId)
                && version.startsWith("${")
                && version.endsWith("}")) {

            // Remove ${ and } and trim inside braces to handle spaces
            String propertyName = version.substring(2, version.length() - 1).trim();

            NodeList propertiesList = document.getElementsByTagName("properties");
            if (propertiesList.getLength() == 0) return null;

            Element properties = (Element) propertiesList.item(0);

            String springBootVersion = getChildText(properties, propertyName);
            if (springBootVersion != null) {
                springBootVersion = springBootVersion.trim(); // remove spaces in value
                return new SpringBootVersionInfo(
                        springBootVersion,
                        SpringBootVersionSource.PROPERTY,
                        SpringBootVersionInfo.Confidence.HIGH
                );
            }
        }

        return null;
    }

    public List<SpringBootDetectionCandidate> detectAll(Path pomPath) {
        Document document = parsePom(pomPath);
        List<SpringBootDetectionCandidate> candidates = new ArrayList<>();

        SpringBootVersionInfo parent = detectFromParent(document);
        if (parent != null) {
            candidates.add(new SpringBootDetectionCandidate(
                    parent.getVersion(),
                    parent.getSource(),
                    parent.getConfidence()
            ));
        }

        SpringBootVersionInfo bom = detectFromBom(document);
        if (bom != null) {
            candidates.add(new SpringBootDetectionCandidate(
                    bom.getVersion(),
                    bom.getSource(),
                    bom.getConfidence()
            ));
        }

        SpringBootVersionInfo dependency = detectFromDependencies(document);
        if (dependency != null) {
            candidates.add(new SpringBootDetectionCandidate(
                    dependency.getVersion(),
                    dependency.getSource(),
                    dependency.getConfidence()
            ));
        }

        return candidates;
    }

    public List<SpringBootDetectionCandidate> detectConflicts(
            List<SpringBootDetectionCandidate> candidates
    ) {
        Map<String, List<SpringBootDetectionCandidate>> byVersion =
                candidates.stream()
                        .collect(Collectors.groupingBy(
                                SpringBootDetectionCandidate::getVersion
                        ));

        if (byVersion.size() <= 1) {
            return List.of(); // no conflicts
        }

        // Flatten all candidates that are part of conflicts
        return candidates;
    }

    public SpringBootDetectionCandidate chooseMain(
            List<SpringBootDetectionCandidate> candidates
    ) {
        return candidates.stream().min(Comparator.comparing(
                        SpringBootDetectionCandidate::getSource,
                        Comparator.comparingInt(this::priorityOf)
                ))
                .orElse(null);
    }

    private int priorityOf(SpringBootVersionSource source) {
        return switch (source) {
            case PARENT -> 1;
            case BOM -> 2;
            case DEPENDENCY -> 3;
            default -> 99;
        };
    }


    private String extractVersion(String version, Document document) {
        // Case 1: literal version
        if (!version.startsWith("${")) {
            return version;
        }
        // Case 2: property-based version
        String propertyName =
                version.substring(2, version.length() - 1).trim();

        NodeList propertiesList =
                document.getElementsByTagName("properties");

        if (propertiesList.getLength() == 0) return null;

        Element properties =
                (Element) propertiesList.item(0);

        return getChildText(properties, propertyName);
    }

    private Document parsePom(Path pomPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(pomPath.toFile());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse pom.xml", e);
        }
    }

    private String getChildText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        return nodes.item(0).getTextContent().trim();
    }

}