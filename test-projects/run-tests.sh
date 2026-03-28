#!/bin/bash

# Test script for spring-migrate tool
# Run from the project root: ./test-projects/run-tests.sh

JAR_PATH="/home/aeob/Desktop/stack-projects/spring-migrate/target/spring-migrate-1.0-SNAPSHOT.jar"
TEST_BASE="/home/aeob/Desktop/stack-projects/spring-migrate/test-projects"

echo "=============================================="
echo "  SPRING-MIGRATE TOOL - TEST SUITE"
echo "=============================================="
echo ""

# Function to run test
run_test() {
    local name="$1"
    local dir="$2"
    local target_version="$3"

    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "📁 TEST: $name"
    echo "   Directory: $dir"
    echo "   Target version: $target_version"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""

    cd "$dir"
    java -jar "$JAR_PATH" analyze --to "$target_version"
    local exit_code=$?

    echo ""
    echo "Exit code: $exit_code"
    echo ""
    echo ""
}

# Test 1: Basic parent project
run_test "Basic Parent (2.7.8 → 3.2.2)" "$TEST_BASE/basic-parent" "3.2.2"

# Test 2: BOM style project
run_test "BOM Style (2.7.8 → 3.2.2)" "$TEST_BASE/bom-style" "3.2.2"

# Test 3: Problematic dependencies
run_test "Problematic Dependencies (2.7.5 → 3.2.2)" "$TEST_BASE/problematic-deps" "3.2.2"

# Test 4: Already migrated project
run_test "Already Migrated (3.2.2 → 3.3.0)" "$TEST_BASE/already-migrated" "3.3.0"

# Test 5: Complex enterprise project
run_test "Complex Enterprise (2.6.14 → 3.2.2)" "$TEST_BASE/complex-project" "3.2.2"

# Test 6: No Spring Boot
run_test "No Spring Boot (edge case)" "$TEST_BASE/no-spring-boot" "3.2.2"

echo "=============================================="
echo "  ALL TESTS COMPLETED"
echo "=============================================="

