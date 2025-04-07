#!/bin/bash
cd ..

echo "===== Running Test Case 1 (Simple Puzzle) ====="
echo "BFS:"
java -jar SokobanSolver.jar -b test/TestCase1_Simple.txt
echo -e "\nDFS:"
java -jar SokobanSolver.jar -d test/TestCase1_Simple.txt
echo -e "\nA* (Manhattan):"
java -jar SokobanSolver.jar -am test/TestCase1_Simple.txt

echo -e "\n\n===== Running Test Case 2 (Unsolvable Puzzle) ====="
echo "BFS with timeout:"
java -jar SokobanSolver.jar -b test/TestCase2_Unsolvable.txt -t 5
echo -e "\nA* (Improved Manhattan) with timeout:"
java -jar SokobanSolver.jar -ai test/TestCase2_Unsolvable.txt -t 5

echo -e "\n\n===== Running Test Case 3 (Complex Puzzle) ====="
echo "A* (Manhattan) with timeout:"
java -jar SokobanSolver.jar -am test/TestCase3_Complex.txt -t 5
echo -e "\nA* (Improved Manhattan) with timeout:"
java -jar SokobanSolver.jar -ai test/TestCase3_Complex.txt -t 5

echo -e "\n\n===== Running Test Case 4 (Already Solved Puzzle) ====="
echo "BFS:"
java -jar SokobanSolver.jar -b test/TestCase4_AlreadySolved.txt

echo -e "\n\n===== Running Test Case 5 (Edge Case) ====="
echo "BFS:"
java -jar SokobanSolver.jar -b test/TestCase5_EdgeCase.txt
echo -e "\nDFS:"
java -jar SokobanSolver.jar -d test/TestCase5_EdgeCase.txt
