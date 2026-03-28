package com.aeob.springmigrate.cli;

import picocli.CommandLine;

@CommandLine.Command(
        name = "spring-migrate",
        description = "Spring Boot migration tool",
        mixinStandardHelpOptions = true,
        subcommands = {
                AnalyzeCommand.class
        }
)
public class SpringMigrateCommand {
}