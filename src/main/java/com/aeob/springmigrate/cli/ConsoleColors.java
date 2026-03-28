package com.aeob.springmigrate.cli;

/**
 * ANSI color codes for terminal output.
 * Makes CLI output look professional and easy to read.
 */
public final class ConsoleColors {

    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bold
    public static final String BOLD = "\u001B[1m";
    public static final String BOLD_RED = "\u001B[1;31m";
    public static final String BOLD_GREEN = "\u001B[1;32m";
    public static final String BOLD_YELLOW = "\u001B[1;33m";
    public static final String BOLD_BLUE = "\u001B[1;34m";
    public static final String BOLD_CYAN = "\u001B[1;36m";
    public static final String BOLD_WHITE = "\u001B[1;37m";

    // Dim
    public static final String DIM = "\u001B[2m";

    private ConsoleColors() {
        // Utility class
    }

    // Helper methods for common patterns
    public static String success(String text) {
        return GREEN + text + RESET;
    }

    public static String error(String text) {
        return RED + text + RESET;
    }

    public static String warning(String text) {
        return YELLOW + text + RESET;
    }

    public static String info(String text) {
        return CYAN + text + RESET;
    }

    public static String bold(String text) {
        return BOLD + text + RESET;
    }

    public static String dim(String text) {
        return DIM + text + RESET;
    }
}

