#!/bin/bash

# Clean up old class files
echo "Cleaning up..."
rm -rf bin
mkdir -p bin

# Compile the source files
echo "Compiling..."
javac -d bin src/com/sokoban/main/*.java src/com/sokoban/model/*.java src/com/sokoban/solver/*.java src/com/sokoban/heuristic/*.java src/com/sokoban/util/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Creating JAR file..."
    
    # Create a manifest file
    echo "Main-Class: com.sokoban.main.SokobanMain" > manifest.txt
    
    # Create the JAR file
    jar cfm SokobanSolver.jar manifest.txt -C bin .
    
    # Clean up the manifest file
    rm manifest.txt
    
    echo "JAR file created: SokobanSolver.jar"
    echo "To run the program: java -jar SokobanSolver.jar [-option] [Sokoban input file] [-t timeout]"
else
    echo "Compilation failed!"
fi
