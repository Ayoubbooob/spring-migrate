package com.aeob.springmigrate;

import com.aeob.springmigrate.cli.SpringMigrateCommand;
import picocli.CommandLine;

/**
 *
 *
 */
public class App 
{
    public static void main(String[] args) {
        int exitCode = new CommandLine(new SpringMigrateCommand())
                .execute(args);
        System.exit(exitCode);
    }

}
