package com.aeob.springmigrate.dependency;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DependencyExtractor {

    public List<Dependency> extractDependencies(Path pomPath) {
        List<Dependency> dependencies = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pomPath.toFile());
            doc.getDocumentElement().normalize();

            // Get all <dependency> elements under <dependencies>
            NodeList depNodes = doc.getElementsByTagName("dependency");

            for (int i = 0; i < depNodes.getLength(); i++) {
                Element depElement = (Element) depNodes.item(i);

                // Skip dependencies inside <dependencyManagement>
                if (isInsideDependencyManagement(depElement)) {
                    continue;
                }

                String groupId = getElementText(depElement, "groupId");
                String artifactId = getElementText(depElement, "artifactId");
                String version = getElementText(depElement, "version");

                if (groupId != null && artifactId != null) {
                    dependencies.add(new Dependency(groupId, artifactId, version));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse pom.xml", e);
        }

        return dependencies;
    }

    private boolean isInsideDependencyManagement(Element element) {
        var parent = element.getParentNode();
        while (parent != null) {
            if (parent.getNodeName().equals("dependencyManagement")) {
                return true;
            }
            parent = parent.getParentNode();
        }
        return false;
    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }
}