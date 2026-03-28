package com.aeob.springmigrate.cli;

import com.aeob.springmigrate.dependency.AnalysisResult;
import com.aeob.springmigrate.dependency.DependencyAnalyzer;
import com.aeob.springmigrate.dependency.DependencyIssue;
import com.aeob.springmigrate.detector.SpringBootDetectionCandidate;
import com.aeob.springmigrate.detector.SpringBootVersionDetector;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import static com.aeob.springmigrate.cli.ConsoleColors.*;

@CommandLine.Command(
        name = "analyze",
        description = "Analyze Spring Boot project for migration"
)
public class AnalyzeCommand implements Callable<Integer> {

    @CommandLine.Option(
            names = "--to",
            description = "Target Spring Boot version (example: 3.2.2)",
            required = true
    )
    private String targetVersion;

    @Override
    public Integer call() {
        // Print banner first
        Banner.print();

        SpringBootVersionDetector detector = new SpringBootVersionDetector();

        Path pomPath = Path.of("pom.xml");

        // Collect all detection candidates
        List<SpringBootDetectionCandidate> candidates =
                detector.detectAll(pomPath);

        if (candidates.isEmpty()) {
            Banner.printError("No Spring Boot version could be detected.");
            return 1;
        }

        // Choose main (authoritative) version
        SpringBootDetectionCandidate main =
                detector.chooseMain(candidates);

        // Detect conflicts
        List<SpringBootDetectionCandidate> conflicts =
                detector.detectConflicts(candidates);

        // Print report
        printReport(main, conflicts);

        // Dependency analysis - pass both current and target versions
        String currentVersion = main.getVersion();
        DependencyAnalyzer analyzer = new DependencyAnalyzer();
        AnalysisResult result = analyzer.analyze(pomPath, currentVersion, targetVersion);

        printDependencyReport(result);

        // Print footer
        Banner.printFooter();

        return conflicts.isEmpty() ? 0 : 2;
    }

    private void printReport(
            SpringBootDetectionCandidate main,
            List<SpringBootDetectionCandidate> conflicts
    ) {
        Banner.printSection("Version Detection");

        Banner.printKeyValue("Current Version", BOLD_WHITE + main.getVersion() + RESET);
        Banner.printKeyValue("Target Version", BOLD_GREEN + targetVersion + RESET);
        Banner.printKeyValue("Source", main.getSource().toString());
        Banner.printKeyValue("Confidence", main.getConfidence().toString());

        if (!conflicts.isEmpty()) {
            System.out.println();
            Banner.printWarning("Conflicting Spring Boot versions detected:");

            for (SpringBootDetectionCandidate c : conflicts) {
                if (!c.getVersion().equals(main.getVersion())) {
                    Banner.printListItem("Version " + YELLOW + c.getVersion() + RESET
                            + " (source: " + c.getSource() + ")");
                }
            }

            System.out.println();
            Banner.printError("INCONSISTENT CONFIGURATION");
            Banner.printInfo("Recommendation: Align all Spring Boot versions before migration");
        } else {
            System.out.println();
            Banner.printSuccess("CONSISTENT CONFIGURATION");
        }
    }

    private void printDependencyReport(AnalysisResult result) {
        Banner.printSection("Dependency Analysis");

        Banner.printKeyValue("Total scanned", String.valueOf(result.allDependencies().size()));

        if (result.hasIssues()) {
            System.out.println();
            Banner.printWarning("Found " + BOLD_YELLOW + result.issues().size() + RESET + YELLOW + " compatibility issues:" + RESET);
            System.out.println();

            for (DependencyIssue issue : result.issues()) {
                String statusColor = switch (issue.status()) {
                    case REMOVED, REPLACED -> RED;
                    case NEEDS_UPDATE -> YELLOW;
                    default -> WHITE;
                };

                System.out.println("    " + statusColor + "┌─ " + BOLD + issue.dependency().getCoordinates() + RESET);
                System.out.println("    " + statusColor + "│  " + RESET + DIM + "Status: " + RESET + statusColor + issue.status() + RESET);
                System.out.println("    " + statusColor + "│  " + RESET + DIM + "Action: " + RESET + issue.suggestion());
                System.out.println("    " + statusColor + "└─" + RESET);
                System.out.println();
            }
        } else {
            System.out.println();
            Banner.printSuccess("No compatibility issues detected");
        }
    }
}