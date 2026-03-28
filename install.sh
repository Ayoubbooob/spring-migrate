#!/bin/bash

# Install script for spring-migrate CLI
# Creates a symlink in /usr/local/bin so you can run 'spring-migrate' from anywhere

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_PATH="$SCRIPT_DIR/target/spring-migrate-1.0-SNAPSHOT.jar"
WRAPPER_PATH="$SCRIPT_DIR/spring-migrate"
INSTALL_PATH="/usr/local/bin/spring-migrate"

echo "🍃 Installing spring-migrate CLI..."

# Check if JAR exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR not found. Building project first..."
    cd "$SCRIPT_DIR"
    mvn clean package -DskipTests -q
fi

# Create symlink
if [ -L "$INSTALL_PATH" ] || [ -f "$INSTALL_PATH" ]; then
    echo "Removing existing installation..."
    sudo rm "$INSTALL_PATH"
fi

echo "Creating symlink at $INSTALL_PATH..."
sudo ln -s "$WRAPPER_PATH" "$INSTALL_PATH"

echo ""
echo "✅ Installation complete!"
echo ""
echo "You can now run:"
echo "  spring-migrate analyze --to 3.2.2"
echo ""

