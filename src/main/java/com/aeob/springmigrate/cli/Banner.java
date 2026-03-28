package com.aeob.springmigrate.cli;

import static com.aeob.springmigrate.cli.ConsoleColors.*;

/**
 * Banner and formatted output utilities for spring-migrate CLI.
 * Provides consistent styling across all commands.
 */
public final class Banner {

    private Banner() {
        // Utility class
    }

    /**
     * Print the main banner.
     * Call this at the start of any command.
     */
    public static void print() {
        System.out.println();
        System.out.println(BOLD_GREEN + "  ┌───────────────────────────────────────┐" + RESET);
        System.out.println(BOLD_GREEN + "  │" + RESET + BOLD_WHITE + "      🍃 SPRING-MIGRATE             " + BOLD_GREEN + "   │" + RESET);
        System.out.println(BOLD_GREEN + "  │" + RESET + CYAN + "   Spring Boot Migration Assistant   " + BOLD_GREEN + "  │" + RESET);
        System.out.println(BOLD_GREEN + "  └───────────────────────────────────────┘" + RESET);
        System.out.println();
    }

    /**
     * Print a section header.
     */
    public static void printSection(String title) {
        System.out.println();
        System.out.println(BOLD_WHITE + "━━━ " + title + " " + "━".repeat(Math.max(0, 40 - title.length())) + RESET);
    }

    /**
     * Print a key-value pair with consistent formatting.
     */
    public static void printKeyValue(String key, String value) {
        System.out.printf("  %-16s %s%n", key + ":", value);
    }

    /**
     * Print an error message.
     */
    public static void printError(String message) {
        System.out.println("  " + RED + "✗ " + message + RESET);
    }

    /**
     * Print a success message.
     */
    public static void printSuccess(String message) {
        System.out.println("  " + GREEN + "✓ " + message + RESET);
    }

    /**
     * Print a warning message.
     */
    public static void printWarning(String message) {
        System.out.println("  " + YELLOW + "⚠ " + message + RESET);
    }

    /**
     * Print an info message.
     */
    public static void printInfo(String message) {
        System.out.println("  " + CYAN + "ℹ " + message + RESET);
    }

    /**
     * Print a list item.
     */
    public static void printListItem(String item) {
        System.out.println("    " + DIM + "• " + RESET + item);
    }

    /**
     * Print a horizontal divider.
     */
    public static void printDivider() {
        System.out.println();
        System.out.println(DIM + "  " + "─".repeat(41) + RESET);
    }

    /**
     * Print the footer with version and project info.
     */
    public static void printFooter() {
        System.out.println();
        System.out.println(DIM + "  ─────────────────────────────────────────" + RESET);
        System.out.println(DIM + "  spring-migrate v1.0.0  github.com/aeob" + RESET);
        System.out.println();
    }

    /**
     * Print a box around content (for important messages).
     */
    public static void printBox(String content) {
        int width = Math.max(content.length() + 4, 30);
        String border = "─".repeat(width);

        System.out.println("  ┌" + border + "┐");
        System.out.printf("  │  %-" + (width - 2) + "s│%n", content);
        System.out.println("  └" + border + "┘");
    }
}

